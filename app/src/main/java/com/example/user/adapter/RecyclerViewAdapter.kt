package com.example.user.adapter

import android.content.Context
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
    private val itemCb: RecyclerItemClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var data = ArrayList<StructUserItem>()
    private val tag = javaClass.simpleName

    interface RecyclerItemClickListener{

        fun onItemClick(view: View, position: Int)
    }

    fun setData(data: ArrayList<StructUserItem>){

        this.data = data
    }

    fun clearData(){

        this.data.clear()
    }

    fun getData(): ArrayList<StructUserItem>{

        return this.data
    }

    class MyViewHolder(holder: View): RecyclerView.ViewHolder(holder){

        val circleImageViewUserListItem = holder.findViewById(R.id.circleImageViewUserListItem) as CircleImageView
        val tvLogin = holder.findViewById(R.id.tvLogin) as TextView
        val tvSiteAdmin = holder.findViewById(R.id.tvSiteAdmin) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_user_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply{

            val userItem = data[position]

            val avatarUrl = userItem.avatar_url
            val login = userItem.login
            val siteAdmin = userItem.site_admin

            if (avatarUrl != null) {

                Glide.with(context)
                    .load(avatarUrl)
                    .into(circleImageViewUserListItem)
            } else {
                circleImageViewUserListItem.setImageResource(R.mipmap.ic_launcher_round)
            }

            tvLogin.text = login ?: ""

            tvSiteAdmin.visibility = if(siteAdmin == null)
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

}