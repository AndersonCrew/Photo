package com.dazone.crewphoto.ui.date_file

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.ItemDateFileBinding
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.model.DateFile
import com.dazone.crewphoto.utils.Constants


class DateFileAdapter(private val list: ArrayList<DateFile>): RecyclerView.Adapter<DateFileAdapter.DateFileViewHolder>(){
    class DateFileViewHolder(private val binding: ItemDateFileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DateFile) {
            binding.tvDate.text = data.date

            val domain = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "")
            Glide
                .with(binding.root.context)
                .load(domain + data.list[0].url)
                .centerCrop()
                .placeholder(R.drawable.notfound)
                .into(binding.imgImage)

            if(data.list.size > 1) {
                binding.tvCount.visibility = View.VISIBLE
                binding.tvCount.text = "+${data.list.size}"
            } else {
                binding.tvCount.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateFileViewHolder {
        val binding = ItemDateFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val params = binding.root.layoutParams
        val size = parent.measuredWidth / 3
        params.width = size
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        binding.root.layoutParams = params
        return DateFileViewHolder((binding))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DateFileViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            Event.goToListDate(list[position].list)
        }
    }

    fun updateList(mList: List<DateFile>) {
        list.clear()
        list.addAll(mList)
        notifyDataSetChanged()
    }
}