package com.fcfm.unigrub

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterUser : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth

    private lateinit var nameInput: EditText
    private lateinit var lastnameInput: EditText
    private lateinit var facultadInput: Spinner
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordconfirmInput: EditText

    private lateinit var registerBtn: Button
    private lateinit var backtologinTitle: TextView

    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeruser)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        nameInput = findViewById(R.id.NameInput)
        lastnameInput = findViewById(R.id.LastNameInput)
        facultadInput = findViewById(R.id.FacultadInput)
        emailInput = findViewById(R.id.EmailInput)
        passwordInput = findViewById(R.id.PasswordInput)
        passwordconfirmInput = findViewById(R.id.PasswordConfirmInput)

        registerBtn = findViewById(R.id.RegisterBtn)
        backtologinTitle = findViewById(R.id.BackToLoginTitle)

        //Llenar el array de las facultades
        ArrayAdapter.createFromResource(
            this,
            R.array.facultades,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            facultadInput.adapter = adapter
        }

        registerBtn.setOnClickListener {
            //revisar si los datos estan correctos
            if (datosCorrectos()) {
                //campos llenos, por lo tanto procede
                crearUsuario(
                    nameInput.text.toString(),
                    lastnameInput.text.toString(),
                    facultadInput.selectedItem.toString(),
                    emailInput.text.toString(),
                    passwordInput.text.toString()
                )
            }
        }

        backtologinTitle.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun datosCorrectos(): Boolean {
        if (TextUtils.isEmpty(nameInput.text.toString())) {
            Toast.makeText(this, "Nombre(s) no puede estar vacio", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(lastnameInput.text.toString())) {
            Toast.makeText(this, "Apellido(s) no puede estar vacio", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(facultadInput.selectedItem.toString())) {
            Toast.makeText(this, "Facultad no puede estar vacio", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(emailInput.text.toString())) {
            Toast.makeText(this, "Email no puede estar vacio", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(passwordInput.text.toString())) {
            Toast.makeText(this, "Contraseña no puede estar vacio", Toast.LENGTH_LONG).show()
            return false
        }
        if (!TextUtils.equals(
                passwordInput.text.toString(),
                passwordconfirmInput.text.toString()
            )
        ) {
            Toast.makeText(this, "Confirme que la contraseña sea la misma", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    private fun crearUsuario(
        name: String,
        lastName: String,
        facultad: String,
        email: String,
        password: String,
    ) {
        val usuario = Usuario(name, lastName, facultad, email, password)

        //Crear Usuario, Guardar en Firebase e Iniciar en HomeActivity
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                auth.currentUser?.let { database.child("usuarios").child(it.uid).setValue(usuario) }
                intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w(TAG, "createUserWIthEmail:failure", task.exception)
                Toast.makeText(
                    baseContext, "Authentication Failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}