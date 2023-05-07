package com.dbclass.hanstagram.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private var imageURI: String = ""
    private var userID: String = ""
    private var initialNickname: String = ""
    private var initialCaption: String = ""
    private lateinit var activityImageResult: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(intent) {
            userID = getStringExtra("user_id") ?: ""
            initialNickname = getStringExtra("nickname") ?: ""
            initialCaption = getStringExtra("caption") ?: ""
            imageURI = getStringExtra("image_uri") ?: ""
        }

        activityImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult

                imageURI = it.data?.data.toString()
                Glide.with(this@ProfileEditActivity).load(imageURI)
                    .error(R.drawable.ic_account_96).into(binding.imageProfile)
            }

        with(binding) {

            if (initialNickname != "")
                editTextNickname.setText(initialNickname)
            if (initialCaption != "")
                editTextCaption.setText(initialCaption)
            Glide.with(this@ProfileEditActivity).load(imageURI)
                .error(R.drawable.ic_account_96).into(imageProfile)

            profileEditToolbar.setTitle(R.string.special_app_name)
            setSupportActionBar(profileEditToolbar)

            imageProfile.setOnClickListener {
                when {
                    checkSelfPermission(
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                    -> {
                        Log.d("Permission","1")
                        runGalleryAppWithResult()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)
                    -> {
                        Log.d("Permission","2")
                        AlertDialog.Builder(this@ProfileEditActivity)
                            .setTitle("권한이 필요합니다.")
                            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
                            .setPositiveButton("동의하기") { _, _ ->
                                requestPermissions(
                                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                                    1000
                                )
                            }
                            .setNegativeButton("취소하기") { _, _ -> }
                            .create()
                            .show()
                    }
                    else -> {
                        Log.d("Permission","3")
                        requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1000)
                    }
                }
            }

            textButtonWithdrawal.setOnClickListener {
                AlertDialog.Builder(this@ProfileEditActivity)
                    .setTitle(getString(R.string.text_user_withdrawal))
                    .setMessage(getString(R.string.alert_withdrawal))
                    .setPositiveButton(getString(R.string.text_ok)) { _, _ ->
                        val intent =
                            Intent(this@ProfileEditActivity, LoginActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            }
                        Toast.makeText(this@ProfileEditActivity, getString(R.string.toast_success_withdrawal), Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        UserRepository.deleteUser(userID)
                    }
                    .setNegativeButton(getString(R.string.text_cancel)) { _, _ -> }
                    .create().show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    runGalleryAppWithResult()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_edit_app_bar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // edit complete
            R.id.icon_set_profile -> {
                if (binding.editTextNickname.text.isEmpty()) {
                    Toast.makeText(this, R.string.toast_nickname_blank, Toast.LENGTH_SHORT).show()
                    return true
                }
                val editedNickname = binding.editTextNickname.text.toString()
                val editedCaption = binding.editTextCaption.text.toString()
                UserRepository.run {
                    updateNickname(id = userID, nickname = editedNickname)
                    updateCaption(id = userID, caption = editedCaption)
                    updateProfileImage(userID, imageURI)
                }
                Toast.makeText(this, R.string.toast_edit_profile, Toast.LENGTH_SHORT).show()

                val returnIntent = Intent(this, MainActivity::class.java).apply {
                    putExtra("image_uri", imageURI)
                    putExtra("nickname", editedNickname)
                    putExtra("caption", editedCaption)
                }
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun runGalleryAppWithResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        activityImageResult.launch(intent)
    }


}