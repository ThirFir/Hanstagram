package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.repository.GuestCommentRepository
import com.dbclass.hanstagram.data.utils.closeKeyboard
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentGuestBookBinding
import com.dbclass.hanstagram.ui.adapter.GuestAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GuestBookFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentGuestBookBinding
    private var ownerID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuestBookBinding.inflate(inflater, container, false)

        ownerID = arguments?.getString("owner_id") ?: userViewModel.user.value?.id

        CoroutineScope(Dispatchers.Default).launch {
            val guestComments = ownerID?.let { GuestCommentRepository.getGuestComments(it) }

            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewGuestComments.layoutManager =
                    LinearLayoutManager(requireContext())
                if (guestComments != null)
                    binding.recyclerviewGuestComments.adapter =
                        GuestAdapter(
                            guestComments as MutableList<GuestCommentEntity>,
                            userViewModel.user.value?.id,
                            requireContext()
                        )
            }
        }

        binding.buttonAddGuestComment.setOnClickListener {
            val fromUserID = userViewModel.user.value?.id ?: return@setOnClickListener
            val toUserID = ownerID ?: userViewModel.user.value?.id ?: return@setOnClickListener
            val commentText =
                binding.editTextGuestComment.text.ifEmpty { return@setOnClickListener }
            val comment = commentText.toString()
            val createdTime = System.currentTimeMillis()
            (binding.recyclerviewGuestComments.adapter as GuestAdapter).addComment(
                GuestCommentEntity(fromUserID, toUserID, comment, createdTime)
            )
            requireContext().closeKeyboard(binding.editTextGuestComment)
            binding.editTextGuestComment.text.clear()
        }


        val layout = inflater.inflate(R.layout.item_guest_comment, container, false) as ConstraintLayout
        val view = layout.findViewById<ImageView>(R.id.image_guest_profile)
        userViewModel.user.observe(viewLifecycleOwner) {
            userViewModel.user.value?.profileImage?.let {
                Glide.with(requireContext()).load(it)
                    .into(view)
            }
        }

        return binding.root
    }
}