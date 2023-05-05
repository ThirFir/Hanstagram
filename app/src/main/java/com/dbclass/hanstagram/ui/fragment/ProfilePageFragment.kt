package com.dbclass.hanstagram.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentProfilePageBinding
import com.dbclass.hanstagram.data.repository.FollowRepository
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.viewmodel.IsFollowingViewModel
import com.dbclass.hanstagram.ui.activity.*
import com.dbclass.hanstagram.ui.adapter.ProfileViewPagerFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var followViewModel: IsFollowingViewModel
    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var activityEditedResult: ActivityResultLauncher<Intent>
    private var postsCount = 0L
    private var followersCount = 0L
    private var followingsCount = 0L
    private var ownerID: String? = null     // 프로필 주인 ID
    private var myID: String? = null     // 본인 ID
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        ownerID = arguments?.getString("user_id") ?: userViewModel.user.value?.id
        myID = userViewModel.user.value?.id
        followViewModel = ViewModelProvider(this)[IsFollowingViewModel::class.java]

        val profileViewPagerFragmentAdapter = ProfileViewPagerFragmentAdapter(requireActivity()).apply {
            ownerID?.let { setProfileOwnerID(it) }
        }
        binding.viewpagerOfProfile.adapter = profileViewPagerFragmentAdapter
        val tabIcons = listOf(R.drawable.ic_page_48, R.drawable.ic_comments_48)
        TabLayoutMediator(binding.tabLayout, binding.viewpagerOfProfile) { tab, pos ->
            tab.setIcon(tabIcons[pos])
        }.attach()

        activityEditedResult =
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

        setViewButtonState()
        setViewProfileInfo()
        setViewCounts()

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }


    private fun setViewCounts() {
        CoroutineScope(Dispatchers.Default).launch {
            followersCount = ownerID?.let { FollowRepository.getFollowersCount(it) } ?: 0
            followingsCount = ownerID?.let { FollowRepository.getFollowingsCount(it) } ?: 0
            postsCount = ownerID?.let { PostRepository.getPostsCount(it) } ?: 0
            CoroutineScope(Dispatchers.Main).launch {
                binding.textFollowerCount.text = followersCount.toString()
                binding.textFollowingCount.text = followingsCount.toString()
                binding.textPostCount.text = postsCount.toString()
            }
        }
    }

    private fun setViewProfileInfo() {

        if (isMyProfile()) {
            Glide.with(requireContext()).load(userViewModel.user.value?.profileImage)
                .error(R.drawable.baseline_account_circle_24)
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(binding.imageProfile)


            userViewModel.user.observe(viewLifecycleOwner) {
                binding.run {
                    textNickname.text = userViewModel.user.value?.nickname
                    textContent.text = userViewModel.user.value?.caption
                    textFollowingCount.text        // TODO : followerID = id 인 count
                    textFollowerCount.text
                    Glide.with(this@ProfilePageFragment)
                        .load(userViewModel.user.value?.profileImage)
                        .error(R.drawable.baseline_account_circle_24)
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .into(imageProfile)
                }
            }
        } else {

            // 타인 프로필
            CoroutineScope(Dispatchers.Default).launch {
                val user = ownerID?.let { UserRepository.getUser(it) }

                CoroutineScope(Dispatchers.Main).launch {
                    binding.textNickname.text = user?.nickname
                    binding.textContent.text = user?.caption
                    Glide.with(requireContext()).load(user?.profileImage)
                        .error(R.drawable.baseline_account_circle_24)
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .into(binding.imageProfile)
                }
            }
        }
    }

    private fun setViewButtonState() {

        if (isMyProfile()) {
            binding.buttonFollow.text = getString(R.string.text_edit_profile)
            binding.buttonMessage.text = getString(R.string.text_logout)

            // follow버튼을 프로필 편집 버튼으로 사용
            binding.buttonFollow.setOnClickListener {
                val intent = Intent(context, ProfileEditActivity::class.java).apply {
                    putExtra("nickname", userViewModel.user.value?.nickname)
                    putExtra("caption", userViewModel.user.value?.caption)
                    putExtra("user_id", myID)
                    putExtra("image_uri", userViewModel.user.value?.profileImage)
                }
                activityEditedResult.launch(intent)
            }

            // message버튼을 로그아웃 버튼으로 사용
            binding.buttonMessage.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java).apply{
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
            }
        } else {
            followViewModel.initializeFollow(myID, ownerID)

            followViewModel.followerCount.observe(viewLifecycleOwner) {
                binding.textFollowerCount.text = it.toString()
            }
            followViewModel.followPID.observe(viewLifecycleOwner) {
                if(it != null){
                    binding.buttonFollow.text = getString(R.string.text_following_now)
                } else {
                    binding.buttonFollow.text = getString(R.string.text_follow)
                }
            }

            binding.buttonFollow.setOnClickListener {
                if(myID != null && ownerID != null)
                    followViewModel.follow(follower = myID!!, following = ownerID!!)
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
                val intent = Intent(requireActivity(), NewPostActivity::class.java).apply {
                    putExtra("user_id", userViewModel.user.value?.id)
                }
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}