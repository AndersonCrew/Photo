package com.dazone.crewphoto.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.ItemFileBinding
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.utils.Constants

class ImageAdapter(private val list: ArrayList<FileModel>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemFileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(fileModel: FileModel) {
            val domain = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "")
            Glide
                .with(binding.root.context)
                .load(domain + fileModel.url)
                .centerCrop()
                .placeholder(R.drawable.notfound)
                .into(binding.imgFile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val params = binding.root.layoutParams
        val size = parent.measuredWidth / 3
        params.width = size
        params.height = size * 5 / 4
        binding.root.layoutParams = params

        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       holder.bindView(list[position])
    }

    fun updateList(mList: ArrayList<FileModel>) {
        list.clear()
        list.addAll(mList)
        notifyDataSetChanged()
    }
}