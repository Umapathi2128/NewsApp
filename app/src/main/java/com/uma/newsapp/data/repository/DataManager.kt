package com.uma.newsapp.data.repository

import android.content.Context
import com.uma.newsapp.data.network.ApiHelper
import com.uma.newsapp.data.room.AppDatabase
import com.uma.newsapp.data.room.DataEntity
import com.uma.newsapp.ui.details.DetailsModel
import com.uma.newsapp.utils.Utils

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-04.
 */
class DataManager(
    val ctx: Context,
    val apiHelper: ApiHelper = ApiHelper(),
    val db: AppDatabase = Utils.getDBService(ctx)
) : DataHelper {

    override suspend fun getNewsList(country: String, category: String) =
        apiHelper.getNewsList(country, category)

//    override suspend fun getWheatherData(lat: String, lon: String) = apiHelper.getWheaterList(lat, lon)

    override suspend fun getWeatherByGPS(
        latitude: String,
        longitude: String,
        units: String
    ) = apiHelper.getWeatherListByGps(latitude, longitude, units)

    override suspend fun getAll() = db.DataDAO().getAll()

    override suspend fun insertAll(vararg data: DataEntity) {
        db.DataDAO().insertAll(*data)
    }

    override suspend fun deleteAll() {
        db.DataDAO().deleteAll()
    }

    override suspend fun delete(vararg entity: DataEntity) {
        db.DataDAO().delete(*entity)
    }

    override suspend fun deleteByTitle(title: String?) {
        db.DataDAO().deleteByTitle(title)
    }
}