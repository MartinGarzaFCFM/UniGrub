package com.fcfm.unigrub

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.json.JSONArray
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    //Variables para elementos en pantalla
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerhereTitle: TextView

    //Variable del Intent
    private lateinit var intent: Intent



    //DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    object LoggedIn{
        var loggedIn: Usuario? = null
    }

    override fun onStart() {
        super.onStart()
        //reviso si ya inicio sesion
        val userLoggedIn = booleanPreferencesKey("userIsLogged")
        val userLoggedInFlow: Flow<Boolean> = dataStore.data.map {
            it[userLoggedIn] ?: false
        }



        userLoggedInFlow.asLiveData().observe(this) {
            if (it) {
                intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d(TAG, it.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        //Asignar Elementos a Variables
        usernameInput = findViewById(R.id.UsernameInput)
        passwordInput = findViewById(R.id.PasswordInput)
        loginBtn = findViewById(R.id.loginBtn)
        registerhereTitle = findViewById(R.id.RegisterHere)

        //Volley

        //Asignar Eventos
        loginBtn.setOnClickListener {
            postVolley()
            //Log.d("USUARIO", usuarioLogged.facultad.toString())
        }

        registerhereTitle.setOnClickListener {
            intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun postVolley() {

        //Variable URL de la API
        val queue = Volley.newRequestQueue(this)
        var url = "http://20.232.215.102/includes/login_inc.php?action=login"
        val requestBody = "email=chuck@gmail.com" + "&password=password"
        val stringReq: StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    val data  = response.toString()
                    val jArray = JSONArray(data)
                    val jObject = jArray.getJSONObject(0)

                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => error")
                }
            ) {
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }

        queue.add(stringReq)
    }
}