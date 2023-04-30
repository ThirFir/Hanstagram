package com.dbclass.hanstagram.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.FragmentNewPostImageAddBinding
import com.dbclass.hanstagram.ui.activity.NewPostActivity


class NewPostImageAddFragment : Fragment() {

    private lateinit var binding: FragmentNewPostImageAddBinding
    private lateinit var activityImageResult: ActivityResultLauncher<Intent>
    private var thumbnailURI: String = ""
    private var secondURI: String = ""
    private var thirdURI: String = ""
    private var fourthURI: String = ""
    private var fifthURI: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostImageAddBinding.inflate(inflater, container, false)

        var selectedImageTab = 1
        activityImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
                when (selectedImageTab) {
                    1 -> {
                        thumbnailURI = it.data?.data.toString()
                        Glide.with(this).load(thumbnailURI).into(binding.imageviewThumbnail)
                    }
                    2 -> {
                        secondURI = it.data?.data.toString()
                        Glide.with(this).load(secondURI).into(binding.imageviewSecond)
                    }
                    3 -> {
                        thirdURI = it.data?.data.toString()
                        Glide.with(this).load(thirdURI).into(binding.imageviewThird)
                    }
                    4 -> {
                        fourthURI = it.data?.data.toString()
                        Glide.with(this).load(fourthURI).into(binding.imageviewFourth)
                    }
                    5 -> {
                        fifthURI = it.data?.data.toString()
                        Glide.with(this).load(fifthURI).into(binding.imageviewFifth)
                    }
                }
            }

        with(binding) {

            imageviewThumbnail.setOnClickListener {
                selectedImageTab = 1
                runGalleryAppWithResult()
            }
            imageviewSecond.setOnClickListener {
                selectedImageTab = 2
                runGalleryAppWithResult()
            }
            imageviewThird.setOnClickListener {
                selectedImageTab = 3
                runGalleryAppWithResult()
            }
            imageviewFourth.setOnClickListener {
                selectedImageTab = 4
                runGalleryAppWithResult()
            }
            imageviewFifth.setOnClickListener {
                selectedImageTab = 5
                runGalleryAppWithResult()
            }

        }

        (requireActivity() as NewPostActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_post_image_add_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_next_page -> {
                if(thumbnailURI != "") {
                    val action = NewPostImageAddFragmentDirections.actionNewPostImageAddFragmentToNewPostContentAddFragment()
                    action.arguments.run {
                        putString("thumbnail_uri", thumbnailURI)
                        putString("second_uri", secondURI)
                        putString("third_uri", thirdURI)
                        putString("fourth_uri", fourthURI)
                        putString("fifth_uri", fifthURI)
                    }
                    (requireActivity() as NewPostActivity).findNavController(R.id.nav_host_fragment_new_post)
                        .navigate(action)
                } else {
                    Toast.makeText(requireContext(), R.string.toast_choose_image, Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> false
        }
    }

    private fun runGalleryAppWithResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityImageResult.launch(intent)
    }
}
