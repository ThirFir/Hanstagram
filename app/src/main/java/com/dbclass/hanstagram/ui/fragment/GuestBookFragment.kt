package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.FragmentGuestBookBinding


class GuestBookFragment : Fragment() {

    private lateinit var binding: FragmentGuestBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuestBookBinding.inflate(inflater, container, false)
        return binding.root
    }

}