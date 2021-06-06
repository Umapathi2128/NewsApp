package com.uma.newsapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.uma.newsapp.R
import com.uma.newsapp.data.model.Article
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.databinding.FragmentHomeBinding
import com.uma.newsapp.ui.details.DetailScreenActivity
import com.uma.newsapp.ui.home.adapter.NewsListAdapter
import com.uma.newsapp.utils.NetworkHelper
import com.uma.newsapp.utils.Status

class HomeFragment : Fragment(), NewsListAdapter.NewsItemClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsListAdapter
    private lateinit var articleList : ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel =
            ViewModelProviders.of(this, context?.let { DataManager(it) }?.let {
                context?.let { NetworkHelper(it) }?.let { it1 ->
                    HomeViewModelFactory(
                        it,
                        it1
                    )
                }
            }).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleList = ArrayList()
        setUpRecyclerView()
        setUpObservables()
        addTextWatcher()
    }

    override fun onResume() {
        super.onResume()
//        binding.etxtSearch.text = null
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsListAdapter(this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = newsAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObservables() {
        viewModel.fetchNewsData()
        viewModel.newsList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.ERROR -> {
                    binding.apply {
                        pbNewsList.hide()
                        recyclerView.hide()
                        txtError.show()
                        txtError.text = it.message
                    }
                }
                Status.LOADING -> {
                    binding.apply {
                        pbNewsList.show()
                        txtError.hide()
                        recyclerView.hide()
                    }
                }
                Status.SUCCESS -> {
                    binding.apply {
                        pbNewsList.hide()
                        txtError.hide()
                        recyclerView.show()
                    }
                    val newsData = it.data
                    if (newsData?.totalResults!! > 1) {
                        articleList = newsData.articles as ArrayList<Article>
                        newsAdapter.addItems(articleList)
                    }
                }
            }
        })
    }

    private fun addTextWatcher() {
        binding.etxtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                filter(binding.etxtSearch.text.toString())
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String) {
        val newsList = ArrayList<Article>()
        for (i in 0 until articleList.size){
            if ((articleList[i].title.toLowerCase()).contains(text.toLowerCase())){
                newsList.add(articleList[i])
            }
        }
//        Log.e("test",articleList.toString())
        newsAdapter.updateList(newsList)
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }

    override fun onClickNewsListener(data: Article, id: ImageView) {
//        binding.etxtSearch.text = null
        val intent = Intent(context, DetailScreenActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("NewsDetails", data)
        intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options: ActivityOptionsCompat? =
                    activity?.let {
                        context?.resources?.getString(R.string.transitionName)?.let { it1 ->
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                it,
                                id,
                                it1
                            )
                        }
                    }
                context?.startActivity(intent, options?.toBundle())
            } else {
                context?.startActivity(intent)
            }
    }
}