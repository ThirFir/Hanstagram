package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentThumbnailsListBinding
import com.dbclass.hanstagram.ui.ThumbnailsAdapter


class ThumbnailsListFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentThumbnailsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThumbnailsListBinding.inflate(inflater, container, false)


        binding.recyclerviewUserPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewUserPosts.adapter = ThumbnailsAdapter(listOf(), requireContext())
        return binding.root
    }
}