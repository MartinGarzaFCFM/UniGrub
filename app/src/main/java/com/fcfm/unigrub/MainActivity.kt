package com.fcfm.unigrub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject


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
    private lateinit var userDataString : String

    object LoggedIn{
        var loggedIn: Usuario? = null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Revisar si ya se inicio sesion
        lifecycleScope.launch {
            val isLoggedIn = buscarLogIn("isLoggedIn")
            if (isLoggedIn == true){
                val toHome = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(toHome)
            }
        }

        //Asignar Elementos a Variables
        usernameInput = findViewById(R.id.UsernameInput)
        passwordInput = findViewById(R.id.PasswordInput)
        loginBtn = findViewById(R.id.loginBtn)
        registerhereTitle = findViewById(R.id.RegisterHere)

        //Volley

        //Asignar Eventos
        loginBtn.setOnClickListener {
            iniciarSesion()
        }

        registerhereTitle.setOnClickListener {
            intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun iniciarSesion() {
        //Variable URL de la API
        val queue = Volley.newRequestQueue(this)
        var url = "http://20.232.215.102/includes/login_inc.php?action=login"

        //Recoge datos de la Pantalla
        val email = usernameInput.text.toString()
        val password = passwordInput.text.toString()

        var resultadoPost = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                val data = JSONObject(response)
                if(data["check"] == true){
                    Toast.makeText(this,"Iniciando Sesion...", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userData", response)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Usuario Invalido", Toast.LENGTH_LONG).show()
                }


            }, Response.ErrorListener { error ->
                Toast.makeText(this,"Error $error ", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros= HashMap<String,String>()
                parametros["email"] = email
                parametros["password"] = password

                return parametros
            }

            override fun getBody(): ByteArray {
                return super.getBody()
            }
        }
        queue.add(resultadoPost)
    }

    private fun saveResponseString(data:String) {
        userDataString = data
    }

    private suspend fun buscarLogIn(key: String): Boolean?{
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


}