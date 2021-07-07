package com.dazone.crewphoto.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazone.crewphoto.base.BaseActivity
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentSplashBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.utils.Constants

class SplashFragment : BaseFragment() {
    private var binding: FragmentSplashBinding?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun initEvents() {
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed({
            hideProgress()
            if(DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, "").isNullOrEmpty()) {
                Event.goToLogin()
            } else {
                Event.goToMain()
            }
        }, 2000)

    }

    override fun initViewModels() {

    }
}