package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.repository.GuestCommentRepository
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentGuestBookBinding
import com.dbclass.hanstagram.ui.GuestAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GuestBookFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentGuestBookBinding
    private var userID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuestBookBinding.inflate(inflater, container, false)

        userID = arguments?.getString("user_id") ?: userViewModel.user.value?.id

        CoroutineScope(Dispatchers.Default).launch {
            val guestComments = userID?.let { GuestCommentRepository.getGuestComments(it) }

            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewGuestComments.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.recyclerviewGuestComments.adapter =
                    GuestAdapter(guestComments as MutableList<GuestCommentEntity>)
            }
        }

        binding.buttonAddGuestComment.setOnClickListener {
            val fromUserID = userViewModel.user.value?.id ?: return@setOnClickListener
            val toUserID = userID ?: userViewModel.user.value?.id ?: return@setOnClickListener
            val commentText =
                binding.editTextGuestComment.text.ifEmpty { return@setOnClickListener }
            val comment = commentText.toString()
            val createdTime = System.currentTimeMillis()
            (binding.recyclerviewGuestComments.adapter as GuestAdapter).addComment(
                GuestCommentEntity(fromUserID, toUserID, comment, createdTime)
            )
            binding.editTextGuestComment.text.clear()
        }

        return binding.root
    }

    private fun isMyProfile(): Boolean = userID != null && userID == userViewModel.user.value?.id
}