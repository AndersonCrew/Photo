package com.dazone.crewphoto.ui.main

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.ItemFileBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.utils.Constants
import java.text.SimpleDateFormat
import java.util.*


class ImageAdapter(private val list: ArrayList<FileModel>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(val binding: ItemFileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(fileModel: FileModel) {
            val domain = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "")
            Glide
                .with(binding.root.context)
                .load(domain + fileModel.url)
                .centerCrop()
                .placeholder(R.drawable.notfound)
                .into(binding.imgFile)

            binding.tvName.text = fileModel.name?: "-"
            binding.tvTime.text = SimpleDateFormat("dd-MM-yyyy hh:mm").format(Date(fileModel.time))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val params = binding.root.layoutParams
        val size = parent.measuredWidth / 3
        params.width = size
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        binding.root.layoutParams = params

        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       holder.bindView(list[position])
        holder.itemView.setOnClickListener {
            Event.goToDetailImage(list[position], list)
        }
    }

    fun updateList(mList: List<FileModel>) {
        list.clear()
        list.addAll(mList)
        notifyDataSetChanged()
    }
}