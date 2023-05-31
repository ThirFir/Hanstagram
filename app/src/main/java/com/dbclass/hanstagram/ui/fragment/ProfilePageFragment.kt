package com.dbclass.hanstagram.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.follow.FollowRepository
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentProfilePageBinding
import com.dbclass.hanstagram.data.repository.follow.FollowRepositoryImpl
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.utils.IntegerConstants.ALL
import com.dbclass.hanstagram.data.utils.IntegerConstants.FOLLOWERS
import com.dbclass.hanstagram.data.utils.IntegerConstants.FOLLOWINGS
import com.dbclass.hanstagram.data.utils.StringConstants.OWNER_ID
import com.dbclass.hanstagram.data.utils.StringConstants.USER_ID
import com.dbclass.hanstagram.ui.activity.*
import com.dbclass.hanstagram.ui.adapter.ProfileViewPagerFragmentAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageFragment private constructor() : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var binding: FragmentProfilePageBinding

    private lateinit var profileEditActivityResult: ActivityResultLauncher<Intent>
    private lateinit var newPostAddActivityResult: ActivityResultLauncher<Intent>

    private val followRepository: FollowRepository = FollowRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl
    private val postRepository: PostRepository = PostRepositoryImpl

    private var ownerID: String? = null     // 프로필 주인 ID
    private var myID: String? = null     // 본인 ID

    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

    val postsCount = MutableLiveData<Long>()
    val followersCount = MutableLiveData<Long>()
    val followingsCount = MutableLiveData<Long>()
    val followState = MutableLiveData<String>()
    val temperature = MutableLiveData<Float>()

    companion object {

        fun newInstance(ownerID: String?): ProfilePageFragment {
            val args = Bundle().apply {
                putString(OWNER_ID, ownerID)
            }

            val fragment = ProfilePageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        binding =
            DataBindingUtil.inflate<FragmentProfilePageBinding?>(
                inflater, R.layout.fragment_profile_page, container, false).apply {
                    profilePageFragment = this@ProfilePageFragment
                    lifecycleOwner = this@ProfilePageFragment
                }
        ownerID = arguments?.getString(OWNER_ID) ?: userViewModel.user.value?.id
        myID = userViewModel.user.value?.id

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        binding.wrapperTextFollowers.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, FollowUsersFragment
                    .newInstance(FOLLOWERS, ownerID!!)).commit()
        }
        binding.wrapperTextFollowings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, FollowUsersFragment
                    .newInstance(FOLLOWINGS, ownerID!!)).commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileViewPagerFragmentAdapter =
            ProfileViewPagerFragmentAdapter(requireActivity()).apply {
                ownerID?.let { setProfileOwnerID(it) }
            }
        binding.viewpagerOfProfile.adapter = profileViewPagerFragmentAdapter
        val tabIcons = listOf(R.drawable.ic_page_48, R.drawable.ic_comments_48)
        TabLayoutMediator(binding.tabLayout, binding.viewpagerOfProfile) { tab, pos ->
            tab.setIcon(tabIcons[pos])
        }.attach()

        profileEditActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val editedImageURI = it.data?.getStringExtra("image_uri")
                    val editedNickname = it.data?.getStringExtra("nickname")
                    val editedCaption = it.data?.getStringExtra("caption")
                    if (editedImageURI != null)
                        userViewModel.setProfileImage(editedImageURI)
                    if (editedNickname != null)
                        userViewModel.setNickname(editedNickname)
                    if (editedCaption != null)
                        userViewModel.setCaption(editedCaption)
                }
            }
        newPostAddActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_content,
                            PostsPageFragment.newInstance(ALL)
                        )
                        .commit()
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        .selectedItemId = R.id.item_posts
                }
            }
        setView()

    }

    private fun setView() {
        setViewButtonState()
        setViewProfileInfo()
        setViewData()
    }

    private fun setViewData() {
        uiScope.launch {
            postsCount.value = ownerID?.let { postRepository.getPostsCount(it) } ?: 0
            followersCount.value = ownerID?.let { followRepository.getFollowersCount(it) } ?: 0
            followingsCount.value = ownerID?.let { followRepository.getFollowingsCount(it) } ?: 0
            temperature.value = ownerID?.let { userRepository.getTemperature(it) } ?: 0f
            followState.value = ownerID?.let {
                if(isMyProfile()) getString(R.string.text_edit_profile)
                else {
                    if (followRepository.getFollowPID(
                            follower = myID!!,
                            following = ownerID!!
                        ) == null
                    ) getString(R.string.text_follow)
                    else getString(R.string.text_following_now)
                }
            } ?: "NULL"
        }
    }

    private fun setViewProfileInfo() {
        if (isMyProfile()) {
            Glide.with(requireContext()).load(userViewModel.user.value?.profileImage)
                .error(R.drawable.ic_account_96)
                .placeholder(R.drawable.ic_account_96)
                .into(binding.imageProfile)

            userViewModel.user.observe(viewLifecycleOwner) {
                binding.run {
                    textNickname.text = it.nickname
                    textContent.text = it.caption
                    textStudentId.text = it.studentID
                    textDepartment.text = it.department
                    Glide.with(this@ProfilePageFragment)
                        .load(it.profileImage)
                        .error(R.drawable.ic_account_96)
                        .placeholder(R.drawable.ic_account_96)
                        .into(imageProfile)
                }
            }
        } else {     // 타인 프로필
            uiScope.launch {
                val user = ownerID?.let { userRepository.getUser(it) }
                binding.run {
                    textNickname.text = user?.nickname
                    textContent.text = user?.caption
                    textStudentId.text = user?.studentID
                    textDepartment.text = user?.department
                    Glide.with(requireContext()).load(user?.profileImage)
                        .error(R.drawable.ic_account_96)
                        .placeholder(R.drawable.ic_account_96)
                        .into(imageProfile)
                }
            }
        }
    }

    private fun setViewButtonState() {

        if (isMyProfile()) {
            binding.buttonMessage.text = getString(R.string.text_logout)

            // follow버튼을 프로필 편집 버튼으로 사용
            binding.buttonFollow.setOnClickListener {
                val intent = Intent(context, ProfileEditActivity::class.java).apply {
                    putExtra("nickname", userViewModel.user.value?.nickname)
                    putExtra("caption", userViewModel.user.value?.caption)
                    putExtra("user_id", myID)
                    putExtra("image_uri", userViewModel.user.value?.profileImage)
                    putExtra("department", userViewModel.user.value?.department)
                }
                profileEditActivityResult.launch(intent)
            }

            // message버튼을 로그아웃 버튼으로 사용
            binding.buttonMessage.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
            }
        } else {
            binding.buttonFollow.setOnClickListener {
                uiScope.launch {
                    if (myID != null && ownerID != null) {
                        if(followRepository.getFollowPID(follower = myID!!, following = ownerID!!) == null) {
                            followRepository.doFollow(follower = myID!!, following = ownerID!!)
                            followState.value = getString(R.string.text_following_now)
                            followersCount.value = followersCount.value?.plus(1)
                        } else {
                            followRepository.doUnFollow(follower = myID!!, following = ownerID!!)
                            followState.value = getString(R.string.text_follow)
                            followersCount.value = followersCount.value?.minus(1)
                        }
                        binding.invalidateAll()
                    }
                }
            }

            binding.buttonMessage.setOnClickListener {
                val intent = Intent(requireActivity(), SendMessageActivity::class.java).apply {
                    putExtra("from_id", myID)
                    putExtra("to_id", ownerID)
                }
                startActivity(intent)
            }
        }
    }

    private fun isMyProfile(): Boolean = ownerID != null && ownerID == userViewModel.user.value?.id

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_frag_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.icon_add_new_post -> {
                val newPostAddActivity = PostAddActivity().apply {
                    setOnEditCompleteListener { userID, images, content ->
                        uiScope.launch {
                            postRepository.addPost(
                                PostEntity(
                                    userID = userID,
                                    content = content,
                                    images = images,
                                    createdTime = System.currentTimeMillis()
                                )
                            )

                            Toast.makeText(requireContext(), "게시글이 작성되었습니다", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
                val intent = Intent(requireActivity(), newPostAddActivity::class.java).apply {
                    putExtra(USER_ID, userViewModel.user.value?.id)
                }
                newPostAddActivityResult.launch(intent)
                true
            }

            else -> false
        }
    }
}

