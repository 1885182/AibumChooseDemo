package com.sszt.basis.ext


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sszt.basis.weight.Loading

fun FragmentActivity.showLoadingEx() {

        Loading.showLoading(this)
}

fun Fragment.showLoadingEx() {

        Loading.showLoading(this.requireActivity())
}

fun FragmentActivity.dismissLoadingEx(msg: String?) {

        Loading.dismissLoading()
}
