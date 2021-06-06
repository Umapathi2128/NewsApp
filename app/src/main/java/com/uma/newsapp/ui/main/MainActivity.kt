package com.uma.newsapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Looper.*
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.uma.newsapp.BuildConfig
import com.uma.newsapp.R
import com.uma.newsapp.data.repository.DataManager
import com.uma.newsapp.databinding.ActivityMainBinding
import com.uma.newsapp.ui.home.HomeFragment
import com.uma.newsapp.ui.main.adapter.NavigationRVAdapter
import com.uma.newsapp.ui.newsList.NewsListFragment
import com.uma.newsapp.utils.*

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), NavigationRVAdapter.NavItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val LOCATION_PERMISSION_REQUEST_CODE = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModel()
        setUpRecycler()
        setUpObservables()
        setUpTabListeners()
        setSupportActionBar(binding.activityMainToolbar)
        loadFragment(HomeFragment())


        binding.btnGotoSettings.setOnClickListener {
            goToAppSettings()
        }
        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.activityMainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    Utils.hideKeyboard(this@MainActivity)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    Utils.hideKeyboard(this@MainActivity)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        binding.drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     *  setting viewmodel...
     */
    private fun setViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                MainViewModelFactory(
                    DataManager(this),
                    NetworkHelper(this)
                )
            ).get(MainViewModel::class.java)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainBinding = viewModel
    }

    private fun setUpRecycler() {
        val listener: NavigationRVAdapter.NavItemClickListener = this
        binding.navigationRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = NavigationRVAdapter(
                arrayListOf(
                    NavigationItemModel(R.drawable.ic_baseline_home_24, "Home"),
                    NavigationItemModel(R.drawable.ic_outline_grade_24, "Clear")
                ), listener
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObservables() {
        viewModel.users.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    binding.apply {
                        pbWeather.hide()
                        txtWeatherError.show()
                        txtWeatherError.text = it.message
                    }
                }
                Status.LOADING -> {
                    binding.pbWeather.show()
                }
                Status.SUCCESS -> {
                    binding.pbWeather.hide()
                    binding.navErrorGroup.hide()
                    val weatherData = it.data

                    binding.apply {
                        txtCity.text = weatherData?.name
                        txtDescription.text = weatherData?.weather?.get(0)?.description
                        txtTemparature.text =
                            (weatherData?.main?.temp?.toInt()).toString() + " \u2103 "
                        txtDate.text = weatherData?.dt?.toLong()?.let { it1 -> Utils.getDate(it1) }
                        txtMaxTemp.text =
                            "Max_temp : " + (weatherData?.main?.tempMax?.toInt()).toString() + " \u2103 "
                        txtMinTemp.text =
                            "Min_temp : " + (weatherData?.main?.tempMin?.toInt()).toString() + " \u2103 "
                    }
                }
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    binding.navErrorGroup.show()
                    binding.pbWeather.hide()
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun goToAppSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID,
            null
        )
        intent.data = uri
        startActivity(intent)

        if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
            binding.drawerLayout.closeDrawer(Gravity.START)
        }
    }

    override fun onClickNavListener(position: Int) {
        when (position) {
            0 -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    loadFragment(HomeFragment())
                    binding.tabLayout.getTabAt(0)?.select()
                }
            }
            1 -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    Toast.makeText(this, "bookmark Cleared", Toast.LENGTH_SHORT).show()
                    viewModel.deleteAllDataFromDb()
                }
            }
            else -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }

    private fun loadFragment(fragment: Fragment) {
//        val fragment = HomeFragment()
//        val bundle = Bundle()
//        bundle.putSerializable("NewsList", newsList)
//        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }

    private fun setUpTabListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    in 0..2 -> {
                        loadFragment(HomeFragment())
                    }
                    else -> {
                        loadFragment(NewsListFragment())
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 10 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(1000*10).setFastestInterval(1000*10)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        viewModel.fetchWeatherData(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            },
            myLooper()
        )
    }
}