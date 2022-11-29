package com.example.currencyconverter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val apiUrl = "https://api.apilayer.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}