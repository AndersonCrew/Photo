package com.dazone.crewphoto.base


import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dazone.crewphoto.R
import com.dazone.crewphoto.dialog.DialogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseActivity : AppCompatActivity() {

    var eventDisposable: CompositeDisposable = CompositeDisposable()
    private var dialogProgress: Dialog?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        DazoneApplication.eventBus
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onEventReceive(it) }
            .addTo(eventDisposable)

        dialogProgress = DialogUtil.getDialogUtil(this).createProgressDialog()

        initEvents()
        initViewModels()
    }

    abstract fun initEvents()
    abstract fun initViewModels()
    open fun onEventReceive(it: Map<String, Any?>) {}

    fun addFragment(resId: Int, fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .add(resId, fragment)
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    fun replaceFragment(resId: Int, fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(resId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showProgress() {
        if(dialogProgress?.isShowing == false) {
            dialogProgress?.show()
        }
    }

    fun hideProgress() {
        if(dialogProgress?.isShowing == true) {
            dialogProgress?.dismiss()
        }
    }
}