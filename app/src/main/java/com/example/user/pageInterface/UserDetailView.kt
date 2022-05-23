package com.example.user.pageInterface

import android.content.DialogInterface

interface UserDetailView {

    fun showLoadingProgressDialog()

    fun dismissLoadingProgressDialog()

    fun showAlert(title: String, message: String, positiveCb: (DialogInterface) ->Unit)

    fun dismissAlert()

    fun loadCircleImage(url: String)

    fun setCircleImageResource(resource: Int)

    fun setNameText(value: String)

    fun loadBioImage(url: String)

    fun setBioImageResource(resource: Int)

    fun setLoginText(value: String)

    fun setSiteAdminVisibility(visibility: Int)

    fun setLocationText(value: String)

    fun setBlogText(value: String)
}