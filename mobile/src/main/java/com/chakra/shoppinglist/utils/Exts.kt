package com.chakra.shoppinglist.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.chakra.shoppinglist.base.BaseFragment


fun BaseFragment.closeKeyBoard() {
    val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
}

fun BaseFragment.openKeyBoard() {
    val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}