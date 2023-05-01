package com.dbclass.hanstagram.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.dbclass.hanstagram.ui.fragment.PostsPageFragment
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationOperation()

        CoroutineScope(Dispatchers.Default).launch {
            val userID = intent.getStringExtra("user_id")
            var user: UserEntity? = null
            if(userID != null)
                user = UserRepository.getUser(userID)
            CoroutineScope(Dispatchers.Main).launch {
                user?.let { userViewModel.setUser(it) }
            }
        }

        binding.mainToolbar.setTitle(R.string.special_app_name)
        setSupportActionBar(binding.mainToolbar)
        supportFragmentManager.beginTransaction().add(R.id.fragment_content, PostsPageFragment()).commit()
    }

    private fun setBottomNavigationOperation() {
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_posts -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, PostsPageFragment()).commit()
                        true
                    }
                    R.id.item_profile -> {
                        val profilePageFragment = ProfilePageFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, profilePageFragment).commit()
                        true
                    }
                    else -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, PostsPageFragment()).commit()
                        true
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_frag_app_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.icon_add_new_post -> {
                val intent = Intent(this, NewPostActivity::class.java).apply {
                    putExtra("user_id", userViewModel.user.value?.id)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}