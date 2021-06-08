package com.dazone.crewphoto.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.dazone.crewphoto.R
import com.dazone.crewphoto.databinding.DialogErrorBinding
import com.dazone.crewphoto.databinding.DialogProgressBinding

open class DialogUtil(private val context: Context) {
    companion object {
        private var instance: DialogUtil?= null
        fun getDialogUtil(context: Context): DialogUtil {
            if(instance == null) {
                instance = DialogUtil(context)
            }

            return instance!!
        }
    }


    fun createConfirmDialog(positiveClick: () -> Unit, title: String, content: String): Dialog {
        var dialog = Dialog(context)
        val binding = DialogErrorBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        binding.tvTitle.text = title
        binding.tvContent.text = content

        binding.tvYes.visibility = View.VISIBLE
        binding.tvNo.visibility = View.VISIBLE
        binding.vLine2.visibility = View.VISIBLE
        binding.tvOK.visibility = View.GONE

        binding.tvYes.setOnClickListener {
            dialog.dismiss()
            positiveClick.invoke()
        }

        binding.tvNo.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }

    fun createMessageDialog(title: String, content: String): Dialog {
        var dialog = Dialog(context)
        val binding = DialogErrorBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        binding.tvTitle.text = title
        binding.tvContent.text = content


        binding.tvYes.visibility = View.GONE
        binding.tvNo.visibility = View.GONE
        binding.vLine2.visibility = View.GONE
        binding.tvOK.visibility = View.VISIBLE

        binding.tvOK.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    fun createProgressDialog(): Dialog {
        var dialog = Dialog(context)
        val binding = DialogProgressBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        return dialog
    }
}