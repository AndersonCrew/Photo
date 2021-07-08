package com.dazone.crewphoto.ui.crew_photo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseActivity
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentCrewphotoBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.ui.date_file.DateFileAdapter
import com.dazone.crewphoto.ui.main.ImageAdapter
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.SpacesItemDecoration
import com.google.gson.JsonObject


class CrewPhotoFragment: BaseFragment() {
    private val viewModel: CrewPhotoViewModel by viewModels ()
    private lateinit var adapter: DateFileAdapter
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
        setUpRecyclerView()
        getAllFile()
        Log.d("CrewPhotoFragment", "initEvents")
    }

    override fun initViewModels() {
        viewModel.allFileMutableLiveData.observe(this, androidx.lifecycle.Observer {
            Log.d("CrewPhotoFragment", "allFileMutableLiveData")
            hideProgress()
            it?.let {
                adapter.updateList(it.sortedBy { it.time })
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = DateFileAdapter(arrayListOf())
        binding?.rvCrewPhoto?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvCrewPhoto?.adapter = adapter
        val spacingInPixels: Int = resources.getDimensionPixelSize(R.dimen.space_item)
        binding?.rvCrewPhoto?.addItemDecoration(SpacesItemDecoration(spacingInPixels))
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