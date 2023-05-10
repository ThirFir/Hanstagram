package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.FragmentImageContentBinding


class ImageContentFragment private constructor(): Fragment() {

    companion object {
        fun newInstance(uri: String, height: Int): ImageContentFragment {
            val args = bundleOf("uri" to uri, "height" to height)

            val fragment = ImageContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentImageContentBinding.inflate(inflater, container, false)
        val imageURI = arguments?.getString("uri")
        val imageHeight = arguments?.getInt("height", 600) ?: 600
        Log.d("ImageContentFragment", "생성 : $imageURI")
        binding.imageContent.layoutParams.height = imageHeight
        Glide.with(requireContext()).load(imageURI).error(R.drawable.ic_error_96)
            .into(binding.imageContent)
        return binding.root
    }

}