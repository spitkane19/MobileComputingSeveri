package com.example.mobilecomputingseveri

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _pictureDataResult = MutableLiveData<String>()
    private val _usernameResult = MutableLiveData<String>()
    val usernameData: LiveData<String> get() = _usernameResult
    val pictureData: LiveData<String> get() = _pictureDataResult

    fun insertProfile(profile: ProfileDatabase) {
        viewModelScope.launch {
            profileRepository.insertProfile(profile)
        }
    }

    fun getProfileByUserName(userName: String) {
        viewModelScope.launch {
            profileRepository.getProfileByUserName(userName)
        }
    }

    fun getProfileByPictureData(pictureData: String) {
        viewModelScope.launch {
            profileRepository.getProfileByPictureData(pictureData)
        }
    }

    fun updateUserName(profileId: Int, newUserName: String) {
        viewModelScope.launch {
            profileRepository.updateUserName(profileId, newUserName)
        }
    }

    fun updatePictureData(profileId: Int, newPictureData: String) {
        viewModelScope.launch {
            profileRepository.updatePictureData(profileId, newPictureData)
        }
    }
    fun getPictureDataByProfileId(profileId: Int) {
        viewModelScope.launch {
            val result = profileRepository.getPictureDataById(profileId)
            _pictureDataResult.postValue(result)
        }
    }
    fun getUsernameByProfileId(profileId: Int){
        viewModelScope.launch {
            val result = profileRepository.getUsernameById(profileId)
            _usernameResult.postValue(result)
        }
    }
}

