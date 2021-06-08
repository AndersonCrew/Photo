package com.dazone.crewphoto.ui.image

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dazone.crewphoto.base.BaseViewModel
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.retrofit.Result
import com.dazone.crewphoto.utils.Constants
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ImageViewModel : BaseViewModel() {
    private val repository = ImageRepository()
    var fileMutableLiveData: MutableLiveData<FileModel> = MutableLiveData()

    fun uploadFile(file: File) = uiScope.launch {
        fileMutableLiveData = MutableLiveData()
        Log.d("UPLOAD_FILE_SERVER", "${file.path}")
        val sessionId = DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, "")?: return@launch
        val requestMemo = sessionId.toRequestBody("text/plain".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("*/*".toMediaTypeOrNull())
        )

        when (val result = repository.uploadFile(requestMemo, body)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: ArrayList<FileModel> = body["data"] as ArrayList<FileModel>
                    val list = Gson().fromJson<ArrayList<FileModel>>(
                        Gson().toJson(data),
                        object : TypeToken<List<FileModel>>() {}.type
                    )

                    if (!list.isNullOrEmpty()) {
                        fileMutableLiveData.postValue(list[0])
                    }

                } else {
                    val error: LinkedTreeMap<String, Any> =
                        body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }
            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }
}