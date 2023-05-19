package com.dbclass.hanstagram.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.dbclass.hanstagram.ui.fragment.PostsPageFragment
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.ActivityMainBinding
import com.dbclass.hanstagram.ui.fragment.FindPageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()
    private val userRepository: UserRepository = UserRepositoryImpl
    private lateinit var postsPageFragment: PostsPageFragment
    private lateinit var followersPostsPageFragment: PostsPageFragment
    private lateinit var findPageFragment: FindPageFragment
    private lateinit var profilePageFragment: ProfilePageFragment
    private var prevSelectedItem: Int = 0
    private var currentSelectedItem: Int = 0

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                binding.bottomNavigationView.menu.findItem(prevSelectedItem)?.isChecked = true
            }
            else finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        postsPageFragment = PostsPageFragment.newInstance(PostsPageFragment.ALL)
        followersPostsPageFragment = PostsPageFragment.newInstance(PostsPageFragment.FOLLOW)
        findPageFragment = FindPageFragment.newInstance()
        // profilePageFragment = ProfilePageFragment.newInstance(userViewModel.user.value?.id)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        CoroutineScope(Dispatchers.Default).launch {
            val userID = intent.getStringExtra("user_id")
            var user: UserEntity? = null
            if(userID != null)
                user = userRepository.getUser(userID)
            CoroutineScope(Dispatchers.Main).launch {
                user?.let { userViewModel.setUser(it) }
                setContentView(binding.root)
                setBottomNavigationOperation()

                binding.mainToolbar.setTitle(R.string.special_app_name)
                setSupportActionBar(binding.mainToolbar)
                supportFragmentManager.beginTransaction().add(R.id.fragment_content, postsPageFragment).commit()
            }
        }
    }

    private fun setBottomNavigationOperation() {
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                Log.d("Prev", prevSelectedItem.toString())
                Log.d("Curr", currentSelectedItem.toString())
                prevSelectedItem = currentSelectedItem
                when(item.itemId) {
                    R.id.item_posts -> {
                        currentSelectedItem = R.id.item_posts
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content, postsPageFragment).addToBackStack(null).commit()
                        true
                    }
                    R.id.item_followers_posts -> {
                        currentSelectedItem = R.id.item_followers_posts
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content, followersPostsPageFragment).addToBackStack(null).commit()
                        true
                    }
                    R.id.item_find -> {
                        currentSelectedItem = R.id.item_find
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content, findPageFragment).addToBackStack(null).commit()
                        true
                    }
                    R.id.item_profile -> {
                        currentSelectedItem = R.id.item_profile
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content, ProfilePageFragment.newInstance(userViewModel.user.value?.id)).addToBackStack(null).commit()
                        true
                    }
                    else -> {
                        currentSelectedItem = R.id.item_posts
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content, postsPageFragment).addToBackStack(null).commit()
                        true
                    }
                }
            }
        }
    }
}