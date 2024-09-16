package com.example.tugas_akhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tugas_akhir.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private  lateinit var binding : ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            val nama = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
             if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(confirmPassword.equals(password)){
                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                val user: FirebaseUser? = auth.currentUser
                                val userId:String = user!!.uid

                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                                val hashMap:HashMap<String,String> = HashMap()
                                hashMap.put("userId",userId)
                                hashMap.put("userName",nama)
                                hashMap.put("profileImage","")
                                hashMap.put("emailUser", email)
                                hashMap.put("Password",password)

                                databaseReference.setValue(hashMap).addOnCompleteListener(this){
                                    if (it.isSuccessful){
                                        //open home activity
                                        binding.etName.setText("")
                                        binding.etEmail.setText("")
                                        binding.etPassword.setText("")
                                        binding.etConfirmPassword.setText("")
                                        val intent = Intent(this@SignUpActivity,
                                            LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }else{
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                Log.i("regis",it.exception.toString())
                            }

                        }
                }else{
                    Toast.makeText(this, "Paswword do not match", Toast.LENGTH_SHORT).show()
                }
                    
               
            }else{
                 Toast.makeText(this, "colloumn must filled ", Toast.LENGTH_SHORT).show()
             }
        }
        binding.btnBack.setOnClickListener(){
            val intent =  Intent(this,LoginActivity::class.java )
            startActivity(intent)
        }

    }
}