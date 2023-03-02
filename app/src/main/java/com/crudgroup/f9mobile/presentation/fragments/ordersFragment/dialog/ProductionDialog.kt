package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.crudgroup.f9mobile.databinding.DialogProductionBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.ProductionAdapter

class ProductionDialog(val context: Context){

    var dialog : AlertDialog? = null
    private lateinit var binding: DialogProductionBinding

    private fun createDialog() {
        dialog = AlertDialog.Builder(context).create()
        binding = DialogProductionBinding.inflate(LayoutInflater.from(context))
        dialog?.setView(binding.root)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()
    }

    fun showDialog(productionAdapter: ProductionAdapter, title : String) {
        dismissDialog()
        if (dialog == null) {
            createDialog()
            binding.dialogProductionTitle.text = title
            binding.detailProductionRv.adapter = productionAdapter
            binding.closeDialog.setOnClickListener { dismissDialog()  }
        }
    }

    fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }
}