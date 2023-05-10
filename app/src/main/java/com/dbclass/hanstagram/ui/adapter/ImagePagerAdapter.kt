package com.dbclass.hanstagram.ui.adapter

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbclass.hanstagram.ui.fragment.ImageContentFragment

class ImagePagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val imageFragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = imageFragments.size

    override fun createFragment(position: Int): Fragment = imageFragments[position]

    fun setImages(imageURIs: List<String>, baseHeight: Int) {
        for(uri in imageURIs)
            imageFragments.add(ImageContentFragment.newInstance(uri, baseHeight))
    }
}