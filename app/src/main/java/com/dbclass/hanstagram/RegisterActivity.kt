package com.dbclass.hanstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.databinding.ActivityRegisterBinding
import com.dbclass.hanstagram.db.HanstagramDatabase
import com.dbclass.hanstagram.db.users.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val id = binding.editTextNewId.text
            val password = binding.editTextNewPassword.text
            val email = binding.editTextEmail.text
            val studentID = binding.editTextStudentId.text

            if (id.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_id, Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show()
            } else if (studentID.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_student_id, Toast.LENGTH_SHORT).show()
            } else {
                // 계정 생성
                CoroutineScope(Dispatchers.Default).launch {
                    val db = HanstagramDatabase.getInstance(this@RegisterActivity)
                    db?.usersDao()?.createUser(
                        UserEntity(
                            id = id.toString(),
                            password = password.toString(),
                            nickname = id.toString(),
                            studentID = studentID.toString(),
                            timestamp = System.currentTimeMillis(),
                            email = email.toString(),
                        )
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            this@RegisterActivity,
                            R.string.toast_create_account,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            }
        }
    }
}