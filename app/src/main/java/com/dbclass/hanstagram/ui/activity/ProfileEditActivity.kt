package com.dbclass.hanstagram.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(intent){
            userID = getStringExtra("user_id") ?: ""
            initialNickname = getStringExtra("nickname") ?: ""
            initialCaption = getStringExtra("caption") ?: ""
            imageURI = getStringExtra("image_uri") ?: ""
        }

        Log.d("ProfileEditActivity", "userID : $userID, nickname : $initialNickname, caption : $initialCaption")

        activityImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
                imageURI = it.data?.data.toString()
                Glide.with(this@ProfileEditActivity).load(imageURI).error(R.drawable.baseline_account_circle_24).into(binding.imageProfile)
            }

        with(binding) {

            if (initialNickname != "")
                editTextNickname.setText(initialNickname)
            if (initialCaption != "")
                editTextCaption.setText(initialCaption)
            Glide.with(this@ProfileEditActivity).load(imageURI).error(R.drawable.baseline_account_circle_24).into(imageProfile)

            profileEditToolbar.setTitle(R.string.special_app_name)
            setSupportActionBar(profileEditToolbar)

            imageProfile.setOnClickListener {
                runGalleryAppWithResult()
                Glide.with(this@ProfileEditActivity).load(imageURI).error(R.drawable.baseline_account_circle_24).into(imageProfile)
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
                if(binding.editTextNickname.text.isEmpty()){
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
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        activityImageResult.launch(intent)
    }
}