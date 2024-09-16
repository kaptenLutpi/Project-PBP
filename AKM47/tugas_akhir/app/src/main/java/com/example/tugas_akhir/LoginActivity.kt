package com.example.tugas_akhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugas_akhir.databinding.ActivityLoginBinding
import com.example.tugas_akhir.databinding.ActivityRoomChatBinding
import com.example.tugas_akhir.roomchat.ActivityRoomChat
import com.example.tugas_akhir.roomchat.ActivityUser
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.tblLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, ActivityUser::class.java )
                            startActivity(intent)
                            Toast.makeText(this, "Login succes", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "Email or Password wrong", Toast.LENGTH_SHORT).show()
                        }

                    }
            }else if (email.isEmpty()){
                Toast.makeText(this, "Email Tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(this, "password harus di isi", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                Toast.makeText(this, "password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "email or password empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.SignUp.setOnClickListener{
            val intent =  Intent(this,SignUpActivity::class.java )
            startActivity(intent)
        }

    }
}