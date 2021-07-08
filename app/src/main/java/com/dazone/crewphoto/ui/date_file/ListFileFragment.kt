package com.dazone.crewphoto.ui.date_file

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentListFileBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.ui.crew_photo.CrewPhotoViewModel
import com.dazone.crewphoto.ui.main.ImageAdapter
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.SpacesItemDecoration
import com.google.gson.JsonObject
import java.text.SimpleDateFormat

class ListFileFragment(private val list: ArrayList<FileModel>?): BaseFragment() {
    private val viewModel: CrewPhotoViewModel by viewModels ()
    private lateinit var adapter: ImageAdapter
    private var binding: FragmentListFileBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListFileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun initEvents() {
        setUpRecyclerView()
        if(list.isNullOrEmpty()) {
            binding?.csHeader?.visibility = View.GONE
            getAllFile()
        } else {
            binding?.csHeader?.visibility = View.VISIBLE
            binding?.tvTitle?.text = SimpleDateFormat("dd-MM-yyyy").format(list[0].time)
            adapter.updateList(list.sortedBy { it.time })
        }

        binding?.imgBack?.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun initViewModels() {
        viewModel.allFileImagesMutableLiveData.observe(this, androidx.lifecycle.Observer {
            Log.d("CrewPhotoFragment", "allFileMutableLiveData")
            hideProgress()
            it?.let { it ->
                adapter.updateList(it.sortedBy { it.time })
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = ImageAdapter(arrayListOf())
        binding?.rvImages?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvImages?.adapter = adapter
    }

    private fun getAllFile() {
        showProgress()
        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        viewModel.getAllFile(params)
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)
        Log.d("CrewPhotoFragment", "onEventReceive")
        it[Event.GET_ALL_FILE]?.let {
            getAllFile()
            Log.d("CrewPhotoFragment", "onEventReceive GET_ALL_FILE")
        }
    }
}