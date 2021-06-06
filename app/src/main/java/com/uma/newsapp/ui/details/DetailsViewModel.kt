package com.uma.newsapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uma.newsapp.data.model.WeatherResponse
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.data.room.DataEntity
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

class DetailsViewModel(var dataManager: DataManager, var networkHelper: NetworkHelper) :
    ViewModel() {

    private val _allDetails = MutableLiveData<Resource<List<DataEntity>>>()
    val allDetails: LiveData<Resource<List<DataEntity>>>
        get() = _allDetails

    init {
        getAllData()
    }

    fun getAllData() {
        viewModelScope.launch {
//            _allDetails.postValue(Resource.loading(null))
            _allDetails.postValue(Resource.success(dataManager.getAll()))
        }
    }

    fun insertAll(data: DataEntity) {
        viewModelScope.launch {
            dataManager.insertAll(data)
        }
    }

    fun deleteByData(data: DataEntity) {
        viewModelScope.launch {
            dataManager.delete(data)
        }
    }

    fun deleteByTitle(title: String) {
        viewModelScope.launch {
            dataManager.deleteByTitle(title)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            dataManager.deleteAll()
        }
    }
}