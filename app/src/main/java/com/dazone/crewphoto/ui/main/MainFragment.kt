package com.dazone.crewphoto.ui.main

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dazone.crewphoto.BuildConfig
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseActivity
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentMainBinding
import com.dazone.crewphoto.dialog.DialogUtil
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.PermissionUtil
import com.dazone.crewphoto.utils.Utils
import com.google.gson.JsonObject

class MainFragment : BaseFragment() {
    private var binding: FragmentMainBinding? = null
    private val REQUEST_CODE_TAKE_CAMERA = 121
    private val REQUEST_CODE_CHOOSE_IMAGE = 123
    private val REQUEST_CODE_PERMISSION_STORAGE = 122
    private val viewModel: MainViewModel by viewModels ()
    private lateinit var adapter: ImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun initEvents() {
        binding?.imgMenu?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }

        binding?.cardCamera?.setOnClickListener {
            if (!PermissionUtil.checkPermissions(
                    requireContext(),
                    PermissionUtil.permissionsStorage
                )
            ) {
                PermissionUtil.requestPermissions(
                    requireActivity(),
                    REQUEST_CODE_TAKE_CAMERA,
                    PermissionUtil.permissionsStorage
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        binding?.tvLogout?.setOnClickListener {
            DazoneApplication.getInstance().mPref?.logout()
            Event.goToLogin()
        }

        binding?.tvAbout?.setOnClickListener {
            val message = "Dazone CrewPhoto Version: ${BuildConfig.VERSION_NAME}"
           DialogUtil.getDialogUtil(requireContext()).createMessageDialog(resources.getString(R.string.message), message).show()
        }

        binding?.imgChoose?.setOnClickListener {
            if (!PermissionUtil.checkPermissions(
                    requireContext(),
                    PermissionUtil.permissionsStorage
                )
            ) {
                PermissionUtil.requestPermissions(
                    requireActivity(),
                    REQUEST_CODE_CHOOSE_IMAGE,
                    PermissionUtil.permissionsStorage
                )
            } else {
                chooseImage()
            }
        }

        setUpRecyclerView()
        Event.getAllFile()
    }

    private fun chooseImage() {

    }

    private fun getAllFile() {
        showProgress(requireActivity() as BaseActivity)
        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        viewModel.getAllFile(params)
    }

    override fun initViewModels() {
        viewModel.allFileMutableLiveData.observe(this, androidx.lifecycle.Observer {
            hideProgress(requireActivity() as BaseActivity)
            it?.let {
                adapter.updateList(it)
            }
        })

        viewModel.getUserLiveData()?.observe(this, Observer {
            it?.let {
                binding?.tvName?.text = it.name?: ""
                binding?.tvPosition?.text = it.companyName?: ""
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = ImageAdapter(arrayListOf())
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_CAMERA)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TAKE_CAMERA && resultCode == Activity.RESULT_OK) {
            data?.let {
                val imageBitmap = data.extras?.get("data") as Bitmap
                Event.goToImageShow(imageBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
            if (PermissionUtil.checkPermissions(
                    requireContext(),
                    PermissionUtil.permissionsStorage
                )
            ) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.GET_ALL_FILE]?.let {
            getAllFile()
        }
    }
}