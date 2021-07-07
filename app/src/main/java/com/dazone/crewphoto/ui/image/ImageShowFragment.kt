package com.dazone.crewphoto.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentImageShowBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.Utils
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ImageShowFragment(
    private var file: File?,
    private val isDetail: Boolean,
    private val fileModel: FileModel?
) :
    BaseFragment() {
    private var binding: FragmentImageShowBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageShowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(file != null) {
            bitmap = BitmapFactory.decodeFile(file?.path)
        }

    }

    private lateinit var bitmap: Bitmap
    override fun initEvents() {

        binding?.imgDelete?.setOnClickListener {
           if(file != null) {
               Event.deleteImage(file!!)
           } else {
               requireActivity().onBackPressed()
           }
        }

        binding?.imgRotate?.setOnClickListener {
            val matrix1 = Matrix()
            matrix1.postRotate(90f) // anti-clockwise by 90 degrees

            var rotatedBitmap1: Bitmap? = null

            try {
                rotatedBitmap1 = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix1,
                    true
                )
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // if rotated then save it path = fOut

            // if rotated then save it path = fOut
            if (rotatedBitmap1 != null) {
                bitmap.recycle()
                bitmap = rotatedBitmap1
                binding?.imgShow?.setImageBitmap(bitmap)
            }
        }

        if (isDetail) {
            binding?.csBottom?.visibility = View.GONE

            binding?.imgShow?.let {
                Glide
                    .with(this)
                    .load(
                        DazoneApplication.getInstance().mPref?.getString(
                            Constants.DOMAIN,
                            ""
                        ) + fileModel!!.url
                    )
                    .placeholder(R.drawable.notfound)
                    .into(it)
            }
        } else {
            binding?.imgShow?.let {
                Glide
                    .with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.notfound)
                    .into(it)
            }
        }

        /*Setup Spinner Quality*/

        // (@resource) android.R.layout.simple_spinner_item:
        //   The resource ID for a layout file containing a TextView to use when instantiating views.
        //    (Layout for one ROW of Spinner)

        // (@resource) android.R.layout.simple_spinner_item:
        //   The resource ID for a layout file containing a TextView to use when instantiating views.
        //    (Layout for one ROW of Spinner)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            resources.getStringArray(R.array.filter_image)
        )

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinnerQuality?.adapter = adapter

        // When user select a List-Item.

        // When user select a List-Item.
        binding?.spinnerQuality?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveBitmap() {


    }

    override fun initViewModels() {

    }
}