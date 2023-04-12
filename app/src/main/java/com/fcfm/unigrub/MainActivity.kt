package com.fcfm.unigrub

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    private lateinit var loginBtn: Button
    private lateinit var registerhereTitle: TextView

    private lateinit var intent: Intent

    override fun onStart() {
        super.onStart()
        //reviso si ya inicio sesion
        val currentUser = auth.currentUser
        if (currentUser != null) {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        usernameInput = findViewById(R.id.UsernameInput)
        passwordInput = findViewById(R.id.PasswordInput)

        loginBtn = findViewById(R.id.loginBtn)
        registerhereTitle = findViewById(R.id.RegisterHere)

        loginBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(
                usernameInput.text.toString(),
                passwordInput.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(
                        baseContext, "Authentication EXITO.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
        }

            registerhereTitle.setOnClickListener {
                intent = Intent(this, RegisterUser::class.java)
                startActivity(intent)
                finish()
            }

        }
    }