package com.dazone.crewphoto

import android.graphics.Bitmap
import android.os.Bundle
import com.dazone.crewphoto.base.BaseActivity
import com.dazone.crewphoto.databinding.ActivityMainBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.ui.date_file.ListFileFragment
import com.dazone.crewphoto.ui.image.ImageShowFragment
import com.dazone.crewphoto.ui.image.ImageSlideFragment
import com.dazone.crewphoto.ui.main.MainFragment
import com.dazone.crewphoto.ui.splash.SplashFragment
import com.dazone.crewphoto.ui.login.LoginFragment
import java.io.File

@Suppress("UNCHECKED_CAST")
class MainActivity : BaseActivity() {
    private var binding: ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun initEvents() {
        replaceFragment(R.id.frMain, SplashFragment(), null)
        supportFragmentManager
    }

    override fun initViewModels() {

    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.GOTO_LOGIN]?.let {
            replaceFragment(R.id.frMain, LoginFragment(), null)
        }

        it[Event.GOTO_MAIN]?.let {
            replaceFragment(R.id.frMain, MainFragment(), null)
        }

        it[Event.GOTO_IMAGE_DETAIL]?.let {
            replaceFragment(R.id.frMain, ImageSlideFragment(it as ArrayList<File>, false, null), null)
        }

        it[Event.GOTO_IMAGE_CAP]?.let {
            replaceFragment(R.id.frMain, ImageSlideFragment(it as ArrayList<File>, true, null), null)
        }

        it[Event.GOTO_DETAIL]?.let {
            replaceFragment(R.id.frMain, ImageSlideFragment(null, false, it as ArrayList<FileModel>), null)
        }

        it[Event.GOTO_LIST_DATE]?.let {
            replaceFragment(R.id.frMain, ListFileFragment(it as ArrayList<FileModel>), null)
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.frMain) is LoginFragment || supportFragmentManager.findFragmentById(R.id.frMain) is MainFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}