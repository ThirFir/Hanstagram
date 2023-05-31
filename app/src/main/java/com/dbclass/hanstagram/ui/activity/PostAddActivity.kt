package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ActivityAddPostBinding

class PostAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding

    private var backPressedTime = 0L
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@PostAddActivity, "한 번 더 누르면 작성을 종료합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        lateinit var onEditCompleteListener: (String, String, String) -> Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        with(binding){
            newPostToolbar.setTitle(R.string.special_app_name)
            setSupportActionBar(newPostToolbar)
        }
    }

    fun setOnEditCompleteListener(listener: (String, String, String) -> Unit) {
        onEditCompleteListener = listener
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
    }
}