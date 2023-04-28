package com.dbclass.hanstagram.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentProfilePageBinding
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentProfilePageBinding
    private val postsCountLiveData = MutableLiveData<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setPostsCount(userViewModel.user.value?.id)

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.run {
                textCaption.text = userViewModel.user.value?.caption
                textFollowingCount.text        // followerID = id 인 count
                textFollowerCount.text

                imageProfile.setOnClickListener {

                }
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

    private fun runGalleryApp() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val imageURL = it.data?.data.toString()
            userViewModel.setProfileImage(imageURL)
        }
    }
}