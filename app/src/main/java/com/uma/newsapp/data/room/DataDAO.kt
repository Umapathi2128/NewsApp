package com.uma.newsapp.data.room

import androidx.room.*

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-06.
 */
@Dao
interface DataDAO {
    @Query("SELECT * FROM data_items")
    suspend fun getAll(): List<DataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: DataEntity)

    @Query("DELETE FROM data_items")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(vararg entity: DataEntity)

    @Query("DELETE FROM data_items WHERE title = :title")
    suspend fun deleteByTitle(title: String?)
}