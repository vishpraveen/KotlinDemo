package com.example.magneto.kotlindemo.activity

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.magneto.kotlindemo.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

//    var animation_view:LottieAnimationView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        animation_view=findViewById<LottieAnimationView>(R.id.animation_view)

        animation_view.playAnimation()
//        animation_view.duration
        animation_view1.playAnimation()
        animation_view2.playAnimation()

        Toast.makeText(this,"on Splash Activity",Toast.LENGTH_SHORT).show()

//        intent= Intent(this,MainActivity::class.java)
//        startActivity(intent)
    }
}
