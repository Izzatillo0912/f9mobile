package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.crudgroup.f9mobile.databinding.DalogFilterBinding
import com.crudgroup.f9mobile.databinding.DialogLoadingAndRequestResultBinding

class FilterDialog(val fragment : Fragment) {

    private var filterDialog : AlertDialog? = null
    private lateinit var filterBinding : DalogFilterBinding

    private fun createDialog() {
        filterDialog = AlertDialog.Builder(fragment.requireContext()).create()
        filterBinding = DalogFilterBinding.inflate(LayoutInflater.from(fragment.requireContext()))
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