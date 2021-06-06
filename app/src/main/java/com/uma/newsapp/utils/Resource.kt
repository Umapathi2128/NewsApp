package com.uma.newsapp.utils

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object{
//        fun <T> success(data : T) : Resource<T> = Resource(status = SUCCESS,data = data,message = null)
//
//        fun <T> error(data : T,message: String?) : Resource<T> = Resource(status = ERROR,data=data,message = message)
//
//        fun <T> loading(data : T) : Resource<T> = Resource(status = LOADING,data,message = null)

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}