package com.dbclass.hanstagram.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.net.toUri
import com.dbclass.hanstagram.ui.activity.PostCommentActivity
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.*

fun Context.closeKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Context.startPostCommentActivity(userID: String, postID: Long) {
    this.startActivity(Intent(this, PostCommentActivity::class.java).apply {
        putExtra("user_id", userID)
        putExtra("post_id", postID)
    })
}

fun Context.showKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.getImageHeightWithWidthFully(image: String, rootWidth: Int = 0): Int {
    val imageOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        try {
            BitmapFactory.decodeStream(
                contentResolver.openInputStream(image.toUri()),
                null,
                this
            )
        } catch (e: Exception) {
            Toast.makeText(this@getImageHeightWithWidthFully, "이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            Log.d("Utils", e.toString())
        }
    }

    val imageWidth = imageOptions.outWidth
    val imageHeight = imageOptions.outHeight
    val displayMetrics = resources.displayMetrics
    val screenWidth = if(rootWidth == 0) displayMetrics.widthPixels else rootWidth
    val scaleFactor = screenWidth.toFloat() / imageWidth

    return (imageHeight * scaleFactor).toInt()
}

fun getFormattedDate(time: Long): String =
    SimpleDateFormat("yyyy-MM-dd kk:mm", Locale("ko", "KR")).format(Date(time))

fun getImageList(imageURIs: String): List<String> {
    return imageURIs.split(",")
}