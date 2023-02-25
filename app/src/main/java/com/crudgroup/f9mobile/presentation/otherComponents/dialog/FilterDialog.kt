package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import com.crudgroup.f9mobile.databinding.DalogFilterBinding

class FilterDialog(fragment : Fragment) {

    private var filterDialog = Dialog(fragment.requireContext())
    private var filterBinding = DalogFilterBinding.inflate(filterDialog.layoutInflater)

    init {
        filterDialog.setContentView(filterBinding.root)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showDialog() {
        filterDialog.show()

        filterBinding.cleanFilterBtn.setOnClickListener {
            cleanFilter()
        }

        filterBinding.filterChangeBtn.setOnClickListener {
            setFilter()
        }
    }

    private fun dismissDialog() {
        filterDialog.dismiss()
    }

    private fun cleanFilter() {
        filterBinding.moneyFilterGroup.clearCheck()
        filterBinding.submitDateGroup.clearCheck()
        dismissDialog()
    }

    private fun setFilter() {
        dismissDialog()
    }

}