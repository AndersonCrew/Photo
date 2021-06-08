package com.dazone.crewphoto.ui.splash

import androidx.lifecycle.LiveData
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.base.BaseRepository

class SplashRepository: BaseRepository() {
    fun getUser(): LiveData<User>? {
        return db?.getUserDao()?.getUser()
    }
}