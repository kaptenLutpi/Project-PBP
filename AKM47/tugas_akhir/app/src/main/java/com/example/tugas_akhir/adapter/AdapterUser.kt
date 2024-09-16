package com.example.tugas_akhir.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugas_akhir.R
import com.example.tugas_akhir.model.User
import com.example.tugas_akhir.roomchat.ActivityRoomChat
import de.hdodenhof.circleimageview.CircleImageView

class AdapterUser(private val context : Context, private val userList : ArrayList<User>) : RecyclerView.Adapter<AdapterUser.ViewHolder>() {
    class ViewHolder(view : View): RecyclerView.ViewHolder(view)  {
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val txtTemp: TextView = view.findViewById(R.id.temp)
        val imgUser: CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUser.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterUser.ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.userName
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context,ActivityRoomChat::class.java)
            intent.putExtra("userId",user.userId)
            intent.putExtra("userName",user.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}