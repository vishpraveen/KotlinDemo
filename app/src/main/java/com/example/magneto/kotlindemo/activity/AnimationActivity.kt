package com.example.magneto.kotlindemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.magneto.kotlindemo.R

class AnimationActivity : AppCompatActivity() {

    private var cl_main:ConstraintLayout?=null
    private var ll_main:LinearLayout?=null
    private var btn_animation:Button?=null
    private var tv_text:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        initUI()
        setClickListener()
    }

    private fun setClickListener() {
        btn_animation?.setOnClickListener {

            var transition: Transition =TransitionInflater.from(this).inflateTransition(R.transition.fade)

            var mFade: Fade = Fade(Fade.IN)
            TransitionManager.beginDelayedTransition(cl_main,transition)

            if (tv_text?.visibility== View.GONE){
                tv_text?.visibility= View.VISIBLE
            }
            else{
                tv_text?.visibility= View.GONE
            }

        }
    }

    private fun initUI() {
        cl_main=findViewById(R.id.cl_main)
        ll_main=findViewById(R.id.ll_main)
        btn_animation=findViewById(R.id.btn_animation)
        tv_text=findViewById(R.id.tv_text)
    }
}
