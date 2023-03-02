package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.DialogImageViewBinding
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class ImageViewDialog(context: Context) {

    private var dialog = Dialog(context)
    private lateinit var binding : DialogImageViewBinding

    fun activeDialog() {
        binding = DialogImageViewBinding.inflate(LayoutInflater.from(dialog.context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun showImage(image : String) {

        dialog.show()

        if (!image.isNullOrEmpty()) Glide
            .with(binding.root.context)
            .load(image)
            .centerCrop()
            .into(binding.image)

        binding.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

}