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

    private lateinit var binding: FragmentImageContentBinding
    private var imageURI: String? = null
    private var imageHeight: Int = 600

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
        binding = FragmentImageContentBinding.inflate(inflater, container, false)

        imageURI = arguments?.getString("uri")
        imageHeight = arguments?.getInt("height", 600) ?: 600
        binding.imageContent.layoutParams.height = imageHeight

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(imageURI).error(R.drawable.ic_error_96)
            .into(binding.imageContent)
    }

}