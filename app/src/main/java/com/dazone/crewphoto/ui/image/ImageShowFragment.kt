package com.dazone.crewphoto.ui.image

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseActivity
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.databinding.FragmentImageShowBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageShowFragment(private val bitmap: Bitmap, private val isDetail: Boolean): BaseFragment() {
    private var binding: FragmentImageShowBinding?= null
    private val viewModel: ImageViewModel by viewModels ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageShowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun initEvents() {
        binding?.imgBack?.setOnClickListener { requireActivity().onBackPressed() }


        binding?.imgShow?.let {
            Glide
                .with(this)
                .load(bitmap)
                .centerCrop()
                .placeholder(R.drawable.notfound)
                .into(it)
        }

       /* binding?.btnUpload?.setOnClickListener {
            showProgress(requireActivity() as BaseActivity)
            val fileName = "Dazone CrewPhoto" + System.currentTimeMillis().toString()
            Utils.bitmapToFile(bitmap, fileName)?.let {
                viewModel.uploadFile(it)
            }
        }

        binding?.btnSave?.setOnClickListener {
            showProgress(requireActivity() as BaseActivity)
            saveBitmap()
        }

        binding?.btnSaveDetail?.setOnClickListener {
            showProgress(requireActivity() as BaseActivity)
            saveBitmap()
        }*/
    }

    private fun saveBitmap() {
        uiScope.launch {
            val path: String = Environment.getExternalStorageDirectory().toString()
            var fOut: OutputStream? = null
            val file = File(path, "CrewPhoto_${System.currentTimeMillis()}.jpg")
            fOut = FileOutputStream(file)

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                85,
                fOut
            ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

            fOut.flush() // Not really required

            fOut.close() // do not forget to close the stream


            MediaStore.Images.Media.insertImage(
                context?.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Save file successfully!", Toast.LENGTH_LONG).show()
                hideProgress(requireActivity() as BaseActivity)
            }
        }

    }

    override fun initViewModels() {
        viewModel.fileMutableLiveData.observe(this, Observer {
            it?.let {
                Toast.makeText(requireContext(), "Upload Successfully!", Toast.LENGTH_LONG).show()
                Event.getAllFile()
                requireActivity().onBackPressed()
            }
        })
    }
}