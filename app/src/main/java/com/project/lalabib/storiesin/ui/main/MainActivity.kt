package com.project.lalabib.storiesin.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.LoadingStateAdapter
import com.project.lalabib.storiesin.databinding.ActivityMainBinding
import com.project.lalabib.storiesin.ui.login.LoginActivity
import com.project.lalabib.storiesin.ui.maps.MapsActivity
import com.project.lalabib.storiesin.ui.upload.UploadActivity
import com.project.lalabib.storiesin.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private var token = ""
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAdapter()
        addStory()
        setupUser()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupView() {
        with(binding.rvStory) {
            layoutManager = if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this@MainActivity, 2)
            } else {
                LinearLayoutManager(this@MainActivity)
            }
            setHasFixedSize(true)
        }
    }

    private fun setupUser() {
        mainViewModel.getUser().observe(this) { user ->
            token = user.token
            if (!user.isLogin) {
                moveToLogin()
            } else {
                getStories()
            }
        }
    }

    private fun setupAdapter() {
        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun addStory() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadActivity::class.java))
        }
    }

    private fun moveToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun getStories() {
        mainViewModel.getListStory.observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                finish()
                true
            }
            R.id.map -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}