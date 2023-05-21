package com.dbclass.hanstagram.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentThumbnailsListBinding
import com.dbclass.hanstagram.ui.adapter.ThumbnailsAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ThumbnailsListFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private val postRepository: PostRepository = PostRepositoryImpl
    private lateinit var binding: FragmentThumbnailsListBinding
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)
    private var ownerID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThumbnailsListBinding.inflate(inflater, container, false)

        ownerID = arguments?.getString("owner_id") ?: userViewModel.user.value?.id

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        uiScope.launch {
            val posts = postRepository.getUserPosts(ownerID)
            binding.recyclerviewThumbnails.adapter = ThumbnailsAdapter(posts)
        }
    }
}