package com.crudgroup.f9mobile.presentation.otherComponents

import java.text.DecimalFormat
import java.text.NumberFormat

object MyNumberFormat {

    fun formattedVolume(volume : Double) : String{
        val formatter: NumberFormat = DecimalFormat("#,###.##")
        return formatter.format(volume)
    }
}