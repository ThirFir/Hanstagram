package com.dbclass.hanstagram.ui.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbclass.hanstagram.ui.fragment.GuestBookFragment
import com.dbclass.hanstagram.ui.fragment.ThumbnailsListFragment

class ProfileViewPagerFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments: List<Fragment> by lazy {
        listOf(ThumbnailsListFragment.newInstance(ownerID), GuestBookFragment.newInstance(ownerID))
    }
    private lateinit var ownerID: String
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setProfileOwnerID(ownerID: String){
        this.ownerID = ownerID
    }
}