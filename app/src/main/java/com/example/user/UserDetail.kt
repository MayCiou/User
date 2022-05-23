package com.example.user

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.user.adapter.RecyclerViewAdapter
import com.example.user.pageInterface.UserDetailView
import com.example.user.pagePresenter.UserDetailPresenter
import com.example.user.server.RetrofitHttp
import com.example.user.utils.LoadingProgressDialog
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.alert

class UserDetail: AppCompatActivity(), UserDetailView, View.OnClickListener {

    private lateinit var ivBack : ImageView
    private lateinit var circleImageViewUserDetail : CircleImageView
    private lateinit var tvName : TextView
    private lateinit var ivBio : ImageView
    private lateinit var tvLogin : TextView
    private lateinit var tvSiteAdmin : TextView
    private lateinit var tvLocation : TextView
    private lateinit var tvBlog : TextView

    private var mainActivityPresenter : UserDetailPresenter? = null
    private var loadingProgressDialog : LoadingProgressDialog? = null
    private var adapter : RecyclerViewAdapter? = null
    private var alertDialog: AlertDialog? = null
    private var service : RetrofitHttp? = null

    private lateinit var activity : Activity
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_user_detail)

        activity = this
        ivBack = findViewById(R.id.ivBack)
        circleImageViewUserDetail = findViewById(R.id.circleImageViewUserDetail)
        tvName = findViewById(R.id.tvName)
        ivBio = findViewById(R.id.ivBio)
        tvLogin = findViewById(R.id.tvLogin)
        tvSiteAdmin = findViewById(R.id.tvSiteAdmin)
        tvLocation = findViewById(R.id.tvLocation)
        tvBlog = findViewById(R.id.tvBlog)

        service = RetrofitHttp()

        mainActivityPresenter = UserDetailPresenter(activity)
        mainActivityPresenter!!.setView(this)
        mainActivityPresenter!!.setLogin(intent)

        loadingProgressDialog = LoadingProgressDialog(activity)
        loadingProgressDialog!!.create()

        ivBack.setOnClickListener(this)
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

    override fun loadCircleImage(url: String) {

        mainActivityPresenter?.loadCircleImage(circleImageViewUserDetail, url)
    }

    override fun setCircleImageResource(resource: Int) {

        circleImageViewUserDetail.setImageResource(resource)
    }

    override fun setNameText(value: String) {
        tvName.text = value
    }

    override fun loadBioImage(url: String) {

        mainActivityPresenter?.loadBioImage(ivBio, url)
    }

    override fun setBioImageResource(resource: Int) {
        ivBio.setImageResource(resource)
    }

    override fun setLoginText(value: String) {
        tvLogin.text = value
    }

    override fun setSiteAdminVisibility(visibility: Int) {
        tvSiteAdmin.visibility = visibility
    }

    override fun setLocationText(value: String) {
        tvLocation.text = value
    }

    override fun setBlogText(value: String) {
        tvBlog.text = value
    }

    override fun onClick(view: View) {

        when(view.id){

            R.id.ivBack->
            {
                mainActivityPresenter?.backOnClick(activity)
            }
        }
    }

}