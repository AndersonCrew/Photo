package com.dazone.crewphoto.ui.login

import androidx.lifecycle.LiveData
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.base.BaseRepository
import com.dazone.crewphoto.base.BaseResponse
import com.dazone.crewphoto.retrofit.RetrofitFactory
import com.google.gson.JsonObject
import com.dazone.crewphoto.retrofit.Result

class LoginRepository(): BaseRepository() {
    suspend fun login(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().login(param)}, errorMessage =  "Cannot login!")
    }

    suspend fun checkSSL(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.apiServiceNonBaseUrl.checkSSL(params)}, errorMessage =  "Cannot check SSL this domain!")
    }

    fun setUser(user: User) {
        db?.getUserDao()?.addUserData(user)
    }

    fun getUser(): LiveData<User>? {
        return db?.getUserDao()?.getUser()
    }
}