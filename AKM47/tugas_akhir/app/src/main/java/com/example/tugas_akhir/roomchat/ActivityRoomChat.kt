package com.example.tugas_akhir.roomchat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tugas_akhir.LoginActivity
import com.example.tugas_akhir.R
import com.example.tugas_akhir.adapter.AdapterChat
import com.example.tugas_akhir.databinding.ActivityRoomChatBinding
import com.example.tugas_akhir.model.Chat
import com.example.tugas_akhir.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class ActivityRoomChat : AppCompatActivity() {
    private  lateinit var binding : ActivityRoomChatBinding

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoomChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgProfile.setOnClickListener{
            val intent =  Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }






            firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)




        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                binding.tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    binding.imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ActivityRoomChat).load(user.profileImage).into(binding.imgProfile)
                }
            }
        })
        binding.btnSendMessage.setOnClickListener {
            var message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                binding.etMessage.setText("")
                topic = "/topics/$userId"
//                PushNotification(NotificationData( userName!!,message),
//                    topic).also {
//                    sendNotification(it)
//                }

            }
        }

        readMessage(firebaseUser!!.uid, userId)
    }
    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = AdapterChat(this@ActivityRoomChat, chatList)

                binding.chatRecyclerView.adapter = chatAdapter
            }
        })
    }
    }

