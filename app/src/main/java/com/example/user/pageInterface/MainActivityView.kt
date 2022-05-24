package com.example.user.pageInterface

import android.content.DialogInterface
import com.example.user.struct.StructUserItem

interface MainActivityView {

    fun showLoadingProgressDialog()

    fun dismissLoadingProgressDialog()

    fun showAlert(title: String, message: String, positiveCb: (DialogInterface) ->Unit)

    fun dismissAlert()

    fun updateListView(data: ArrayList<StructUserItem?>)

    fun setItemTotal(value: Int)

    fun addLoadingView()

    fun removeLoadingView()

    fun setLoaded()
}