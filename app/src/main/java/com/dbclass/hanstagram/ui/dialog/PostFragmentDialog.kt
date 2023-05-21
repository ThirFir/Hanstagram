package com.dbclass.hanstagram.ui.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.DialogFragmentPostBinding
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import com.dbclass.hanstagram.ui.fragment.PostsPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostFragmentDialog : DialogFragment() {

    private lateinit var binding: DialogFragmentPostBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val postRepository: PostRepository = PostRepositoryImpl
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)
    private lateinit var postEditActivityResult: ActivityResultLauncher<Intent>

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

        postEditActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_content,
                            PostsPageFragment.newInstance(PostsPageFragment.ALL)
                        )
                        .commit()
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        .selectedItemId = R.id.item_posts
                }
            }

        binding.recyclerviewOnePost.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Default).launch {
            val postWithUser = postRepository.getPostWithUser(arguments?.getLong("post_id"))
            if (postWithUser == null) {
                Toast.makeText(requireContext(), R.string.toast_error_load_post, Toast.LENGTH_SHORT)
                    .show()
                Log.d("PostFragmentDialog", getString(R.string.toast_error_load_post))
                dismiss()
            }

            // View가 완전히 그려진 후 실행
            uiScope.launch {
                view.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        val pwu = mutableListOf<Pair<PostEntity, UserEntity>>()
                        pwu.addAll(postWithUser!!.toList())
                        binding.recyclerviewOnePost.adapter =
                            PostAdapter(
                                pwu,
                                userViewModel.user.value?.id, requireContext(),
                                binding.root.width + 1,
                                postEditActivityResult
                            )
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
                )

            }
        }

    }

}

