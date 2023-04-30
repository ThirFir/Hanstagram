package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dbclass.hanstagram.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}