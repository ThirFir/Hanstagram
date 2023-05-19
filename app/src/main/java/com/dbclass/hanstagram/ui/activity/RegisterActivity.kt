package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.databinding.ActivityRegisterBinding
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val userRepository: UserRepository = UserRepositoryImpl
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

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
            } else if (email.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_email, Toast.LENGTH_SHORT).show()
            } else if (studentID.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_student_id, Toast.LENGTH_SHORT).show()
            } else {
                // 계정 생성
                val user = UserEntity(
                    id = id.toString(),
                    password = password.toString(),
                    nickname = id.toString(),
                    studentID = studentID.toString(),
                    createdTime = System.currentTimeMillis(),
                    email = email.toString()
                )
                uiScope.launch {
                    userRepository.createUser(user)
                    Toast.makeText(
                        this@RegisterActivity,
                        R.string.toast_create_account,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}