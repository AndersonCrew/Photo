package com.dazone.crewphoto.ui.crew_photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentCrewphotoBinding

class CrewPhotoFragment: BaseFragment() {
    private var binding: FragmentCrewphotoBinding?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCrewphotoBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun initEvents() {

    }

    override fun initViewModels() {

    }
}