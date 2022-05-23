package com.example.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.user.adapter.RecyclerViewAdapter
import com.example.user.pageInterface.UserDetailView
import com.example.user.pagePresenter.UserDetailPresenter
import com.example.user.server.RetrofitHttp
import com.example.user.utils.LoadingProgressDialog
import org.jetbrains.anko.alert

class UserDetail: AppCompatActivity(), UserDetailView {

    private var mainActivityPresenter : UserDetailPresenter? = null
    private var loadingProgressDialog : LoadingProgressDialog? = null
    private var adapter : RecyclerViewAdapter? = null
    private var alertDialog: AlertDialog? = null
    private var service : RetrofitHttp? = null
    private lateinit var context : Context
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_user_detail)

        context = this


        service = RetrofitHttp()

        mainActivityPresenter = UserDetailPresenter(context)
        mainActivityPresenter!!.setView(this)

        loadingProgressDialog = LoadingProgressDialog(context)
        loadingProgressDialog!!.create()

    }

    override fun onResume() {
        super.onResume()

        mainActivityPresenter!!.getUserDetail(service!!)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStop() {
        super.onStop()

        adapter?.run {

            clearData()
            notifyDataSetChanged()

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        service?.destroy()
        dismissLoadingProgressDialog()
        dismissAlert()
        adapter = null
        service = null
        mainActivityPresenter = null
        loadingProgressDialog = null

    }

    override fun showLoadingProgressDialog() {
        loadingProgressDialog?.show()
    }

    override fun dismissLoadingProgressDialog() {
        loadingProgressDialog?.dismiss()
    }

    override fun showAlert(
        title: String,
        message: String,
        positiveCb: (DialogInterface) -> Unit
    ) {

        if(alertDialog != null)
        {
            dismissAlert()
        }

        alertDialog = context.alert(message,title){

            isCancelable = false
            positiveButton(context.getString(R.string.confirm),positiveCb)

        }.show()
    }

    override fun dismissAlert() {

        alertDialog?.takeIf {
            it.isShowing
        }?.apply {

            dismiss()
            cancel()
        }
    }

}