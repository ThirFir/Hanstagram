package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbclass.hanstagram.databinding.FragmentUserPostsListBinding


class UserPostsListFragment : Fragment() {

    private lateinit var binding: FragmentUserPostsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPostsListBinding.inflate(inflater, container, false)
        return binding.root
    }
}