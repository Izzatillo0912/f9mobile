package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.crudgroup.f9mobile.databinding.DialogCalculatorBinding
import com.crudgroup.f9mobile.presentation.otherComponents.TimberCalculator

class TimberCalculatorDialog(val context: Context) {

    private val timberDialog = AlertDialog.Builder(context).create()
    private var binding: DialogCalculatorBinding = DialogCalculatorBinding.inflate(LayoutInflater.from(timberDialog.context))


    init {
        timberDialog.setView(binding.root)
        timberDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showDialog() {
        timberDialog.show()

        TimberCalculator().calculator(binding.inputDiameter, binding.inputLength, binding.calculatorDialogResult,false)

        binding.calculatorDialogCloseBtn.setOnClickListener {
            timberDialog.dismiss()
        }
    }

}