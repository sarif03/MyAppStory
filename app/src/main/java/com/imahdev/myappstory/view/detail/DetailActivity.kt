package com.imahdev.myappstory.view.detail

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.imahdev.myappstory.R
import com.imahdev.myappstory.adapter.loadImage
import com.imahdev.myappstory.data.remote.response.ListStoryItem
import com.imahdev.myappstory.databinding.ActivityDetailBinding
import com.imahdev.myappstory.view.ViewModelFactory
import com.imahdev.myappstory.view.login.LoginVIewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        val story = intent.parcelable<ListStoryItem>(STORY_EXTRA)

        if (story!= null) {
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            binding.ivDetailPhoto.loadImage(url = story.photoUrl)
        }

    }
    private inline fun <reified T: Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
    companion object { const val STORY_EXTRA = "extra_story"}
}