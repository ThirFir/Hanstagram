package com.dbclass.hanstagram.ui.fragment

import android.app.Activity
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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentProfilePageBinding
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.repository.FollowRepository
import com.dbclass.hanstagram.ui.activity.ProfileEditActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfilePageBinding
    private val postsCountLiveData = MutableLiveData<Int>()
    private lateinit var activityEditedResult: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        val userID = arguments?.getString("user_id")

        activityEditedResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                val editedImageURI = it.data?.getStringExtra("image_uri")
                val editedNickname = it.data?.getStringExtra("nickname")
                val editedCaption = it.data?.getStringExtra("caption")
                if (editedImageURI != null)
                    userViewModel.setProfileImage(editedImageURI)
                if (editedNickname != null)
                    userViewModel.setNickname(editedNickname)
                if(editedCaption != null)
                    userViewModel.setCaption(editedCaption)
            }
        }

        setButtonState(userID)
        setPostsCount(userViewModel.user.value?.id)
        Glide.with(requireContext()).load(userViewModel.user.value?.profileImage).error(R.drawable.baseline_account_circle_24).into(binding.imageProfile)

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.run {
                textNickname.text = userViewModel.user.value?.nickname
                textCaption.text = userViewModel.user.value?.caption
                textFollowingCount.text        // followerID = id 인 count
                textFollowerCount.text
                Glide.with(this@ProfilePageFragment).load(userViewModel.user.value?.profileImage).into(imageProfile)
            }
        }

        return binding.root
    }

    private fun setPostsCount(userID: String?) {
        postsCountLiveData.observe(viewLifecycleOwner) {
            binding.textPostCount.text = it.toString()  // user의 count(post)
        }
        if (userID != null)
            CoroutineScope(Dispatchers.Default).launch {
                val db = HanstagramDatabase.getInstance(requireContext())
                val count = db?.postsDao()?.getPostsCount(userID) ?: 0
                postsCountLiveData.postValue(count)
            }
    }

    private fun setButtonState(userID: String?) {
        // 본인 프로필 or 타인 프로필
        if (userID == null) {
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
                    if (FollowRepository.isFollowing(it, userID)){
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
}