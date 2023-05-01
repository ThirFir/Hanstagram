package com.dbclass.hanstagram.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentProfilePageBinding
import com.dbclass.hanstagram.data.repository.FollowRepository
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.ui.ProfileViewPagerFragmentAdapter
import com.dbclass.hanstagram.ui.activity.ProfileEditActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfilePageBinding
    private val postsCountLiveData = MutableLiveData<Int>()
    private val followersCountLiveData = MutableLiveData<Int>()
    private val followingsCountLiveData = MutableLiveData<Int>()
    private lateinit var activityEditedResult: ActivityResultLauncher<Intent>
    private var userID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfilePageBinding.inflate(inflater, container, false)

        userID = arguments?.getString("user_id") ?: userViewModel.user.value?.id
        Log.d("ProfilePageFragment", userViewModel.user.value?.profileImage ?: "ddd")

        binding.viewpagerOfProfile.adapter = ProfileViewPagerFragmentAdapter(requireActivity())
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

        setButtonState()
        setProfileInfo()
        setPostsCount()
        setFollowersCount()
        setFollowingsCount()

        return binding.root
    }

    private fun setPostsCount() {
        postsCountLiveData.observe(viewLifecycleOwner) {
            binding.textPostCount.text = it.toString()  // user의 count(post)
        }
        CoroutineScope(Dispatchers.Default).launch {
            val count = userID?.let { PostRepository.getPostsCount(it) }
            postsCountLiveData.postValue(count ?: 0)
        }
    }

    private fun setFollowersCount() {
        followersCountLiveData.observe(viewLifecycleOwner) {
            binding.textFollowerCount.text = it.toString()  // user의 count(post)
        }
        CoroutineScope(Dispatchers.Default).launch {
            val count = userID?.let { FollowRepository.getFollowersCount(it) }
            followersCountLiveData.postValue(count ?: 0)
        }
    }

    private fun setFollowingsCount() {
        followingsCountLiveData.observe(viewLifecycleOwner) {
            binding.textFollowingCount.text = it.toString()  // user의 count(post)
        }
        CoroutineScope(Dispatchers.Default).launch {
            val count = userID?.let { FollowRepository.getFollowingsCount(it) }
            followingsCountLiveData.postValue(count ?: 0)
        }
    }

    private fun setProfileInfo() {

        if (isMyProfile()) {
            Glide.with(requireContext()).load(userViewModel.user.value?.profileImage)
                .error(R.drawable.baseline_account_circle_24)
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(binding.imageProfile)

            userViewModel.user.observe(viewLifecycleOwner) {
                binding.run {
                    textNickname.text = userViewModel.user.value?.nickname
                    textContent.text = userViewModel.user.value?.caption
                    textFollowingCount.text        // TODO: followerID = id 인 count
                    textFollowerCount.text
                    Glide.with(this@ProfilePageFragment)
                        .load(userViewModel.user.value?.profileImage)
                        .error(R.drawable.baseline_account_circle_24)
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .into(imageProfile)
                }
            }
        } else {
            CoroutineScope(Dispatchers.Default).launch {
                val user = UserRepository.getUser(userID!!)

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

    private fun setButtonState() {

        if (isMyProfile()) {
            binding.buttonMessage.isVisible = false
            binding.buttonFollow.text = getString(R.string.text_edit_profile)

            // follow버튼을 프로필 편집 버튼으로 사용
            binding.buttonFollow.setOnClickListener {
                val intent = Intent(context, ProfileEditActivity::class.java).apply {
                    putExtra("nickname", userViewModel.user.value?.nickname)
                    putExtra("caption", userViewModel.user.value?.caption)
                    putExtra("user_id", userViewModel.user.value?.id)
                    putExtra("image_uri", userViewModel.user.value?.profileImage)
                }
                activityEditedResult.launch(intent)
            }
        } else {
            CoroutineScope(Dispatchers.Default).launch {
                userViewModel.user.value?.id?.let {
                    if (FollowRepository.isFollowing(it, userID!!)) {
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.buttonFollow.text = getString(R.string.text_following_now)
                        }
                    }
                }
            }
            binding.buttonFollow.setOnClickListener {
            }
        }
    }

    private fun isMyProfile(): Boolean = userID != null && userID == userViewModel.user.value?.id
}