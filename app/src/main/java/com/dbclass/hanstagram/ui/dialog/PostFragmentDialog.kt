package com.dbclass.hanstagram.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.utils.getImageHeightWithWidthFully
import com.dbclass.hanstagram.data.utils.startPostCommentActivity
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.DialogFragmentPostBinding
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
            val post = PostRepository.getPost(arguments?.getLong("post_id"))
            if (post == null) {
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
                                listOf(post!!),
                                userViewModel.user.value?.id,
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

