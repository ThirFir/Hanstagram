package com.dbclass.hanstagram.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.message.MessageRepository
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentPostsPageBinding
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.utils.IntegerConstants.ALL
import com.dbclass.hanstagram.data.utils.StringConstants.LOAD
import com.dbclass.hanstagram.data.utils.StringConstants.USER_ID
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.MessageBoxActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsPageFragment private constructor() : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val messageRepository: MessageRepository = MessageRepositoryImpl
    private val postRepository: PostRepository = PostRepositoryImpl
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)
    private lateinit var postEditActivityResult: ActivityResultLauncher<Intent>

    private lateinit var binding: FragmentPostsPageBinding

    companion object {

        fun newInstance(load: Int): PostsPageFragment {
            val args = bundleOf(LOAD to load)

            val fragment = PostsPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsPageBinding.inflate(inflater, container, false)

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

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
                            newInstance(ALL)
                        )
                        .commit()
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        .selectedItemId = R.id.item_posts
                }
            }

        binding.recyclerviewPosts.layoutManager = LinearLayoutManager(context)
        uiScope.launch {
            val load = arguments?.getInt(LOAD) ?: 1000
            val postsWithUser =
                if (load == ALL) userViewModel.user.value?.id?.let {
                    postRepository.getAllPostsWithUsers(0)
                }
                else userViewModel.user.value?.id?.let {
                    postRepository.getFollowingPostsWithUsers(it)
                }

            val pwu = mutableListOf<Pair<PostEntity, UserEntity>>()
            pwu.addAll(postsWithUser?.toList() ?: listOf())
            binding.recyclerviewPosts.adapter =
                PostAdapter(
                    pwu,
                    userViewModel.user.value?.id,
                    requireContext(),
                    postEditActivityResult = postEditActivityResult
                )

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        uiScope.launch {
            val newMessageExists = userViewModel.user.value?.id?.let {
                messageRepository.getHasUnreadMessage(it)
            }
            if (newMessageExists != null && !newMessageExists)  // TODO : New Message Icon
                inflater.inflate(R.menu.post_frag_app_bar_menu, menu)
            else
                inflater.inflate(R.menu.post_frag_app_bar_menu, menu)

        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_message_box -> {
                val intent = Intent(requireActivity(), MessageBoxActivity::class.java).apply {
                    putExtra(USER_ID, userViewModel.user.value?.id)
                }
                startActivity(intent)

                true
            }

            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        //requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.item_posts
    }
}
