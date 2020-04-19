package com.global.test.globaltest.network

import com.revolut.network.DataService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class WebClient {

    companion object {
        const val host = " https://hiring.revolut.codes/api/android//"

    }

    private val retrofit: Retrofit


    init {
        val interceptor = HttpLoggingInterceptor()
       // interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        retrofit = Retrofit.Builder()
            .baseUrl(host)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    fun dataService() = retrofit.create(DataService::class.java)
}