package com.imahdev.myappstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imahdev.myappstory.data.local.pref.UserModel
import com.imahdev.myappstory.data.remote.response.ListStoryItem
import com.imahdev.myappstory.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository): ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        repository.getStories("Bearer ${repository.getToken()}").cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> = repository.getSession()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}

