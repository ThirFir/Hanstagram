package com.dbclass.hanstagram.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.DialogFragmentPostBinding
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostFragmentDialog : DialogFragment() {

    private lateinit var binding: DialogFragmentPostBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewOnePost.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Default).launch {
             val postWithUser = PostRepositoryImpl.getPostWithUser(arguments?.getLong("post_id"))
            //val postWithUser = PostRepository.getPostWithUser(arguments?.getLong("post_id"))
            if (postWithUser == null) {
                Toast.makeText(requireContext(), R.string.toast_error_load_post, Toast.LENGTH_SHORT)
                    .show()
                Log.d("PostFragmentDialog", getString(R.string.toast_error_load_post))

                dismiss()
            }
            CoroutineScope(Dispatchers.Main).launch {

                // View가 완전히 그려진 후 실행
                view.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.recyclerviewOnePost.adapter =
                            PostAdapter(
                                postWithUser!!.toList(),
                                userViewModel.user.value?.id, requireContext(),
                                binding.root.width + 1
                            )
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
                )
            }
        }

    }

}

