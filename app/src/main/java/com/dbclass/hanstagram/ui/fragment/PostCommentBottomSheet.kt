package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.comment.CommentRepository
import com.dbclass.hanstagram.data.repository.comment.CommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentPostCommentBottomSheetBinding
import com.dbclass.hanstagram.ui.adapter.PostCommentAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostCommentBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPostCommentBottomSheetBinding
    private val commentRepository: CommentRepository = CommentRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostCommentBottomSheetBinding.inflate(inflater, container, false)
        binding.recyclerviewPostComments.layoutManager = LinearLayoutManager(requireContext())

        val postID = arguments?.getLong("post_id")
        if (postID == null) {
            Log.d("PostCommentBottomSheet", "Post ID Load Failure")
            dismiss()
        }


        CoroutineScope(Dispatchers.Default).launch {
            val image = userRepository.getProfileImage(userViewModel.user.value?.id ?: return@launch)
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(requireContext()).load(image).error(R.drawable.ic_account_96).into(binding.imageProfile)
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            val comments = postID?.let { commentRepository.getComments(it) }
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewPostComments.adapter =
                    PostCommentAdapter(comments as MutableList, postID, userViewModel.user.value?.id)
                binding.textButtonLeaveComment.setOnClickListener {
                    binding.editTextPostComment.text.ifEmpty {
                        Toast.makeText(requireContext(), "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    (binding.recyclerviewPostComments.adapter as PostCommentAdapter).addComment(
                        userViewModel.user.value?.id ?: return@setOnClickListener,
                        binding.editTextPostComment.text.toString()
                    )
                    binding.editTextPostComment.setText("")
                }
            }
        }

        return binding.root
    }
}