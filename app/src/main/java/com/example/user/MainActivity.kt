package com.example.user


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user.adapter.RecyclerViewAdapter

import com.example.user.pageInterface.MainActivityView
import com.example.user.pagePresenter.MainActivityPresenter
import com.example.user.server.RetrofitHttp
import com.example.user.struct.StructUserItem
import com.example.user.utils.LoadingProgressDialog
import org.jetbrains.anko.alert


class MainActivity : AppCompatActivity() , MainActivityView{

    private lateinit var recyclerViewUserList : RecyclerView

    private var mainActivityPresenter : MainActivityPresenter? = null
    private var loadingProgressDialog : LoadingProgressDialog? = null
    private var adapter : RecyclerViewAdapter? = null
    private var alertDialog: AlertDialog? = null
    private var service : RetrofitHttp? = null
    private lateinit var activity : Activity
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this

        recyclerViewUserList = findViewById(R.id.recyclerViewUserList)

        service = RetrofitHttp()

        mainActivityPresenter = MainActivityPresenter(activity)
        mainActivityPresenter!!.setView(this)

        loadingProgressDialog = LoadingProgressDialog(activity)
        loadingProgressDialog!!.create()

        recyclerViewUserList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter = RecyclerViewAdapter(activity, object :
            RecyclerViewAdapter.RecyclerItemClickListener{
            override fun onItemClick(view: View, position: Int) {

                mainActivityPresenter?.onItemClick(activity, view, position, adapter!!.getData())

            }

        })
        recyclerViewUserList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        mainActivityPresenter!!.getUserList(service!!)
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

        alertDialog = activity.alert(message,title){

            isCancelable = false
            positiveButton(activity.getString(R.string.confirm),positiveCb)

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

    @SuppressLint("NotifyDataSetChanged")
    override fun updateListView(data: ArrayList<StructUserItem>) {

        adapter?.run {

            setData(data)
            notifyDataSetChanged()
        }

    }

}