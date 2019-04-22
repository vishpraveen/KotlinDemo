package com.example.kotlinandroidcomponentsdemo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.kotlinandroidcomponentsdemo.R
import com.google.android.material.appbar.CollapsingToolbarLayout

class MyProfileActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        initUI()
        setClickListener()
    }

    private fun setClickListener() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initUI() {
        collapsingToolbar=findViewById(R.id.collapsingToolbar)
//        collapsingToolbar.title=getString(R.string.profile_act)
        toolbar=findViewById(R.id.toolbar)
//        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
