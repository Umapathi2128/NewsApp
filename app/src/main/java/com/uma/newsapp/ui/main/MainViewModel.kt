package com.uma.newsapp.ui.main

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uma.newsapp.data.model.NewsListResponse
import com.uma.newsapp.data.model.WeatherResponse
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.utils.Constant
import com.uma.newsapp.utils.NetworkHelper
import com.uma.newsapp.utils.Resource
import kotlinx.coroutines.launch

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */
class MainViewModel(var dataManager: DataManager, var networkHelper: NetworkHelper) : ViewModel() {

    private val _users = MutableLiveData<Resource<WeatherResponse>>()
    val users: LiveData<Resource<WeatherResponse>>
        get() = _users

    fun fetchWeatherData(lat: String, lang: String) {
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                dataManager.getWeatherByGPS(lat, lang, Constant.METRIC).let {
                    if (it.isSuccessful)
                        _users.postValue(Resource.success(it.body()))
                    else _users.postValue(Resource.error(data = null, msg = "Something went wrong"))
                }
            }else _users.postValue(Resource.error(data = null, msg = "No internet"))
        }
    }

    fun deleteAllDataFromDb(){
        viewModelScope.launch {
            dataManager.deleteAll()
        }
    }
}