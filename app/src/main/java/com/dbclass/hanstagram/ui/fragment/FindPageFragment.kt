package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.FragmentFindBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.adapter.FoundUserAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class FindPageFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentFindBinding

    companion object {
        fun newInstance(): FindPageFragment {
            val args = Bundle()

            val fragment = FindPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindBinding.inflate(inflater, container, false)

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewFoundUsers.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewFoundUsers.adapter = FoundUserAdapter(mutableListOf())

        binding.imageButtonFind.setOnClickListener {
            binding.editTextFind.text.ifEmpty { return@setOnClickListener }
            (binding.recyclerviewFoundUsers.adapter as FoundUserAdapter).findUsers(binding.editTextFind.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        //requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.item_find
    }

}