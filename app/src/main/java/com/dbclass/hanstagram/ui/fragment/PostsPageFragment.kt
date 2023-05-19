package com.dbclass.hanstagram.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.message.MessageRepository
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentPostsPageBinding
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.MessageBoxActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsPageFragment private constructor() : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val messageRepository: MessageRepository = MessageRepositoryImpl

    companion object {
        val ALL: Int = 1000
        val FOLLOW: Int = 1001
        fun newInstance(id: Int): PostsPageFragment {
            val args = bundleOf("id" to id)

            val fragment = PostsPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsPageBinding.inflate(inflater, container, false)

        binding.recyclerviewPosts.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.Default).launch {
            val id = arguments?.getInt("id") ?: 100
            val posts =
                if (id == ALL) userViewModel.user.value?.id?.let { PostRepositoryImpl.getAllPostsWithUsers(0) }
                else userViewModel.user.value?.id?.let { PostRepositoryImpl.getFollowingPostsWithUsers(it) }

            val a =  mapOf(PostEntity(userID = "", content = null, images = "", createdTime = 0, postID = 0) to 3)

            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewPosts.adapter =
                    PostAdapter(posts?.toList() ?: listOf(), userViewModel.user.value?.id, requireContext())
            }
        }

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        CoroutineScope(Dispatchers.Default).launch {
            val newMessageExists = userViewModel.user.value?.id?.let {
                messageRepository.getHasUnreadMessage(it)
            }
            CoroutineScope(Dispatchers.Main).launch {
                if (newMessageExists != null && !newMessageExists)
                    inflater.inflate(R.menu.post_frag_app_bar_menu, menu)
                else
                    inflater.inflate(R.menu.post_frag_app_bar_menu, menu)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_message_box -> {
                val intent = Intent(requireActivity(), MessageBoxActivity::class.java).apply {
                    putExtra("user_id", userViewModel.user.value?.id)
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
