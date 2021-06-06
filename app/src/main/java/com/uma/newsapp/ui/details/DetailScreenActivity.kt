package com.uma.newsapp.ui.details

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.uma.newsapp.R
import com.uma.newsapp.data.model.Article
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.data.room.DataEntity
import com.uma.newsapp.databinding.ActivityDetailsScreenBinding
import com.uma.newsapp.utils.Constant
import com.uma.newsapp.utils.NetworkHelper
import com.uma.newsapp.utils.Utils

@RequiresApi(Build.VERSION_CODES.M)
class DetailScreenActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailsScreenBinding
    private lateinit var viewModel: DetailsViewModel
    private lateinit var newsData: DataEntity
    private lateinit var newsList: ArrayList<DataEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar?.hide()
        } catch (e: NullPointerException) {
        }
        setViewModel()
        initilizeData()
    }

    private fun getIntentData(): Article? {
        getAllDbData()
        val intent = this.intent
        val bundle = intent.extras
        var data: Article? = null
        if (bundle != null) {
            data = bundle.getSerializable(Constant.newsDetails) as Article
        }
        return data
    }

    private fun initilizeData() {
        newsList = ArrayList()
        val data = getIntentData()

        binding.imgBack.setOnClickListener(this)
        binding.txtBack.setOnClickListener(this)
        binding.imgBookmark.setOnClickListener(this)
        binding.imgDownload.setOnClickListener(this)
        binding.imgShare.setOnClickListener(this)
        binding.imgUpload.setOnClickListener(this)
        
        newsData = data?.title?.let {
            DataEntity(
                it,
                data.content,
                data.description,
                data.publishedAt,
                data.source.toString(),
                data.title,
                data.url,
                data.urlToImage
            )
        }!!
        binding.newsTitle.text = data?.title
        binding.newsDescription.text = data?.description
        binding.newsContent.text = data?.content?: "No data available"
        binding.txtDetailsDate.text = Utils.getDateFromString(data.publishedAt)
        binding.txtDeatilsAuthor.text = "By ${data?.author?:"Unknown"}"

        Glide.with(this).load(data?.urlToImage)
            .placeholder(Utils.getImagePlaceHolderLoading(this))
            .error(R.drawable.ic_launcher_background).into(binding.newsImage)
    }

    private fun setViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                DetailsViewModelFactory(
                    DataManager(this),
                    NetworkHelper(this)
                )
            ).get(DetailsViewModel::class.java)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_details_screen)
        binding.detailsBinding = viewModel
    }

    private fun getAllDbData() {
        viewModel.allDetails.observe(this, Observer {
            newsList.clear()
            newsList = it?.data as ArrayList<DataEntity>

            if (checkDataIsBookmarkedOrNot(newsList)) {
                binding.imgBookmark.setImageResource(R.drawable.ic_baseline_grade_24)
            } else binding.imgBookmark.setImageResource(R.drawable.ic_outline_grade_24)
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgBack -> {
                onBackPressed()
            }
            binding.txtBack -> {
                onBackPressed()
            }
            binding.imgBookmark -> {

                if (checkDataIsBookmarkedOrNot(newsList)) {
//                    viewModel.deleteByData(newsData)
                    newsData.title?.let { viewModel.deleteByTitle(it) }
                    viewModel.getAllData()
                    binding.imgBookmark.setImageResource(R.drawable.ic_outline_grade_24)
                    Toast.makeText(this, "Bookmark Cleared", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.imgBookmark.setImageResource(R.drawable.ic_baseline_grade_24)
                    viewModel.insertAll(newsData)
                    viewModel.getAllData()
                    Toast.makeText(this, "Bookmark added", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.imgDownload -> {
                Toast.makeText(this, "Download clicked", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.imgShare -> {
                Toast.makeText(this, "share clicked", Toast.LENGTH_SHORT).show()
            }
            binding.imgUpload -> {
                Toast.makeText(this, "upload clicked", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkDataIsBookmarkedOrNot(data: List<DataEntity>): Boolean {
        var isBookmarked = false
        for (i in data.indices) {
            if (data[i].title.equals(newsData.title)) {
                isBookmarked = true
            }
        }
        return isBookmarked
    }

}
