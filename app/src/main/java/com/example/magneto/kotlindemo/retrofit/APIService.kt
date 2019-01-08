package com.example.magneto.kotlindemo.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface APIService {
    companion object {
        val webHost:String= "http://www.omdbapi.com/"

        fun getBaseUrl():ApiService{
            val retrofit = Retrofit.Builder()
                    .baseUrl(webHost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(ApiService::class.java)
        }
           /* fun getBaseUrl():ApiService{
                val retrofit: Retrofit =Retrofit.Builder()
                        .baseUrl(webHost)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                return retrofit.create(ApiService::class.java)
            }*/

    }
}