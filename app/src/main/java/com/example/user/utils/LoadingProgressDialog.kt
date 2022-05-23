package com.example.user.utils

import android.app.Dialog
import android.content.Context
import com.example.user.R


class LoadingProgressDialog(private val context: Context) {

    private var dialog : Dialog? = null

    fun create(){

        dialog = Dialog(context, R.style.customProgressDialog)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.view_progress)

    }

    fun show(){

        dialog?.show()
    }

    fun dismiss(){

        dialog?.takeIf {
            it.isShowing
        }?.apply {

            dismiss()
            cancel()
        }
    }
}