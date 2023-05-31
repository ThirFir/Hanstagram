package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        var selectedDepartment = ""
        binding.editTextDepartment.setOnClickListener {
            AlertDialog.Builder(this@RegisterActivity)
                .setTitle("학부")
                .setSingleChoiceItems(R.array.departments, 0) { _, which ->
                    selectedDepartment = resources.getStringArray(R.array.departments)[which]
                }
                .setPositiveButton("확인") { _, _ ->
                    binding.editTextDepartment.setText(selectedDepartment)
                }.setNegativeButton("취소") { _, _ ->
                    selectedDepartment = ""
                }
                .show()
        }

        binding.buttonRegister.setOnClickListener {
            val id = binding.editTextNewId.text
            val password = binding.editTextNewPassword.text
            val email = binding.editTextEmail.text
            val studentID = binding.editTextStudentId.text
            val department = binding.editTextDepartment.text

            if (id.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_id, Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_email, Toast.LENGTH_SHORT).show()
            } else if (studentID.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_student_id, Toast.LENGTH_SHORT).show()
            } else if (department.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_department, Toast.LENGTH_SHORT).show()
            }
            else {
                // 계정 생성
                val user = UserEntity(
                    id = id.toString(),
                    password = password.toString(),
                    nickname = id.toString(),
                    studentID = studentID.toString(),
                    createdTime = System.currentTimeMillis(),
                    email = email.toString(),
                    department = department.toString()
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