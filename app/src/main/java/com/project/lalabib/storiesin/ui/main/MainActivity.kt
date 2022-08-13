package com.project.lalabib.storiesin.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.model.dataStore
import com.project.lalabib.storiesin.databinding.ActivityMainBinding
import com.project.lalabib.storiesin.ui.login.LoginActivity
import com.project.lalabib.storiesin.ui.upload.UploadActivity
import com.project.lalabib.storiesin.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var token = ""
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupUser()
        setupData()
        addStory()
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

    private fun setupViewModel() {
        val pref = UserPreference.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
    }

    private fun setupUser() {
        showLoading()
        mainViewModel.getUser().observe(this) { user ->
            token = user.token
            if (user.isLogin) {
                getStories(token)
            } else {
                moveToLogin()
            }
        }
        showToast()
    }

    private fun setupData() {
        mainViewModel.storiesResponse.observe(this) {story ->
            if (story != null) {
                binding.rvStory.adapter = StoryAdapter(story.listStory)

            }
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

    private fun getStories(token: String) {
        mainViewModel.getListStory(token)
    }

    private fun showToast() {
        mainViewModel.txtMsg.observe(this@MainActivity) {
            it.getContentIfNotHandled()?.let {  txtMsg ->
                Toast.makeText(this, txtMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        mainViewModel.loading.observe(this@MainActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}