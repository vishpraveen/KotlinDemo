package com.example.magneto.kotlindemo.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.bean.CityBean
import kotlin.math.nextTowards
import kotlin.math.nextUp
import kotlin.random.Random

class RecyclerAdapter(var mContext: Context?,var arrayList: ArrayList<CityBean>) : RecyclerView.Adapter<RecyclerAdapter.DataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        var view: View=LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler,parent,false)
        return DataHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        var random=(150..450).random()

        holder.etText.height=random
        var CityName="";
        holder.etText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
               /* arrayList.forEach{
                    CityName=CityName+it+","
                }*/
                var text:String =p0.toString()
                arrayList.get(position).cityName=text
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    class DataHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var etText:EditText=itemView.findViewById(R.id.etText)
    }

    fun getArrayListData():String {
        var data : String=""
        /*for (cityBean in arrayList) {
            data=data+cityBean.cityName+", "
        }*/

        for(i in 0 until arrayList.size){
            Log.e("passing",arrayList.get(i).cityName)
            data += arrayList.get(i).cityName
        }
        return data
    }
}

