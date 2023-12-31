package com.imahdev.myappstory.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.imahdev.myappstory.data.local.pref.UserModel
import com.imahdev.myappstory.data.remote.response.LoginResponse
import com.imahdev.myappstory.data.repository.StoryRepository
import com.imahdev.myappstory.view.register.RegisterActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginVIewModel(private val repository: StoryRepository): ViewModel() {

    companion object { private val TAG = RegisterActivity::class.java.simpleName}

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, pass: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.login(email, pass)
                saveSession(
                    UserModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        email,
                        response.loginResult.token,
                        isLogin = true
                    )
                )
                _isLoading.postValue(false)
                _loginResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _loginResponse.postValue(errorBody)

                Log.d(TAG,"register Error: $errorMessage" )
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}