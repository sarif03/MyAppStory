package com.imahdev.myappstory.view.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.imahdev.myappstory.view.maps.MapsActivity
import com.imahdev.myappstory.R
import com.imahdev.myappstory.adapter.LoadingStateAdapter
import com.imahdev.myappstory.adapter.StoryAdapter
import com.imahdev.myappstory.databinding.ActivityMainBinding
import com.imahdev.myappstory.view.ViewModelFactory
import com.imahdev.myappstory.view.add.AddStoryActivity
import com.imahdev.myappstory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        binding.swipeBottomRefresh.setOnRefreshListener {
            refreshStories()
        }
    }

    private fun setupView() {
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        mainViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                val adapter = StoryAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )

                mainViewModel.stories.observe(this) {
                    adapter.submitData(lifecycle, it)
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

    }

    private fun refreshStories() {
        if (isNetworkAvailable()) {
            mainViewModel.stories
            binding.swipeBottomRefresh.isRefreshing = false
        } else {
             binding.swipeBottomRefresh.isRefreshing = false
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Keluar")
                    .setMessage("Apakah anda yakin ingin keluar?")
                    .setCancelable(false)
                    .setPositiveButton("Ya") { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.cancel()
                    }.show()

            }
            R.id.action_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}