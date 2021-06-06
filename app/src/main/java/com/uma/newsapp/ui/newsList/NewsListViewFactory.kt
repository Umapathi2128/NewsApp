package com.uma.newsapp.ui.newsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.utils.NetworkHelper
import java.lang.IllegalArgumentException
/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-06.
 */
class NewsListViewFactory(var dataManager: DataManager,var networkHelper: NetworkHelper)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)){
            return NewsListViewModel(dataManager,networkHelper) as T
        }
        throw IllegalArgumentException("Unknown class exception")
    }
}