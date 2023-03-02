package com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.repository.WarehouseRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ConnectionError
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog

class WarehouseViewModel : ViewModel() {

    private val repository = WarehouseRepository()

    fun getWarehouseCategory(searchText : String, myPlantId : Int, connectionDialog: ConnectionDialog): LiveData<PagingData<WarehouseCategoryModel>> {
        return repository.getWarehouseCategory(searchText, myPlantId, connectionDialog)
    }
}