package com.uma.newsapp.ui.newsList

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.uma.newsapp.R
import com.uma.newsapp.data.model.Article
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.databinding.ActivityNewsListBinding
import com.uma.newsapp.ui.details.DetailScreenActivity
import com.uma.newsapp.ui.home.HomeViewModel
import com.uma.newsapp.ui.home.HomeViewModelFactory
import com.uma.newsapp.ui.home.adapter.NewsListAdapter
import com.uma.newsapp.utils.NetworkHelper
import com.uma.newsapp.utils.Status

class NewsListFragment : Fragment(), NewsListAdapter.NewsItemClickListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsListAdapter: NewsListAdapter
    private lateinit var binding: ActivityNewsListBinding
    lateinit var countryList: Array<String>
    lateinit var catogoryList: Array<String>
    var country = "in"
    var category = "technology"

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_news_list, container, false)
        viewModel =
            ViewModelProviders.of(this,
                context?.let { DataManager(it) }?.let { NewsListViewFactory(it,
                    NetworkHelper(context!!)
                ) }).get(NewsListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        setUpSpiner()
        setUpObservers()
        binding.spCategory.onItemSelectedListener = this
        binding.spCountry.onItemSelectedListener = this
        viewModel.callNewsListApi(country,category)
    }

    private fun setUpUi() {
        newsListAdapter = NewsListAdapter(this)
        binding.recycleView.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = newsListAdapter
        }
    }

    private fun setUpObservers() {
        viewModel.newsList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycleView.visibility = View.GONE
                    binding.txtNEwsListError.visibility = View.VISIBLE
                    binding.txtNEwsListError.text = it.message
//                    it.message?.let { it1 -> showSnack(it1) }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recycleView.visibility = View.GONE
                    binding.txtNEwsListError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.txtNEwsListError.visibility = View.GONE
                    binding.recycleView.visibility = View.VISIBLE
                    val newsData = it.data
                    if (newsData?.totalResults!! > 1) {
//                           var articleList = newsData.articles as ArrayList<Article>
                        newsListAdapter.addItems(newsData.articles as ArrayList<Article>)
                    }
                }
            }

        })
    }

    private fun setUpSpiner() {
        countryList = arrayOf("in", "us")
        val countryAdapter =
            context?.let { ArrayAdapter<CharSequence>(it, R.layout.spiner_text, countryList) }
        countryAdapter?.setDropDownViewResource(R.layout.spiner_drop_down)
        binding.spCountry.adapter = countryAdapter

        catogoryList =
            arrayOf("technology", "entertainment", "business", "health", "science", "sports")
        val catogoryAdapter =
            context?.let { ArrayAdapter<CharSequence>(it, R.layout.spiner_text, catogoryList) }
        catogoryAdapter?.setDropDownViewResource(R.layout.spiner_drop_down)
        binding.spCategory.adapter = catogoryAdapter
    }

    private fun showSnack(text: String) {
        val snackbar = Snackbar
            .make(binding.recycleView, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun showSnackWithRetry(text: String) {
        val snackbar = Snackbar
            .make(binding.recycleView, text, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                viewModel.callNewsListApi(country, category)
            }
        snackbar.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spCountry -> {
                if (country == countryList[position]) {
                    return
                }
                country = countryList[position]
                viewModel.callNewsListApi(country, category)
            }
            R.id.spCategory -> {
                if (category == catogoryList[position]) {
                    return
                }
                category = catogoryList[position]
                viewModel.callNewsListApi(country, category)
            }
            else -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onClickNewsListener(data: Article, id: ImageView) {
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