package com.dbclass.hanstagram.data.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Context.closeKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}