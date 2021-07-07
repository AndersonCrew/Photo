package com.dazone.crewphoto.retrofit

import com.dazone.crewphoto.base.BaseResponse
import com.dazone.crewphoto.utils.Config
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DazoneService {
    @POST(Config.LOGIN_V5)
    suspend fun login(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.CHECK_SSL)
    suspend fun checkSSL(@Body param: JsonObject): Response<BaseResponse>

    @Multipart
    @POST(Config.UPLOAD_FILE)
    suspend fun uploadFiles(
        @Part("sessionId") memo: RequestBody,
        @Part file: List<MultipartBody.Part>): Response<BaseResponse>

    @POST(Config.GET_FILE)
    suspend fun getAllFile(@Body param: JsonObject): Response<BaseResponse>
}
