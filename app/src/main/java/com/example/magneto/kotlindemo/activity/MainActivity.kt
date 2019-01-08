package com.example.magneto.kotlindemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.List_Adapter
import com.example.magneto.kotlindemo.adapter.List_Adapter2
import com.example.magneto.kotlindemo.bean.Login
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecycleClick
import com.example.magneto.kotlindemo.retrofit.APIService
import com.example.magneto.kotlindemo.retrofit.ApiService
import com.google.android.material.color.MaterialColors
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),RecycleClick {


    val data_list:ArrayList<String> = ArrayList()
    val click:RecycleClick=this
    val mContext:Context=this

    var recycler_list:RecyclerView?=null;
    var recycler_list2:RecyclerView?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI();


//        Toast.makeText(this,""+url,Toast.LENGTH_SHORT).show()



        ApiService.getInstance().login("{\"method\":\"login\",\"email\":\"mphp.magneto1@gmail.com\",\"password\":\"12345\",\"loginType\":\"app\",\"socialID\":\"\",\"deviceToken\":\"\", \"deviceType\":\"ios\", \"deviceEnvironment\":\"sandbox\"}")
                .enqueue(object :Callback<Login>{
                    override fun onFailure(call: Call<Login>?, t: Throwable?) {
                        msg(""+ t?.message)
                    }

                    override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                        if(response!=null && response.isSuccessful){
                            if(response.body()!=null){
                                msg(""+ (response.body()!!.data?.event?.get(0)?.eventType))
                            }
                        }
                    }
                })


        add_data()
        recycler_list?.layoutManager= GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false)
        recycler_list?.adapter= List_Adapter(this,data_list,click)

        recycler_list2?.layoutManager=GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false)
        recycler_list2?.adapter= List_Adapter2(this,data_list,click)

        animation_view.playAnimation()
    }

    private fun initUI() {
        recycler_list=findViewById(R.id.recycler_list)
        recycler_list2=findViewById(R.id.recycler_list2)
    }

    private fun msg(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    override fun click(where: EnumClicks, view: View, position: Int) {
        if(where==EnumClicks.CELL) {
//            view.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))


            Toast.makeText(this, "Cilcked on:\n" + data_list.get(position), Toast.LENGTH_SHORT).show()


            val intent=Intent(mContext, Main2Activity::class.java)
            intent.putExtra("data",data_list.get(position))
            var options:ActivityOptionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(this,view,"adapter1")
            startActivity(intent,options.toBundle())
//            finish()
        }
    }

    private fun add_data() {
        data_list.add("Honey")
        data_list.add("Zack")
        data_list.add("Max")
        data_list.add("Rocket")
        data_list.add("Quil")
        data_list.add("Kai")
        data_list.add("Tigger")
        data_list.add("Essai")
        data_list.add("Rias")
        data_list.add("Manderene")
        data_list.add("Tony")
        data_list.add("Falk")
        data_list.add("Wonda")
        data_list.add("Honda")
        data_list.add("Ben")
        data_list.add("Honey")
        data_list.add("Zack")
        data_list.add("Max")
        data_list.add("Rocket")
        data_list.add("Quil")
        data_list.add("Kai")
        data_list.add("Tigger")
        data_list.add("Essai")
        data_list.add("Rias")
        data_list.add("Manderene")
        data_list.add("Tony")
        data_list.add("Falk")
        data_list.add("Wonda")
        data_list.add("Honda")
        data_list.add("Ben")
        data_list.add("Honey")
        data_list.add("Zack")
        data_list.add("Max")
        data_list.add("Rocket")
        data_list.add("Quil")
        data_list.add("Kai")
        data_list.add("Tigger")
        data_list.add("Essai")
        data_list.add("Rias")
        data_list.add("Manderene")
        data_list.add("Tony")
        data_list.add("Falk")
        data_list.add("Wonda")
        data_list.add("Honda")
        data_list.add("Ben")
    }
}




