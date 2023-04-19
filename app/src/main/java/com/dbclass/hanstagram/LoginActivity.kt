package com.dbclass.hanstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.databinding.ActivityLoginBinding
import com.dbclass.hanstagram.db.HanstagramDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val id = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (id.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_id, Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show()
            } else {
                // 로그인 시도 (내부 DB)
                CoroutineScope(Dispatchers.Default).launch {
                    val db = HanstagramDatabase.getInstance(this@LoginActivity)
                    val user = db?.usersDao()?.getUser(id)
                    if (user == null) {
                        //Toast.makeText(this@LoginActivity, R.string.toast_no_exist_id, Toast.LENGTH_SHORT).show()
                    } else {
                        if(id == user.id && password == user.password) { // Login Success
                            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            //Toast.makeText(this@LoginActivity, R.string.toast_wrong_account, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.textRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }
}