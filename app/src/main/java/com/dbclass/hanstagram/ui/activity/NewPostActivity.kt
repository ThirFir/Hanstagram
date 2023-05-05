package com.dbclass.hanstagram.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ActivityNewPostBinding
import com.dbclass.hanstagram.ui.fragment.NewPostImageAddFragmentDirections

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            newPostToolbar.setTitle(R.string.special_app_name)
            setSupportActionBar(newPostToolbar)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)/*
        binding.imageviewSecond.layoutParams.height = binding.imageviewSecond.width
        binding.imageviewThird.layoutParams.height = binding.imageviewThird.width
        binding.imageviewFourth.layoutParams.height = binding.imageviewFourth.width
        binding.imageviewFifth.layoutParams.height = binding.imageviewFifth.width*/
    }

    private var backKeyPressedTime = 0L
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한 번 더 누르면 작성을 종료합니다", Toast.LENGTH_SHORT).show()
        } else{
            finish()
        }
    }

}