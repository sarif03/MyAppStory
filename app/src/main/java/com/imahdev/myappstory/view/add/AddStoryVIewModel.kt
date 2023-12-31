package com.imahdev.myappstory.view.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.imahdev.myappstory.data.local.pref.UserModel
import com.imahdev.myappstory.data.remote.response.FileUploadResponse
import com.imahdev.myappstory.data.repository.StoryRepository
import com.imahdev.myappstory.view.register.RegisterActivity
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryVIewModel(private val repository: StoryRepository): ViewModel() {

    private val _fileUploadResponse = MutableLiveData<FileUploadResponse>()
    val fileUploadResponse: LiveData<FileUploadResponse> = _fileUploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> = repository.getSession()

    fun addStory(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.addStory("Bearer $token",multipartBody,description)

                _fileUploadResponse.postValue(response)
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, FileUploadResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _fileUploadResponse.postValue(errorBody)

                Log.d(TAG,"Upload FIle Error: $errorMessage" )
            }
        }
    }

    companion object { private val TAG = RegisterActivity::class.java.simpleName}
}