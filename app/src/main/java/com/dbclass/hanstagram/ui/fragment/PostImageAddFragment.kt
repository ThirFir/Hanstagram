package com.dbclass.hanstagram.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.utils.getImageList
import com.dbclass.hanstagram.databinding.FragmentPostImageAddBinding
import com.dbclass.hanstagram.ui.activity.PostAddActivity


class PostImageAddFragment : Fragment() {

    private lateinit var binding: FragmentPostImageAddBinding
    private lateinit var getSelectedImageActivityResult: ActivityResultLauncher<Intent>

    private val imageURIs = arrayOf("", "", "", "", "")

    var selectedImageTab = 1

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostImageAddBinding.inflate(inflater, container, false)
        setInitialImages()

        with(binding) {

            imageFirst.setOnClickListener {
                selectedImageTab = 0
                when {
                    checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                    -> {
                        runGalleryAppWithResult()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)
                    -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("권한 요청")
                            .setMessage("이미지 선택을 위해 갤러리 접근 권한이 필요합니다")
                            .setPositiveButton("동의") { _, _ ->
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                                    1000
                                )
                            }
                            .setNegativeButton("취소") { _, _ -> }
                            .create()
                            .show()
                    }

                    else -> {
                        Log.d("Permission", "3")
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                            1000
                        )
                    }
                }
                runGalleryAppWithResult()
            }
            imageSecond.setOnClickListener {
                selectedImageTab = 1
                runGalleryAppWithResult()
            }
            imageThird.setOnClickListener {
                selectedImageTab = 2
                runGalleryAppWithResult()
            }
            imageFourth.setOnClickListener {
                selectedImageTab = 3
                runGalleryAppWithResult()
            }
            imageFifth.setOnClickListener {
                selectedImageTab = 4
                runGalleryAppWithResult()
            }
        }

        (requireActivity() as PostAddActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSelectedImageActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
                imageURIs[selectedImageTab] = it.data?.data.toString()
                with(imageURIs[selectedImageTab]) {
                    when (selectedImageTab) {
                        0 ->
                            Glide.with(this@PostImageAddFragment).load(this)
                                .into(binding.imageFirst)
                        1 ->
                            Glide.with(this@PostImageAddFragment).load(this)
                                .into(binding.imageSecond)
                        2 ->
                            Glide.with(this@PostImageAddFragment).load(this)
                                .into(binding.imageThird)
                        3 ->
                            Glide.with(this@PostImageAddFragment).load(this)
                                .into(binding.imageFourth)
                        4 ->
                            Glide.with(this@PostImageAddFragment).load(this)
                                .into(binding.imageFifth)
                    }
                }
            }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_post_image_add_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_next_page -> {
                if (imageURIs[0] != "") {
                    val action =
                        PostImageAddFragmentDirections.actionAddPostImageAddFragmentToAddPostContentAddFragment()
                    action.arguments.run {
                        putStringArray(PostContentAddFragment.IMAGE_ARRAY, imageURIs)
                    }
                    (requireActivity() as PostAddActivity).findNavController(R.id.nav_host_fragment_post)
                        .navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.toast_choose_image,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }

            else -> false
        }
    }

    private fun runGalleryAppWithResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        getSelectedImageActivityResult.launch(intent)

    }

    private fun setInitialImages() {
        val images = (requireActivity() as PostAddActivity).intent.getStringExtra("images")
        images?.let {
            val imageList = getImageList(images)
            for (i in imageList.indices){
                imageURIs[i] = imageList[i]
                when(i) {
                    0 -> Glide.with(this).load(imageURIs[i]).into(binding.imageFirst)
                    1 -> Glide.with(this).load(imageURIs[i]).into(binding.imageSecond)
                    2 -> Glide.with(this).load(imageURIs[i]).into(binding.imageThird)
                    3 -> Glide.with(this).load(imageURIs[i]).into(binding.imageFourth)
                    4 -> Glide.with(this).load(imageURIs[i]).into(binding.imageFifth)
                }
            }
        }
    }
}
