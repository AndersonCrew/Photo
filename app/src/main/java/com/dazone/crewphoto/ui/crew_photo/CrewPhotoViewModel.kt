package com.dazone.crewphoto.ui.crew_photo

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dazone.crewphoto.base.BaseViewModel
import com.dazone.crewphoto.model.DateFile
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.retrofit.Result
import com.dazone.crewphoto.ui.main.MainRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class CrewPhotoViewModel : BaseViewModel() {
    private val repository = MainRepository()
    var allFileMutableLiveData: MutableLiveData<ArrayList<DateFile>> = MutableLiveData()
    var allFileImagesMutableLiveData: MutableLiveData<ArrayList<FileModel>> = MutableLiveData()

    fun getAllFile(params: JsonObject) = uiScope.launch {
        when (val result = repository.getAllFile(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val data: ArrayList<FileModel> = body["data"] as ArrayList<FileModel>
                    val list = Gson().fromJson<ArrayList<FileModel>>(
                        Gson().toJson(data),
                        object : TypeToken<List<FileModel>>() {}.type
                    )

                    allFileImagesMutableLiveData.postValue(list)
                    sortList(list)

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

    @SuppressLint("SimpleDateFormat")
    private fun sortList(list: List<FileModel>) {
        uiScope.launch {
            var map: HashMap<String, ArrayList<FileModel>> = hashMapOf()
            for(i in list.indices) {
                val date = SimpleDateFormat("yyyy-MM-dd").format(list[i].time)
                if(map.containsKey(date)) {
                    map[date]?.add(list[i])
                } else {
                    map[date] = arrayListOf()
                    map[date]?.add(list[i])
                }
            }

            val listDate: ArrayList<DateFile> = arrayListOf()
            for ((key, value) in map) {
                val dateFile = DateFile(key, value)
                val time = SimpleDateFormat("yyyy-MM-dd").parse(key).time
                dateFile.time = time
                listDate.add(dateFile)
            }

            allFileMutableLiveData.postValue(listDate)
        }
    }
}