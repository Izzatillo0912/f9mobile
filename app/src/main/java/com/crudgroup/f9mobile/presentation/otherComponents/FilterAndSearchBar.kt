package com.crudgroup.f9mobile.presentation.otherComponents

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.crudgroup.f9mobile.databinding.FragmentAllHistoriesBinding
import com.crudgroup.f9mobile.databinding.FragmentGetSupplyHistoryBinding
import com.crudgroup.f9mobile.databinding.FragmentOrdersBinding
import com.crudgroup.f9mobile.databinding.FragmentWorkerRawMaterialsBinding
import com.crudgroup.f9mobile.databinding.FragmentWorkerWarehouseBinding
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.FilterDialog

class FilterAndSearchBar(val fragment: Fragment) {

    fun activeOrdersFragmentFilterAndSearchBar(binding: FragmentOrdersBinding) {
        binding.appBar.filterBtn.setOnClickListener {
            val filterDialog = FilterDialog(fragment)
            filterDialog.showDialog()
        }
    }

    fun activeWarehouseFragmentFilterAndSearchBar(binding: FragmentWorkerWarehouseBinding) {

        binding.appBar.filterBtn.setOnClickListener {

        }
    }

    fun activeHistoryFragmentFilterAndSearchBar(binding: FragmentAllHistoriesBinding) {

        binding.appBar.backPageBtn.visibility = View.GONE

        binding.appBar.filterBtn.setOnClickListener {
            val filterDialog = FilterDialog(fragment)
            filterDialog.showDialog()
        }
    }

    fun activeRawMaterialFragmentFilterAndSearchBar(binding: FragmentWorkerRawMaterialsBinding, pageTitle : String) {
        binding.appBar.backPageBtn.visibility = View.VISIBLE
        binding.appBar.pageTitle.text = pageTitle

        binding.appBar.backPageBtn.setOnClickListener {
            fragment.findNavController().popBackStack()
        }

        binding.appBar.filterBtn.setOnClickListener {
            val filterDialog = FilterDialog(fragment)
            filterDialog.showDialog()
        }
    }

    fun activeSupplyHistoryFragmentSearchBar(binding: FragmentGetSupplyHistoryBinding, pageTitle : String) {
        binding.appBar.backPageBtn.visibility = View.VISIBLE
        binding.appBar.pageTitle.text = pageTitle

        binding.appBar.backPageBtn.setOnClickListener {
            fragment.findNavController().popBackStack()
        }
    }
}