package com.fcfm.unigrub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class RegisterAsRestaurant : AppCompatActivity() {
    //Variables de Entrada
    private lateinit var nameInput: EditText
    private lateinit var facultadInput: Spinner
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordconfirmInput: EditText

    //Variables de Botones
    private lateinit var registerBtn: Button
    private lateinit var registerAsCustomer: TextView
    private lateinit var backtologinTitle: TextView

    //Intent Reciclable
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_as_restaurant)

        //Anexar variables
        nameInput = findViewById(R.id.NameInput)
        facultadInput = findViewById(R.id.FacultadInput)
        emailInput = findViewById(R.id.EmailInput)
        passwordInput = findViewById(R.id.PasswordInput)
        passwordconfirmInput = findViewById(R.id.PasswordConfirmInput)

        registerBtn = findViewById(R.id.RegisterBtn)
        registerAsCustomer = findViewById(R.id.RegisterAsCustomerTitle)
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
                    null,
                    facultadInput.selectedItem.toString(),
                    emailInput.text.toString(),
                    passwordInput.text.toString()
                )
            }
        }

        //Asignar Comportamientos
        registerAsCustomer.setOnClickListener {
            intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
            finish()
        }

        backtologinTitle.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Revisar si los inputs son Apropiados
    private fun datosCorrectos(): Boolean {
        if (TextUtils.isEmpty(nameInput.text.toString())) {
            Toast.makeText(this, "Nombre(s) no puede estar vacio", Toast.LENGTH_LONG).show()
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
        lastName: String?,
        facultad: String,
        email: String,
        password: String,
    ) {
        val usuario = Usuario(name, lastName, facultad, email, password)

        val url = "http://20.232.215.102/includes/register_inc.php?action=create"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                Toast.makeText(this,"Usuario insertado exitosamente", Toast.LENGTH_LONG).show()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, Response.ErrorListener { error ->
                Toast.makeText(this,"Error $error ", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros= HashMap<String,String>()
                parametros.put("name",usuario.name.toString())
                parametros.put("lastName", usuario.lastName.toString())
                parametros.put("email",usuario.email.toString())
                parametros.put("password",usuario.password.toString())
                parametros.put("school",usuario.facultad.toString())
                parametros.put("userType", "2")

                return parametros
            }
        }

        queue.add(resultadoPost)
    }
}