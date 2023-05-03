package com.dbclass.hanstagram.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.ui.adapter.PostAdapter
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentPostsPageBinding
import com.dbclass.hanstagram.data.repository.MessageRepository
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.MessageBoxActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsPageFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsPageBinding.inflate(inflater, container, false)

        binding.recyclerviewPosts.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.Default).launch {
            val posts = userViewModel.user.value?.id?.let { PostRepository.getAllPosts(0) }
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewPosts.adapter = PostAdapter(posts ?: listOf(), requireContext())
            }
        }

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        CoroutineScope(Dispatchers.Default).launch {
            val newMessageExists = userViewModel.user.value?.id?.let {
                MessageRepository.getHasUnreadMessage(it)
            }
            CoroutineScope(Dispatchers.Main).launch {
                if(newMessageExists != null && !newMessageExists)
                    inflater.inflate(R.menu.post_frag_app_bar_menu, menu)
                else
                    inflater.inflate(R.menu.post_frag_app_bar_menu, menu)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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

}
