package com.example.kotlinandroidcomponentsdemo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.kotlinandroidcomponentsdemo.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private var context: Context? = null

    //    setting Navigation Components
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var navigationview: NavigationView
    private lateinit var fab: FloatingActionButton
    private lateinit var mainFrame: FrameLayout
    private var homeFragment : HomeFragment = HomeFragment()
    private var profileFragment: ProfileFragment = ProfileFragment()
    private var fragmentManager: FragmentManager= supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initUI()
        setonClickListener()
        setHomeFragment()
    }


    private fun setHomeFragment() {
        addFragmentToActivity(fragmentManager,homeFragment,R.id.mainFrame)
    }

    private fun setonClickListener() {
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
//      Click events when clicked on navigation options
        navigationview.setNavigationItemSelectedListener { menuItem: MenuItem ->
            //         set item as selected to persist highlight

//            close the drawer when it is tapped
            drawerLayout.closeDrawer(GravityCompat.START)
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            when (menuItem.itemId) {
                R.id.home_fragment -> {
                    addFragmentToActivity(fragmentManager,homeFragment,R.id.mainFrame)
                    menuItem.isChecked = true
                    toolbar.title=getString(R.string.home)
                    msg(getString(R.string.home))
                } R.id.profile_fragment -> {
                    addFragmentToActivity(fragmentManager,profileFragment,R.id.mainFrame)
                    menuItem.isChecked = true
                    toolbar.title=getString(R.string.profile_frag)
                    msg(getString(R.string.profile_frag))
                }
                R.id.profile_activity -> {
                    val intent = Intent(this, MyProfileActivity::class.java)
                    startActivity(intent)
//                    addFragmentToActivity(fragmentManager,profileFragment,R.id.mainFrame)
//                    toolbar.title=getString(R.string.profile)
                    msg(getString(R.string.profile_act))
                }
            }
            true
        }
    }

    private fun msg(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show();
    }

    private fun initUI() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar =findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.home)
//        title= toolbar.findViewById(R.id.toolbar_title)
//        title.text=getString(R.string.home)
        toolbar.inflateMenu(R.menu.notification_count)
        setSupportActionBar(toolbar)
        navigationview = findViewById(R.id.navigation)
        bottomAppBar = findViewById(R.id.bar)
        mainFrame = findViewById(R.id.mainFrame)
        val actionBar : ActionBar? = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notification_count,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.notification ->{
                msg("Clicked on Notification")
            }
        }
        return true
    }

//    Adding fragment
    fun addFragmentToActivity( manager: FragmentManager, fragment: Fragment, fragmentId: Int){
        val transition: FragmentTransaction = manager.beginTransaction()
        transition.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
        transition.replace(fragmentId,fragment)
        transition.commit()
    }
}
