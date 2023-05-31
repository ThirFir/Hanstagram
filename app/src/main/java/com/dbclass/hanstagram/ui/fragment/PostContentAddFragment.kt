package com.dbclass.hanstagram.ui.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.utils.closeKeyboard
import com.dbclass.hanstagram.databinding.FragmentPostContentAddBinding
import com.dbclass.hanstagram.ui.activity.PostAddActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostContentAddFragment : Fragment() {
    private lateinit var binding: FragmentPostContentAddBinding
    private lateinit var imageURIArray: Array<String>
    private val postRepository: PostRepository = PostRepositoryImpl
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostContentAddBinding.inflate(inflater, container, false)

        loadImages()
        (requireActivity() as PostAddActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_post_content_add_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_add_complete_new_post -> {
                val userID = (requireActivity() as PostAddActivity).intent.getStringExtra("user_id")!!

                uiScope.launch {
                    var images = imageURIArray[0]
                    if(imageURIArray[1] != "")
                        images += "," + imageURIArray[1]
                    if(imageURIArray[2] != "")
                        images += "," + imageURIArray[2]
                    if(imageURIArray[3] != "")
                        images += "," + imageURIArray[3]
                    if(imageURIArray[4] != "")
                        images += "," + imageURIArray[4]

                    requireContext().closeKeyboard(binding.editTextContent)
                    requireActivity().setResult(RESULT_OK)
                    PostAddActivity.onEditCompleteListener(userID, images, binding.editTextContent.text.toString())
                    (requireActivity() as PostAddActivity).finish()
                }
                true
            }
            else -> false
        }
    }

    private fun loadImages() {
        imageURIArray = arguments?.getStringArray(IMAGE_ARRAY) ?: arrayOf("", "", "", "", "")
        Glide.with(requireContext()).load(imageURIArray[0]).into(binding.imageDisplay)
    }

    companion object {
        const val IMAGE_ARRAY = "image_array"
    }

}