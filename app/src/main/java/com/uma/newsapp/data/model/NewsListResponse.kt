package com.uma.newsapp.data.model

import java.io.Serializable

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */
data class NewsListResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
): Serializable

data class Article(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Serializable

data class Source(
    val id: Any?,
    val name: String
) : Serializable