package com.example.magneto.kotlindemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.magneto.kotlindemo.R

class AddProduct : AppCompatActivity() {
    private var mContext : Context?=null
    private lateinit var constraintMain : ConstraintLayout
    private lateinit var ivProductImage : ImageView
    private lateinit var edtProductName : EditText
    private lateinit var edtProductDescription : EditText
    private lateinit var edtProductPrice : EditText
    private lateinit var btnAddProduct : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        mContext=this
        initUI()
        setOnClickListener()

    }

    private fun setOnClickListener() {
        btnAddProduct.setOnClickListener {
            val replyIntent= Intent()
            if (TextUtils.isEmpty(edtProductName.text)){
                setResult(Activity.RESULT_CANCELED,replyIntent)
            }else{
                val word = edtProductName.text.toString()
                replyIntent.putExtra(EXTAR_REPLY,word)
                setResult(Activity.RESULT_OK,replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTAR_REPLY="com.example.android.wordlistsql.REPLY"
    }

    private fun initUI() {
        constraintMain=findViewById(R.id.constraintMain)
        ivProductImage=findViewById(R.id.ivProductImage)
        edtProductName=findViewById(R.id.edtProductName)
        edtProductDescription=findViewById(R.id.edtProductDescription)
        edtProductPrice=findViewById(R.id.edtProductPrice)
        btnAddProduct=findViewById(R.id.btnAddProduct)
    }
}
