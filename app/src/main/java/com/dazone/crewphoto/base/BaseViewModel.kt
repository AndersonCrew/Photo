package com.dazone.crewphoto.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel(): ViewModel() {
    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    val errorMessage: MutableLiveData<String> = MutableLiveData()
}