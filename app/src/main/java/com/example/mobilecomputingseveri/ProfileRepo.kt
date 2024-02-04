package com.example.mobilecomputingseveri

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val userDao: UserDao) {


    val allProfiles = MutableLiveData<List<ProfileDatabase>>()
    val foundProfile = MutableLiveData<ProfileDatabase>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertProfile(profile: ProfileDatabase) {
        withContext(Dispatchers.IO) {
            userDao.insertProfile(profile)
        }
    }

    suspend fun getProfileByUserName(userName: String): ProfileDatabase? {
        return withContext(Dispatchers.IO) {
            userDao.getProfileByUserName(userName)
        }
    }

    suspend fun getProfileByPictureData(pictureData: String): ProfileDatabase? {
        return withContext(Dispatchers.IO) {
            userDao.getProfileByPictureData(pictureData)
        }
    }

    suspend fun updateUserName(profileId: Int, newUserName: String) {
        withContext(Dispatchers.IO) {
            userDao.ensureInitialEntityExists()
            userDao.updateUserName(profileId, newUserName)
        }
    }

    suspend fun updatePictureData(profileId: Int, newPictureData: String) {
        withContext(Dispatchers.IO) {
            userDao.ensureInitialEntityExists()
            userDao.updatePictureData(profileId, newPictureData)
        }
    }
    suspend fun getPictureDataById(profileId: Int): String{
        return withContext(Dispatchers.IO){
            userDao.getPictureDataById(profileId)
        }
    }
    suspend fun getUsernameById(profileId: Int): String{
        return withContext(Dispatchers.IO){
            userDao.getuserNameById(profileId)
        }
    }

    fun getAllProfiles() {
        TODO("Not yet implemented")
    }
}