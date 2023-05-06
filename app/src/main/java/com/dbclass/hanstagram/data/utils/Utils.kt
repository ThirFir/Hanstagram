package com.dbclass.hanstagram.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.net.toUri
import com.dbclass.hanstagram.ui.activity.PostCommentActivity

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

fun Context.getImageHeightWithWidthFully(image: String, rootWidth: Int = 0): Int {
    val imageOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            contentResolver.openInputStream(image.toUri()),
            null,
            this
        )
    }

    val imageWidth = imageOptions.outWidth
    val imageHeight = imageOptions.outHeight
    val displayMetrics = resources.displayMetrics
    val screenWidth = if(rootWidth == 0) displayMetrics.widthPixels else rootWidth
    val scaleFactor = screenWidth.toFloat() / imageWidth

    return (imageHeight * scaleFactor).toInt()
}
