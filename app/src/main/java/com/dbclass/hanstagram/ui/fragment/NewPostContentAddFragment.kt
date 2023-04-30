package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.PostRepository
import com.dbclass.hanstagram.databinding.FragmentNewPostContentAddBinding
import com.dbclass.hanstagram.ui.activity.NewPostActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NewPostContentAddFragment : Fragment() {
    private lateinit var binding: FragmentNewPostContentAddBinding
    private var thumbnailURI: String = ""
    private var secondURI: String = ""
    private var thirdURI: String = ""
    private var fourthURI: String = ""
    private var fifthURI: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostContentAddBinding.inflate(inflater, container, false)

        loadImages()
        (requireActivity() as NewPostActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_post_content_add_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_add_complete_new_post -> {
                val userID = (requireActivity() as NewPostActivity).intent.getStringExtra("user_id")
                CoroutineScope(Dispatchers.Default).launch {
                    if (userID != null)
                        PostRepository.addPost(
                            PostEntity(
                                userID = userID,
                                content = binding.editTextContent.text.toString(),
                                images = "$thumbnailURI,$secondURI,$thirdURI,$fourthURI,$fifthURI",
                                createdTime = System.currentTimeMillis()
                            )
                        )
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(requireContext(), "게시글이 작성되었습니다", Toast.LENGTH_SHORT).show()
                        (requireActivity() as NewPostActivity).finish()
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun loadImages() {
        thumbnailURI = arguments?.getString("thumbnail_uri") ?: ""
        secondURI = arguments?.getString("second_uri") ?: ""
        thirdURI = arguments?.getString("third_uri") ?: ""
        fourthURI = arguments?.getString("fourth_uri") ?: ""
        fifthURI = arguments?.getString("fifth_uri") ?: ""
        Glide.with(requireContext()).load(thumbnailURI).into(binding.imageviewThumbnail)
    }
}