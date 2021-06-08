package com.dazone.crewphoto.ui.image

import com.dazone.crewphoto.base.BaseRepository
import com.dazone.crewphoto.base.BaseResponse
import com.dazone.crewphoto.retrofit.Result
import com.dazone.crewphoto.retrofit.RetrofitFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ImageRepository: BaseRepository() {
    suspend fun uploadFile(sessionId: RequestBody, file: MultipartBody.Part): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().uploadFiles(sessionId, file)}, errorMessage =  "Cannot Upload!")
    }
}