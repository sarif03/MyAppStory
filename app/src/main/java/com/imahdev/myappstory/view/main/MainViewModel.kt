package com.imahdev.myappstory.view.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imahdev.myappstory.data.local.pref.UserModel
import com.imahdev.myappstory.data.remote.response.StoryResponse
import com.imahdev.myappstory.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories : LiveData<StoryResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession():LiveData<UserModel> = repository.getSession()

    fun logout() {
         viewModelScope.launch {
             repository.logout()
         }
    }
    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getStories("Bearer ${repository.getToken()}")
                _stories.value = result
            } catch (e: Exception) {
                _stories.value = StoryResponse(error = true, message = "Tidak ada koneksi internet")
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}

