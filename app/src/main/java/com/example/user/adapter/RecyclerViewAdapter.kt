package com.example.user.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.user.R
import com.example.user.struct.StructUserItem
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerViewAdapter(
    private val context: Context,
    private val itemCb: RecyclerItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<StructUserItem?>()
    private val tag = javaClass.simpleName

    interface RecyclerItemClickListener{

        fun onItemClick(view: View, position: Int)
    }

    fun setData(data: ArrayList<StructUserItem?>){

        this.data.addAll(data)
    }

    fun clearData(){

        this.data.clear()
    }

    fun getData(): ArrayList<StructUserItem?>{

        return this.data
    }

    fun addLoadingView() {

        Handler(Looper.getMainLooper()).post {
            data.add(null)
            notifyItemInserted(data.size - 1)
        }
    }

    fun removeLoadingView() {

        data.removeAt(data.size - 1)
        notifyItemRemoved(data.size)

    }

    class MyViewHolder(holder: View): RecyclerView.ViewHolder(holder){

        val circleImageViewUserListItem = holder.findViewById(R.id.circleImageViewUserListItem) as CircleImageView
        val tvLogin = holder.findViewById(R.id.tvLogin) as TextView
        val tvSiteAdmin = holder.findViewById(R.id.tvSiteAdmin) as TextView

    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == Constant.VIEW_TYPE_ITEM) {
            return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_user_list_item, parent, false))
        } else{
            //if (viewType == Constant.VIEW_TYPE_LOADING) {
            return LoadingHolder(LayoutInflater.from(context).inflate(R.layout.view_progress_loading, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position) == Constant.VIEW_TYPE_ITEM){

            val userItem = data[position]?:return
            val view = holder as MyViewHolder

            val avatarUrl = userItem.avatar_url
            val login = userItem.login
            val siteAdmin = userItem.site_admin

            if (avatarUrl != null) {

                Glide.with(context)
                    .load(avatarUrl)
                    .into(view.circleImageViewUserListItem)
            } else {
                view.circleImageViewUserListItem.setImageResource(R.mipmap.ic_launcher_round)
            }

            view.tvLogin.text = login ?: ""

            view.tvSiteAdmin.visibility = if(siteAdmin == null)
            {
                View.INVISIBLE
            }else
            {
                if(siteAdmin)
                {
                    View.VISIBLE
                }else
                {
                    View.INVISIBLE
                }
            }

            holder.itemView.setOnClickListener{

                itemCb.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == null) Constant.VIEW_TYPE_LOADING else Constant.VIEW_TYPE_ITEM
    }
}