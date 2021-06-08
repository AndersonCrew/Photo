package com.dazone.crewphoto.ui.splash

import androidx.lifecycle.LiveData
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.base.BaseViewModel

class SplashViewModel: BaseViewModel() {
    private val repository = SplashRepository()

    fun getUserLiveData(): LiveData<User>? {
        return repository.getUser()
    }
}