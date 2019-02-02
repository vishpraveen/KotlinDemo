package com.example.magneto.kotlindemo.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.RecyclerAdapter
import com.example.magneto.kotlindemo.bean.CityBean
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TestActivity : AppCompatActivity() {

    private lateinit var fabAdd : FloatingActionButton
    private lateinit var fabSave : FloatingActionButton
    private lateinit var recycler : RecyclerView
    private lateinit var tvText : TextView
    private lateinit var adapter : RecyclerAdapter
    private var arrayList : ArrayList<CityBean> = ArrayList()
    private var mContext: Context?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        mContext=this
        initUI()
        setRecycler()
        setClickListener()
    }

    private fun setRecycler() {
//        var layoutManager: LinearLayoutManager= LinearLayoutManager(mContext)
//        var layoutManager : GridLayoutManager = GridLayoutManager(mContext,2)
        var layoutManager : StaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = RecyclerAdapter(mContext, arrayList)
        recycler.layoutManager=layoutManager
        recycler.adapter=adapter
    }

    private fun setClickListener() {

        fabSave.setOnClickListener {
            setData()
        }

        fabAdd.setOnClickListener {
            addData()
        }
    }

    private fun setData() {
        var value1 : String =adapter.getArrayListData()
        Log.e("ListData",""+value1)
        tvText.text=value1
        /*var city : ArrayList<CityBean> = adapter.getArrayListData()
        Log.e("ListSize",""+city.size)
        for (cityBean in arrayList) {
            Log.e("Data",cityBean.cityName)
            tvText.text=cityBean.cityName
        }*/
    }

    private fun addData() {
        var city : CityBean= CityBean()
        city.cityName="";
        arrayList.add(city)
        adapter.notifyItemInserted(arrayList.size-1)
    }

    private fun initUI() {
        fabAdd=findViewById(R.id.fabAdd)
        fabSave=findViewById(R.id.fabSave)
        recycler=findViewById(R.id.recycler)
        tvText=findViewById(R.id.tvText)
    }
}
