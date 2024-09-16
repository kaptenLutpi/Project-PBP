package com.example.tugas_akhir.roomchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir.R
import com.example.tugas_akhir.adapter.AdapterUser
import com.example.tugas_akhir.databinding.ActivityUserBinding
import com.example.tugas_akhir.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class ActivityUser : AppCompatActivity() {
    var userList = ArrayList<User>()
    private  lateinit var binding : ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewChat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseService.token = it.token
//        }

        binding.imageView.setOnClickListener{
            val intent =  Intent(this, ActivityProfile::class.java )
            startActivity(intent)
        }





        getUsersList()
    }
    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
//                if (currentUser!!.profileImage == ""){
//                    imgProfile.setImageResource(com.google.firebase.database.R.drawable.profile_image)
//                }else{
//                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(imgProfile)
//                }

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = AdapterUser(this@ActivityUser, userList)

                binding.listViewChat.adapter = userAdapter
            }

        })
    }
}