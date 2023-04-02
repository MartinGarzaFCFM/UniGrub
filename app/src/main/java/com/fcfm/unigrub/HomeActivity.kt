package com.fcfm.unigrub

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity: AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {

    //Manejar de forma global nuesto drawer
    private lateinit var drawer: DrawerLayout
    //Configura el icono de la aplicación ubicado a la izquierda de la barra de acciones o la barra de herramientas
    //para abrir y cerrar el cajón de navegación
    private lateinit var toggle: ActionBarDrawerToggle
    //Indice de nuestra seleccion
    private var intSelection:Int =  0
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawer = findViewById(R.id.drawer_layout)
        //Es el icono de hamburguesa
        //El contexto, drawlayout, toolbar, accesibilidad texto cuando abre, accesibilidad texto cuando cierra
        toggle = ActionBarDrawerToggle(this, drawer, toolbarHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //conectamos nuestros toogle con el navigation draw
        drawer.addDrawerListener(toggle)
        //lo habilitamos para la navegación "ascendente" a través de
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Habilitamos el icono de la aplicación a través
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        //Para capturar el evento click de las opciones de menu
        navigationView.setNavigationItemSelectedListener(this)


    }
    */
}