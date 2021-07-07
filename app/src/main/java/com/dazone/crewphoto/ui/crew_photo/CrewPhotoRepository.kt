package com.dazone.crewphoto.ui.crew_photo

import androidx.lifecycle.LiveData
import com.dazone.crewphoto.base.BaseRepository
import com.dazone.crewphoto.base.BaseResponse
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.retrofit.Result
import com.dazone.crewphoto.retrofit.RetrofitFactory
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CrewPhotoRepository: BaseRepository() {

    suspend fun getAllFile(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getAllFile(params)}, errorMessage =  "Cannot Upload!")
    }
}