package com.imahdev.myappstory.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.imahdev.myappstory.data.remote.response.StoryResponse
import com.imahdev.myappstory.data.repository.StoryRepository
import com.imahdev.myappstory.view.register.RegisterActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    private val _storiesWithLocation = MutableLiveData<StoryResponse>()
    val storiesWithLocation: LiveData<StoryResponse> = _storiesWithLocation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getStoriesWithLocation() {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation("Bearer ${repository.getToken()}")
                _isLoading.postValue(false)
                _storiesWithLocation.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                Log.d(TAG," Error: $errorMessage" )
            }
        }
    }
    companion object { private val TAG = RegisterActivity::class.java.simpleName}
}