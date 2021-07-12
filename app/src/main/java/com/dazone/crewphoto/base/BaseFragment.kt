package com.dazone.crewphoto.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.dazone.crewphoto.dialog.DialogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


abstract class BaseFragment: Fragment() {
    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var eventDisposable: CompositeDisposable = CompositeDisposable()
    var isLoadDataFirstTime = true
    private var dialogProgress: Dialog?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogProgress = DialogUtil.getDialogUtil(requireContext()).createProgressDialog()

    }

    override fun onResume() {
        super.onResume()
        if (isLoadDataFirstTime) {
            isLoadDataFirstTime = false
            DazoneApplication.eventBus
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onEventReceive(it) }
                .addTo(eventDisposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        eventDisposable.clear()
        eventDisposable.dispose()
    }

    override fun onStart() {
        initEvents()
        initViewModels()
        super.onStart()
    }

    abstract fun initEvents()
    abstract fun initViewModels()
    open fun onEventReceive(it: Map<String, Any?>) {

    }

    fun showProgress() {
        if(dialogProgress?.isShowing == false && !requireActivity().isFinishing) {
            dialogProgress?.show()
            handlerTimeOutProgress.postDelayed(mRunnable, 5000)
        }
    }

    fun hideProgress() {
        if(dialogProgress?.isShowing == true) {
            handlerTimeOutProgress.removeCallbacks(mRunnable)
            dialogProgress?.dismiss()
        }
    }

    fun replaceFragment(fragment: Fragment, resId: Int, strBackStack: String?) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(resId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private var handlerTimeOutProgress = Handler(Looper.getMainLooper())
    private var mRunnable = Runnable {
        if(dialogProgress?.isShowing == true) {
            dialogProgress?.dismiss()
        }
    }
}