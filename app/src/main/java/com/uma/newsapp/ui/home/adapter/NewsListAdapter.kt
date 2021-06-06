package com.uma.newsapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.data.DataHolder
import com.uma.newsapp.R
import com.uma.newsapp.data.model.Article
import com.uma.newsapp.databinding.RowNewsListBinding
import com.uma.newsapp.utils.Utils

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */

class NewsListAdapter(var listener: NewsItemClickListener) :
    RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>() {

    private lateinit var context: Context
    private lateinit var binding: RowNewsListBinding
    private var newsList: ArrayList<Article> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        context = parent.context

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_news_list,
            parent,
            false
        )
        return NewsListViewHolder(binding)
    }

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {

        holder.onBind(position)

        holder.itemView.setOnClickListener {
            listener.onClickNewsListener(newsList[position], holder.v.imgNews)
        }
    }

    fun addItems(list: ArrayList<Article>) {
        newsList.apply {
            clear()
        }
        newsList = list
        notifyDataSetChanged()
    }

    fun updateList(list: ArrayList<Article>) {
        newsList = list
        notifyDataSetChanged()
    }

    inner class NewsListViewHolder(var v: RowNewsListBinding) : RecyclerView.ViewHolder(v.root) {

        fun onBind(position: Int) {
            v.apply {
                txtTitle.text = newsList[position].title
//                txtAuthor.text = newsList[position].author
            }
            Glide.with(v.imgNews.context).load(newsList[position].urlToImage)
                .placeholder(Utils.getImagePlaceHolderLoading(v.imgNews.context)).error(R.drawable.ic_launcher_background)
                .into(v.imgNews)
        }
    }

    interface NewsItemClickListener {
        fun onClickNewsListener(data: Article, id: ImageView)
    }
}