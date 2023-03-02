package com.crudgroup.f9mobile.presentation.otherComponents

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*

class DateTimePicker(val context: Context) {

    private val calendar = Calendar.getInstance()
    private var year = calendar[Calendar.YEAR]
    private var month = calendar[Calendar.MONTH]
    private var day = calendar[Calendar.DAY_OF_MONTH]

    fun openDatePicker(date: TextView) {

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener(object : DatePickerDialog.OnDateSetListener,
                (DatePicker, Int, Int, Int) -> Unit {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SetTextI18n")
            override fun invoke(p1: DatePicker, year1: Int, month1: Int, day1: Int) {
                if (month1.toString().length < 2 && day1.toString().length < 2) {
                    date.text = "$year1-0${month1 + 1}-0$day1"
                }
                else if (month1.toString().length < 2 && day1.toString().length > 1){
                    date.text = "$year1-0${month1 + 1}-$day1"
                }
                else if (month1.toString().length > 1 && day1.toString().length < 2){
                    date.text = "$year1-${month1 + 1}-0$day1"
                }

            } }
        ), year, month, day).show()
    }

    fun openTimePicker(textView: TextView) {

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener(object : TimePickerDialog.OnTimeSetListener,
                    (TimePicker, Int, Int) -> Unit {
                override fun onTimeSet(p0: TimePicker?, hour1: Int, minute1: Int) {
                }

                @SuppressLint("SimpleDateFormat")
                override fun invoke(p1: TimePicker, hour1: Int, minute1: Int) {
                    calendar.set(0, 0, 0, hour1, minute1)
                    val time = "$hour1:$minute1"
                    val f24Hours = SimpleDateFormat("HH:mm")
                    try {
                        val date = f24Hours.parse(time)
                        val f12Hours = SimpleDateFormat("HH:mm")
                        textView.text = f12Hours.format(date!!)
                    } catch (e: ExceptionInInitializerError) {
                    }
                }
            }
            ),
            12,
            0,
            true).show()

    }
}