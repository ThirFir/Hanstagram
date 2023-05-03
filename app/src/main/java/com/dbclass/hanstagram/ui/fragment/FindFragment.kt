package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.FragmentFindBinding
import com.dbclass.hanstagram.databinding.ItemFoundUserBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.adapter.FoundUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FindFragment : Fragment() {
    private lateinit var binding: FragmentFindBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindBinding.inflate(inflater, container, false)

        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        binding.recyclerviewFoundUsers.layoutManager = LinearLayoutManager(requireActivity())

        binding.imageButtonFind.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                binding.editTextFind.text.ifEmpty { return@launch }
                val foundUsers = UserRepository.findUsers(binding.editTextFind.text.toString()) as MutableList
                CoroutineScope(Dispatchers.Main).launch {
                    binding.recyclerviewFoundUsers.adapter = FoundUserAdapter(foundUsers, requireContext())
                }
            }
        }
        return binding.root
    }


}