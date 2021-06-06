package com.uma.newsapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-06.
 */
@Entity(tableName = "data_items")
data class DataEntity(
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "publishedAt") var publishedAt: String?,
    @ColumnInfo(name = "source") var source: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "urlToImage") var urlToImage: String?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}