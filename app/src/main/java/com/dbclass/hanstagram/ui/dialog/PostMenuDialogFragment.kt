package com.dbclass.hanstagram.ui.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.utils.IntegerConstants.DELETE
import com.dbclass.hanstagram.data.utils.IntegerConstants.EDIT
import com.dbclass.hanstagram.data.utils.IntegerConstants.REPORT
import com.dbclass.hanstagram.data.utils.StringConstants.MY_ID
import com.dbclass.hanstagram.data.utils.StringConstants.OWNER_ID
import com.dbclass.hanstagram.data.utils.StringConstants.POST_ID
import com.dbclass.hanstagram.databinding.ItemFragmentPostMenuDialogBinding
import com.dbclass.hanstagram.databinding.FragmentPostMenuDialogBinding


class PostMenuDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPostMenuDialogBinding

    private val menuItemsForOwner =
        listOf(Pair("수정", R.drawable.baseline_edit_32), Pair("삭제", R.drawable.baseline_delete_32))
    private val menuItemsForGuest = listOf(Pair("신고", R.drawable.ic_siren_48))

    private var postID: Long? = null
    private var ownerID: String? = null
    private var myID: String? = null

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostMenuDialogBinding.inflate(inflater, container, false)
        arguments?.let {
            postID = it.getLong(POST_ID)
            ownerID = it.getString(OWNER_ID)
            myID = it.getString(MY_ID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerviewPostMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewPostMenu.adapter = PostMenuAdapter()
    }


    private inner class PostMenuAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private inner class PostMenuViewHolder(val binding: ItemFragmentPostMenuDialogBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostMenuViewHolder {

            return PostMenuViewHolder(
                ItemFragmentPostMenuDialogBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PostMenuViewHolder).binding.run {
                if (ownerID == myID) {
                    textPostMenu.text = menuItemsForOwner[position].first
                    Glide.with(this@PostMenuDialogFragment)
                        .load(menuItemsForOwner[position].second).into(iconPostMenu)

                    root.setOnClickListener {
                        when(position) {
                            0 -> if(onItemClickListener != null) onItemClickListener!!(EDIT)
                            1 -> if(onItemClickListener != null) onItemClickListener!!(DELETE)
                        }
                    }
                } else {
                    textPostMenu.text = menuItemsForGuest[position].first
                    Glide.with(this@PostMenuDialogFragment)
                        .load(menuItemsForGuest[position].second).into(iconPostMenu)
                    root.setOnClickListener {
                        when(position) {
                            0 -> if(onItemClickListener != null) onItemClickListener!!(REPORT)
                        }
                    }
                }
            }
        }


        override fun getItemCount(): Int =
            if (ownerID == myID) menuItemsForOwner.size else menuItemsForGuest.size

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {

        val TAG = ""

        private const val POST_POSITION = "post_position"

        fun newInstance(postID: Long, ownerID: String, myID: String, postPosition: Int): PostMenuDialogFragment =
            PostMenuDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(POST_ID, postID)
                    putString(OWNER_ID, ownerID)
                    putString(MY_ID, myID)
                    putInt(POST_POSITION, postPosition)
                }
            }

    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}