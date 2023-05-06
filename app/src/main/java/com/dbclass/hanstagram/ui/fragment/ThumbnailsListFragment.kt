package com.dbclass.hanstagram.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentThumbnailsListBinding
import com.dbclass.hanstagram.ui.adapter.ThumbnailsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ThumbnailsListFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentThumbnailsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThumbnailsListBinding.inflate(inflater, container, false)

        val spanCount = 3
        binding.recyclerviewThumbnails.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerviewThumbnails.addItemDecoration(
            object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom = 1
                    if(parent.getChildAdapterPosition(view) % spanCount != spanCount - 1)
                        outRect.right = 1
                }
            }
        )
        CoroutineScope(Dispatchers.Default).launch {
            val posts = PostRepository.getUserPosts(userViewModel.user.value?.id)
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewThumbnails.adapter = ThumbnailsAdapter(posts)
            }
        }

        return binding.root
    }
}