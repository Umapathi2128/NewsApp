package com.uma.newsapp.ui.details

import androidx.room.ColumnInfo

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */

data class DetailsModel(
    var author: String,
    var content: String,
    var description: String,
    var publishedAt: String,
    var source: String,
    var title: String,
    var url: String,
    var urlToImage: String
)