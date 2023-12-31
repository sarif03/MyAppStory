package com.imahdev.myappstory.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.imahdev.myappstory.data.StoryPagingSource
import com.imahdev.myappstory.data.local.pref.UserModel
import com.imahdev.myappstory.data.local.pref.UserPreferences
import com.imahdev.myappstory.data.remote.response.ListStoryItem
import com.imahdev.myappstory.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(private var apiService: ApiService, private val userPreferences: UserPreferences){

    suspend fun saveSession(user : UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession() =  userPreferences.getSession().asLiveData()

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun logout() {
        userPreferences.logout()
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun addStory(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
    ) = apiService.uploadImage(token,multipartBody,description)

    suspend fun getStoriesWithLocation(token: String) = apiService.getStoriesWithLocation(token)


    fun getToken() = runBlocking {userPreferences.getSession().first().token }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
