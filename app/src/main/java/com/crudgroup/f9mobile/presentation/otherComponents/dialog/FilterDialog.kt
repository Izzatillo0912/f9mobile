package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.crudgroup.f9mobile.databinding.DalogFilterBinding
import com.crudgroup.f9mobile.presentation.otherComponents.DateTimePicker

class FilterDialog(val fragment : Fragment, private val filterBtnClicked: FilterBtnClicked) {

    private var filterDialog : AlertDialog? = null
    private lateinit var filterBinding : DalogFilterBinding
    private lateinit var dateTimePicker: DateTimePicker

    interface FilterBtnClicked {
        fun clearFilterClicked()

        fun setFilterClicked(fromDate : String, toDate : String)
    }

    private fun createDialog() {
        filterDialog = AlertDialog.Builder(fragment.requireContext()).create()
        filterBinding = DalogFilterBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        dateTimePicker = DateTimePicker(fragment.requireContext())
        filterDialog?.setView(filterBinding.root)
        filterDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog?.show()
    }

    fun showDialog() {
        if (filterDialog == null) {
            createDialog()

            filterBinding.cleanFilterBtn.setOnClickListener {
                cleanFilter()
            }

            filterBinding.filterChangeBtn.setOnClickListener {
                setFilter()
            }

            filterBinding.filterFromDate.setOnClickListener {
                dateTimePicker.openDatePicker(filterBinding.filterFromDate)
            }

            filterBinding.filterToDate.setOnClickListener {
                dateTimePicker.openDatePicker(filterBinding.filterToDate)
            }

            filterBinding.cleanFilterBtn.setOnClickListener {
                filterBtnClicked.clearFilterClicked()
                dismissDialog()
            }

            filterBinding.filterChangeBtn.setOnClickListener {
                filterBtnClicked.setFilterClicked(filterBinding.filterFromDate.text.toString(), filterBinding.filterToDate.text.toString())
                dismissDialog()
            }
        }
        else {
            dismissDialog()
            showDialog()
        }
    }

    private fun dismissDialog() {
        filterDialog?.dismiss()
        filterDialog = null
    }

    private fun cleanFilter() {
        dismissDialog()
    }

    private fun setFilter() {
        dismissDialog()
    }

}