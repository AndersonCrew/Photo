package com.dazone.crewphoto.ui.image

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentImageSlideBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.FileModel
import kotlinx.coroutines.launch
import java.io.File


class ImageSlideFragment(var files: ArrayList<File>?, private val file: FileModel?, private val isCapture: Boolean) : BaseFragment() {
    private var binding: FragmentImageSlideBinding?= null
    private val viewModel: ImageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageSlideBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun initEvents() {
        binding?.imgBack?.setOnClickListener { requireActivity().onBackPressed() }

        binding?.imgDone?.setOnClickListener {
            showProgress()
            if(isCapture) {
                saveImage()
            }

            if(!files.isNullOrEmpty() && file == null) {
                sendMultipleImage()
            }
        }

        /*SetUp ViewPager*/
        if(files.isNullOrEmpty() && file != null) {
            val adapter = MyPagerAdapter(requireActivity().supportFragmentManager, arrayListOf(), file)
            binding?.vpImage?.adapter = adapter
            binding?.header?.visibility = View.GONE
        } else {
            val adapter = MyPagerAdapter(requireActivity().supportFragmentManager, files!!, null)
            binding?.vpImage?.adapter = adapter
        }
    }

    private fun saveImage() {
        uiScope.launch {
            val file = files?.get(0)?: return@launch

            MediaStore.Images.Media.insertImage(
                context?.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )
        }
    }

    private fun sendMultipleImage() {
        showProgress()
        viewModel.uploadFile(files!!)
    }

    override fun initViewModels() {
        viewModel.fileMutableLiveData.observe(requireActivity(), Observer {
            it?.let {
                hideProgress()
                Toast.makeText(requireContext(), "Upload Successfully!", Toast.LENGTH_LONG).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    Event.getAllFile()
                    requireActivity().onBackPressed()
                }, 500)
            }
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            hideProgress()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    class MyPagerAdapter(fragmentManager: FragmentManager, private val files: ArrayList<File>, private val file: FileModel?) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        // Returns total number of pages
        override fun getCount(): Int {
            return if(files.isNullOrEmpty()) 1 else files.size
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment {
            return when (files.size) {
                0 -> ImageShowFragment(null, true, file)
                 else -> ImageShowFragment(files[position], false, null)
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.DELETE_IMAGE]?.let {
            var file = it as File
            if(files.isNullOrEmpty()) {
                requireActivity().onBackPressed()
            } else {
                if(files?.contains(file) == true && files?.size?: 0 > 1) {
                    files?.remove(file)

                    val adapter = MyPagerAdapter(requireActivity().supportFragmentManager, files!!, null)
                    binding?.vpImage?.adapter = adapter
                } else {
                    requireActivity().onBackPressed()
                }
            }
        }
    }
}