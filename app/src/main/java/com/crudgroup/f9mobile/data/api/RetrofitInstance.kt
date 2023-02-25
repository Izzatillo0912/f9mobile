package com.crudgroup.f9mobile.data.api

import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder().apply { addInterceptor(ApiInterceptor()) }.build()

    val api: F9Api by lazy { retrofit.create(F9Api::class.java) }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}