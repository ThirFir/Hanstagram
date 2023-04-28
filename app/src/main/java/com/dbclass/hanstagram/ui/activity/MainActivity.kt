package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dbclass.hanstagram.ui.fragment.PostsPageFragment
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        supportFragmentManager.beginTransaction().add(R.id.fragment_content, PostsPageFragment()).commit()
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_posts -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, PostsPageFragment()).commit()
                        true
                    }
                    R.id.item_profile -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, ProfilePageFragment()).commit()
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
}