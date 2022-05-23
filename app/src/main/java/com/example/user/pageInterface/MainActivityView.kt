package com.example.user.pageInterface

import android.content.Context
import android.content.DialogInterface
import com.example.user.struct.UserItem

interface MainActivityView {

    fun showLoadingProgressDialog()

    fun dismissLoadingProgressDialog()

    fun showAlert(title: String, message: String, positiveCb: (DialogInterface) ->Unit)

    fun dismissAlert()

    fun updateListView(data: ArrayList<UserItem>)
}