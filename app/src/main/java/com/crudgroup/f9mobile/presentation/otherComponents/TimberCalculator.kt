package com.crudgroup.f9mobile.presentation.otherComponents

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener


class TimberCalculator {

    @SuppressLint("SetTextI18n")
    fun calculator(diameterInput : EditText, lengthInput : EditText, resultVolume : EditText, calculator : Boolean) {
        var resultText = ""
        if (calculator) resultText = "KUB  :  "

        diameterInput.addTextChangedListener {
            try {
                val diameter = diameterInput.text.toString().toDouble()
                val length = lengthInput.text.toString().toDouble()
                if (!diameter.isNaN() && diameter > 0 && !length.isNaN() && length > 0) {
                    resultVolume.setText(resultText + TimberCalculator().oneSidedTimberCalculate(diameter, length))
                }
            }
            catch (_: java.lang.NumberFormatException) { }
        }

        lengthInput.addTextChangedListener {
            try {
                val diameter = diameterInput.text.toString().toDouble()
                val length = lengthInput.text.toString().toDouble()
                if (!diameter.isNaN() && diameter > 0 && !length.isNaN() && length > 0) {
                    resultVolume.setText(resultText + TimberCalculator().oneSidedTimberCalculate(diameter, length))
                }
            }
            catch (_: java.lang.NumberFormatException) { }
        }
    }

    private fun oneSidedTimberCalculate(diameter : Double, length : Double) : String {
        val result = diameter * diameter * length / 2304
        return MyNumberFormat.formattedVolume(result)
    }

    private fun twoSidedTimberCalculate(diameterOne : Double, diameterTwo : Double, length : Double) : String {
        val result = diameterOne * diameterTwo * length / 2304
        return MyNumberFormat.formattedVolume(result)
    }
}