package com.project.lalabib.storiesin.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.response.ListStoryItem
import com.project.lalabib.storiesin.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        populatedDetail()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.detail)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun populatedDetail() {
        val data = intent.getParcelableExtra<ListStoryItem>(DETAIL) as ListStoryItem
        binding.apply {
            tvName.text = data.name
            tvDesc.text = data.description
            Glide.with(this@DetailActivity)
                .load(data.photo)
                .fitCenter()
                .apply(
                    RequestOptions
                    .placeholderOf(R.drawable.ic_load)
                    .error(R.drawable.ic_broken_image))
                .into(ivImage)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val DETAIL = "detail"
    }
}