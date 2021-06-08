package com.dazone.crewphoto.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.base.BaseViewModel
import com.dazone.crewphoto.base.DazoneApplication
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.launch
import com.dazone.crewphoto.retrofit.Result
import com.dazone.crewphoto.utils.Constants

class LoginViewModel: BaseViewModel() {
    private val repository = LoginRepository()
    val loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val hasSSL: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loginSuccess.value = false
    }

    fun login(params: JsonObject) = uiScope.launch {
        when (val result = repository.login(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val data: LinkedTreeMap<String, Any> = body["data"] as LinkedTreeMap<String, Any>
                    data["session"]?.let {
                        DazoneApplication.getInstance().mPref?.setString(
                            Constants.ACCESS_TOKEN,
                            it as String
                        )
                    }

                    val gson = Gson()
                    val json = gson.toJson(data)
                    val user = gson.fromJson(json, User::class.java)

                    repository.setUser(user)
                    loginSuccess.postValue(true)
                } else {
                    val error: LinkedTreeMap<String, Any> = body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }

            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    fun checkSSL(params: JsonObject) = uiScope.launch {
        when (val result = repository.checkSSL(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val data: LinkedTreeMap<String, Any> = body["data"] as LinkedTreeMap<String, Any>
                    val ssl = data["SSL"] as Boolean
                    hasSSL.postValue(ssl)
                } else {
                    errorMessage.postValue("Cannot check SSL")
                }
            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    fun insertAndroidDevice(params: JsonObject) = uiScope.launch {
    }

    fun getUserLiveData(): LiveData<User>? {
        return repository.getUser()
    }
}