package com.dazone.crewphoto.ui.local_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentLocalGalleryBinding

class LocalFragment: BaseFragment() {
    private var binding: FragmentLocalGalleryBinding?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalGalleryBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun initEvents() {

    }

    override fun initViewModels() {

    }
}