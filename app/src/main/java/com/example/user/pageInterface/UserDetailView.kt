package com.example.user.pageInterface

import android.content.DialogInterface

interface UserDetailView {

    fun showLoadingProgressDialog()

    fun dismissLoadingProgressDialog()

    fun showAlert(title: String, message: String, positiveCb: (DialogInterface) ->Unit)

    fun dismissAlert()
}