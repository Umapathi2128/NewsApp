package com.uma.newsapp.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.uma.newsapp.R
import com.uma.newsapp.databinding.RowNavDrawerBinding
import com.uma.newsapp.ui.home.adapter.NewsListAdapter
import com.uma.newsapp.ui.main.NavigationItemModel

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-05.
 */

class NavigationRVAdapter(private var items: ArrayList<NavigationItemModel>,var listener : NavItemClickListener) :RecyclerView.Adapter<NavigationRVAdapter.NavigationItemViewHolder>() {

    private lateinit var context: Context
    private lateinit var binding: RowNavDrawerBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.row_nav_drawer,parent,false)
        return NavigationItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {

        holder.onBind(position)

        holder.itemView.setOnClickListener {
            listener.onClickNavListener(position)
        }

    }

    inner class NavigationItemViewHolder(var v: RowNavDrawerBinding) : RecyclerView.ViewHolder(v.root){

        fun onBind(position : Int){
            v.navigationIcon.setImageResource(items[position].icon)
            v.navigationTitle.text = items[position].title
        }
    }

    interface NavItemClickListener{
        fun onClickNavListener(position: Int)
    }
}