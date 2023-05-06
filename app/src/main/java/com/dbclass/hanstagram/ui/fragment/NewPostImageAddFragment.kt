package com.dbclass.hanstagram.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.dbclass.hanstagram.databinding.FragmentNewPostImageAddBinding
import com.dbclass.hanstagram.ui.activity.NewPostActivity


class NewPostImageAddFragment : Fragment() {

    private lateinit var binding: FragmentNewPostImageAddBinding
    private lateinit var activityImageResult: ActivityResultLauncher<Intent>
    private var firstURI: String = ""
    private var secondURI: String = ""
    private var thirdURI: String = ""
    private var fourthURI: String = ""
    private var fifthURI: String = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                        firstURI = it.data?.data.toString()
                        Glide.with(this).load(firstURI).into(binding.imageFirst)
                    }
                    2 -> {
                        secondURI = it.data?.data.toString()
                        Glide.with(this).load(secondURI).into(binding.imageSecond)
                    }
                    3 -> {
                        thirdURI = it.data?.data.toString()
                        Glide.with(this).load(thirdURI).into(binding.imageThird)
                    }
                    4 -> {
                        fourthURI = it.data?.data.toString()
                        Glide.with(this).load(fourthURI).into(binding.imageFourth)
                    }
                    5 -> {
                        fifthURI = it.data?.data.toString()
                        Glide.with(this).load(fifthURI).into(binding.imageFifth)
                    }
                }
            }

        with(binding) {

            imageFirst.setOnClickListener {
                selectedImageTab = 1
                when {
                    checkSelfPermission(requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                    -> {
                        Log.d("Permission","1")
                        runGalleryAppWithResult()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)
                    -> {
                        Log.d("Permission","2")
                        AlertDialog.Builder(requireContext())
                            .setTitle("권한 요청")
                            .setMessage("이미지 선택을 위해 갤러리 접근 권한이 필요합니다")
                            .setPositiveButton("동의하기") { _, _ ->
                                ActivityCompat.requestPermissions(requireActivity(),
                                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                                    1000
                                )
                            }
                            .setNegativeButton("취소하기") { _, _ -> }
                            .create()
                            .show()
                    }
                    else -> {
                        Log.d("Permission","3")
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1000)
                    }
                }
                runGalleryAppWithResult()
            }
            imageSecond.setOnClickListener {
                selectedImageTab = 2
                runGalleryAppWithResult()
            }
            imageThird.setOnClickListener {
                selectedImageTab = 3
                runGalleryAppWithResult()
            }
            imageFourth.setOnClickListener {
                selectedImageTab = 4
                runGalleryAppWithResult()
            }
            imageFifth.setOnClickListener {
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
                if(firstURI != "") {
                    val action = NewPostImageAddFragmentDirections.actionNewPostImageAddFragmentToNewPostContentAddFragment()
                    action.arguments.run {
                        putString("thumbnail_uri", firstURI)
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
        intent.action = Intent.ACTION_PICK
        activityImageResult.launch(intent)

    }
}
