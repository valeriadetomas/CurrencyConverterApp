package com.example.currencyconverter

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RatesApi {
    @Headers("apikey: " + "ejAL8A6mLhgebun4pYelnwVRcCSy7K8F")
    @GET("/fixer/latest?")
    suspend fun getData() : Response<WorldCurrency>

    @Headers("apikey: " + "ejAL8A6mLhgebun4pYelnwVRcCSy7K8F")
    @GET("/fixer/symbols?")
    suspend fun getSymbols() : Response<SymbolsData>
}