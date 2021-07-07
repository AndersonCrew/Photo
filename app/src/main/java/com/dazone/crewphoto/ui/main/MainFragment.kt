package com.dazone.crewphoto.ui.main

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dazone.crewphoto.BuildConfig
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentMainBinding
import com.dazone.crewphoto.dialog.DialogUtil
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.ui.AllFileFragment
import com.dazone.crewphoto.ui.local_fragment.LocalFragment
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.FileUtils
import com.dazone.crewphoto.utils.PermissionUtil
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainFragment : BaseFragment() {
    private var binding: FragmentMainBinding? = null
    private val REQUEST_CODE_TAKE_CAMERA = 121
    private val REQUEST_CODE_CHOOSE_IMAGE = 123
    private val REQUEST_CODE_PERMISSION_STORAGE = 122
    private val viewModel: MainViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun initEvents() {
        mType = -1
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDark)
        binding?.imgMenu?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }

        binding?.imgCamera?.setOnClickListener {
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

        binding?.cardChooseImage?.setOnClickListener {
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

        binding?.tvLocalGallery?.setOnClickListener {
            changeFragment(0)
        }

        binding?.cardLocalGallery?.setOnClickListener {
            changeFragment(0)
        }

        binding?.tvCrewPhoto?.setOnClickListener {
            changeFragment(1)
        }

        binding?.cardCrewPhoto?.setOnClickListener {
            changeFragment(1)
        }

        changeFragment(1)
    }

    private fun chooseImage() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(pickPhoto, REQUEST_CODE_CHOOSE_IMAGE)
    }

    override fun initViewModels() {
        viewModel.getUserLiveData()?.observe(this, Observer {
            it?.let {
                binding?.tvName?.text = it.name?: ""
                binding?.tvPosition?.text = it.companyName?: ""

                binding?.imgAvatar?.let { image ->
                    Glide
                        .with(this)
                        .load(DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + it.avatar)
                        .placeholder(R.drawable.avatar_default)
                        .into(image)
                }
            }
        })
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
                var files : ArrayList<File> = arrayListOf()
                val imageBitmap = data.extras?.get("data") as Bitmap
                FileUtils(requireContext()).bitmapToFile(imageBitmap, "CrewPhoto_${System.currentTimeMillis()}.jpg")?.let {
                    files.add(it)
                    Event.goToImageShowCapture(files)
                }

            }
        } else if (requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                try {
                    //If uploaded with the new Android Photos gallery
                    var files : ArrayList<File> = arrayListOf()
                    val clipData: ClipData = data.clipData ?: return
                    files.clear()

                    for (i in 0 until clipData.itemCount) {
                        //uri converted to file

                        FileUtils(requireContext()).convertImageUriToFile(clipData.getItemAt(i).uri, requireActivity())?.let {
                            files.add(it)
                        }

                    }

                    Event.goToImageShow(files)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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

    private var mType: Int = 0
    private fun changeFragment(type: Int) {
        binding?.drawerLayout?.closeDrawer(Gravity.LEFT)
        if(mType == type) {
            return
        } else {
            mType = type
        }

        val fragment : Fragment?
        when(type) {
            0 -> {
                fragment = LocalFragment()
            }

            else -> {
                fragment = AllFileFragment()
            }
        }

        replaceFragment(fragment, R.id.frShow, fragment::class.java.simpleName)
    }
}