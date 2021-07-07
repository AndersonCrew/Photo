package com.dazone.crewphoto.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentAllFileBinding
import com.dazone.crewphoto.ui.crew_photo.CrewPhotoFragment
import com.dazone.crewphoto.ui.date_file.ListFileFragment

class AllFileFragment: BaseFragment() {
    private var binding: FragmentAllFileBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllFileBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun initEvents() {
        val adapter = MyPagerAdapter(
            requireActivity().supportFragmentManager
        )

        binding?.vpAllFile?.adapter = adapter

        binding?.tvAlbum?.setOnClickListener { changeTab(0) }
        binding?.tvAllImage?.setOnClickListener { changeTab(1) }
    }

    override fun initViewModels() {

    }

    private fun changeTab(tab: Int) {
        if(tab != binding?.vpAllFile?.currentItem?: 0) {
            when(tab) {
                0 -> {
                    binding?.tvAlbum?.alpha = 1f
                    binding?.tvAllImage?.alpha = 0.6f
                }

                else -> {
                    binding?.tvAlbum?.alpha = 0.6f
                    binding?.tvAllImage?.alpha = 1f
                }
            }

            binding?.vpAllFile?.currentItem = tab
        }
    }

    class MyPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        // Returns total number of pages
        override fun getCount(): Int {
            return 2
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CrewPhotoFragment()
                else -> ListFileFragment()
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }
    }

}

