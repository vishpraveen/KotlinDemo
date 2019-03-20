package com.example.magneto.kotlindemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.WordListAdapter
import com.example.magneto.kotlindemo.bean.Word
import com.example.magneto.kotlindemo.viewmodel.WordViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductActivity : AppCompatActivity() {
    private var mContext : Context?=null
    private lateinit var fab : FloatingActionButton
    private lateinit var productList : RecyclerView
    private lateinit var adapter : WordListAdapter

    private lateinit var wordViewModel : WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })
        mContext=this
        initUI()
        setClickListener()
    }

    companion object {
        const val newWordActivityRequestCode = 1
    }

    private fun setClickListener() {
        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext,AddProduct::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.let {
                val word = Word(it.getStringExtra(AddProduct.EXTAR_REPLY))
                wordViewModel.insert(word)
            }
        }else{
            Toast.makeText(
                    applicationContext,
                    "Empty String Not Saved",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUI() {
        fab= findViewById(R.id.fab)
        productList= findViewById(R.id.productList)
        adapter= WordListAdapter(mContext)
        productList.adapter = adapter
        productList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }
}
