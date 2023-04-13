package com.fcfm.unigrub

import android.app.Notification.Action
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.unigrub.adapter.RecyclerAdapter
import com.fcfm.unigrub.data.Item
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var usuarioActivo: Usuario

    lateinit var mRecyclerView: RecyclerView
    val mAdapter: RecyclerAdapter = RecyclerAdapter()

    private lateinit var textView: TextView
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val header: View = navigationView.getHeaderView(0)

        val user = Firebase.auth.currentUser
        if (user == null){
            Log.w(ContentValues.TAG, "IniciarSesion:FAILURE")
            Toast.makeText(baseContext, "Fallo el Usuario, Saliendo...", Toast.LENGTH_LONG).show()

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            user?.let {
                usuarioActivo = Usuario(it.displayName, it.displayName, "Facultad", it.email)
            }
        }

        textView = header.findViewById(R.id.nav_header_textView)

        textView.text = usuarioActivo.email.toString()

        setUpRecyclerView()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_item_one->Toast.makeText(this, "Item 1", Toast.LENGTH_SHORT).show()
            R.id.nav_item_two->Toast.makeText(this, "Item 2", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three->Toast.makeText(this, "Item 3", Toast.LENGTH_SHORT).show()
            R.id.logOutTitle -> {
                FirebaseAuth.getInstance().signOut()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpRecyclerView(){
        mRecyclerView = findViewById<RecyclerView>(R.id.rvItemList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.RecyclerAdapter(getItems(), this)
        mRecyclerView.adapter = mAdapter
    }

    fun getItems(): MutableList<Item>{
        var items:MutableList<Item> = ArrayList()
        items.add(Item("Chicken Tikka Masala", "Tandoori Palace", "A popular Indian dish with tender chunks of marinated chicken in a spiced tomato-based sauce.", "asdf"))
        items.add(Item("Pad Thai", "Thai House", "A classic Thai dish with stir-fried rice noodles, shrimp, bean sprouts, and peanuts in a tangy sauce.", "asdf"))
        items.add(Item("Fish and Chips", "The Codfather", "A traditional British dish of battered fish and deep-fried chips, served with tartar sauce and lemon wedges.", "asdf"))
        items.add(Item("Sushi Roll", "Sushi Time", "A Japanese dish of vinegared rice and various fillings wrapped in seaweed, sliced into bite-sized pieces.", "asdf"))
        items.add(Item("Tacos al Pastor", "Taqueria El Charro", "A Mexican dish of marinated pork cooked on a vertical spit, served in soft corn tortillas with onions and cilantro.", "asdf"))
        items.add(Item("Margherita Pizza", "Pizza Roma", "A classic Italian pizza with tomato sauce, fresh mozzarella cheese, and basil leaves on a thin crust.", "asdf"))
        items.add(Item("Chicken Shawarma", "Sahara Grill", "A Middle Eastern dish of marinated chicken roasted on a spit and served in a pita with vegetables and sauce.", "asdf"))
        return items
    }
}