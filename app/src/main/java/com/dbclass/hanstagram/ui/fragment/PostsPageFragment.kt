package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.ui.PostAdapter
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentPostsPageBinding
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.posts.PostEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsPageFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val binding = FragmentPostsPageBinding.inflate(inflater, container, false)

        binding.recyclerviewPosts.layoutManager = LinearLayoutManager(context)


        val posts1 = listOf(
            PostEntity(
                userID = "test1",
                caption = "ddddd",
                images = "",
                createdTime = System.currentTimeMillis()
            ), PostEntity(
                userID = "test2",
                caption = "ddddddd",
                images = "",
                createdTime = System.currentTimeMillis()
            )
        )
        CoroutineScope(Dispatchers.Default).launch {
            val db = context?.let { HanstagramDatabase.getInstance(it) }
            val posts =
                userViewModel.user.value?.id?.let { db?.postsDao()?.getFollowingPosts(id = it) }
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewPosts.adapter = PostAdapter(posts1)
            }
        }
        return binding.root
    }


}
