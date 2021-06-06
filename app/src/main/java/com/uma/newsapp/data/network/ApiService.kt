package com.uma.newsapp.data.network

import com.uma.newsapp.data.model.NewsListResponse
import com.uma.newsapp.data.model.WeatherResponse
import com.uma.newsapp.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-04.
 */
interface ApiService {

    @GET("top-headlines?apiKey=${Constant.NEWS_API_KEY}")
    suspend fun getNewsList(
        @Query("country") country: String,
        @Query("category") category: String
    ): Response<NewsListResponse>

//    @GET("current?key=3d483d0e4d524272a10bc5865128d1eb")
//    suspend fun getWheatherData(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String
//    ): WheatherResponse

    @GET("weather?appid=${Constant.WEATHER_API_KEY}")
    suspend fun getWeatherByGPS(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

}