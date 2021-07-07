package com.dazone.crewphoto.ui.date_file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentDateFileBinding
import com.dazone.crewphoto.model.DateFile

class DateFileFragment(private val list: List<DateFile>): BaseFragment() {
    private var binding: FragmentDateFileBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDateFileBinding.inflate(inflater, container, false)

        return binding?.root
    }
    override fun initEvents() {

    }

    override fun initViewModels() {

    }
}