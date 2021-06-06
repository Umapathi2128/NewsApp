package com.uma.newsapp.data.network

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-04.
 */
class ApiHelper : RetrofitBulder() {


    private fun getWheatherApi() =
        getWheatherApiRetrofit().create(ApiService::class.java)

    private fun getNewsApi() = getNewsListApiRetrofit().create(ApiService::class.java)

    suspend fun getNewsList(country: String, category: String) =
        getNewsApi().getNewsList(country, category)

//    suspend fun getWheaterList(lat: String, lon: String) =
//        getWheatherApi().getWheatherData(lat, lon)

    suspend fun getWeatherListByGps(latitude: String, longitude: String, units: String) =
        getWheatherApi().getWeatherByGPS(latitude, longitude, units)
}