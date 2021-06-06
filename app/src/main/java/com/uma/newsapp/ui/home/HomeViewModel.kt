package com.uma.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uma.newsapp.data.model.NewsListResponse
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
class HomeViewModel(var dataManager: DataManager, var networkHelper: NetworkHelper) : ViewModel() {

    private val _newsList = MutableLiveData<Resource<NewsListResponse>>()
    val newsList: LiveData<Resource<NewsListResponse>>
        get() = _newsList

    fun fetchNewsData() {
        viewModelScope.launch {
            _newsList.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                dataManager.getNewsList(Constant.country,Constant.category).let {
                    if (it.isSuccessful)
                        _newsList.postValue(Resource.success(it.body()))
                    else _newsList.postValue(Resource.error(data = null, msg = "Something went wrong"))
                }
            }else _newsList.postValue(Resource.error(data = null, msg = "No internet"))
        }
    }
}