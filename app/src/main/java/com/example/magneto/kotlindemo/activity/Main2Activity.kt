package com.example.magneto.kotlindemo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.magneto.kotlindemo.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.lay_list_adapter2.*

class Main2Activity : AppCompatActivity() {

    var btn:Button?=null
    var btn_map:Button?=null
    var txt_name:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

//        for entering fullscreen mode
        window.decorView.systemUiVisibility=(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        var name=intent.getStringExtra("data")
        animation_view.playAnimation()

        btn=findViewById(R.id.btn)
        btn_map=findViewById(R.id.btn_map)
        txt_name=findViewById(R.id.txt_name)
        txt_name?.text=name

        btn_map?.setOnClickListener { var intent= Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


        btn?.setOnClickListener {
            var intent= Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
        rl.setOnClickListener {
            var intent= Intent(this, ThirdNew::class.java)
            startActivity(intent)
        }
    }


}
