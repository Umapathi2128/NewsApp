package com.uma.newsapp.data.network

import com.uma.newsapp.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-04.
 */
open class RetrofitBulder {

//    http://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=5495a6bd50884de7a4e8651dd1d0067c
//    https://api.weatherbit.io/v2.0/current?lat=19.0760&lon=72.8777&key=3d483d0e4d524272a10bc5865128d1eb&include=minutely


    private val WHEATHER_BASE_URL = " https://api.weatherbit.io/v2.0/"

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    fun getWheatherApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getNewsListApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}