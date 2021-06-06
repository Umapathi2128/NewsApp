package com.uma.newsapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.utils.NetworkHelper
import java.lang.IllegalArgumentException

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */
class MainViewModelFactory(var dataManager: DataManager,var networkHelper: NetworkHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
           return MainViewModel(dataManager,networkHelper) as T
        }
        throw IllegalArgumentException("Unknown Exception")
    }
}