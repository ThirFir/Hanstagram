package com.dbclass.hanstagram.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbclass.hanstagram.ui.fragment.GuestBookFragment
import com.dbclass.hanstagram.ui.fragment.UserPostsListFragment

class ProfileViewPagerFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(UserPostsListFragment(), GuestBookFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}