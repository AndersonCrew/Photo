package com.dazone.crewphoto.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dazone.crewphoto.R
import com.dazone.crewphoto.dialog.DialogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment: Fragment() {
    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var eventDisposable: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DazoneApplication.eventBus
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onEventReceive(it) }
            .addTo(eventDisposable)
    }



    override fun onResume() {
        super.onResume()
        initEvents()
        initViewModels()
    }

    abstract fun initEvents()
    abstract fun initViewModels()
    open fun onEventReceive(it: Map<String, Any?>) {

    }

    fun showProgress(baseActivity: BaseActivity) {
        baseActivity.showProgress()
    }

    fun hideProgress(baseActivity: BaseActivity) {
        baseActivity.hideProgress()
    }
}