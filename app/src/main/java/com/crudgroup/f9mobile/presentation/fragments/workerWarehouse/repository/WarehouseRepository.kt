package com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.paginAndAndapter.WarehouseCategoryPaging
import com.crudgroup.f9mobile.presentation.otherComponents.ConnectionError
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog

class WarehouseRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    fun getWarehouseCategory(searchText : String, myPlantId : Int, connectionDialog: ConnectionDialog): LiveData<PagingData<WarehouseCategoryModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { WarehouseCategoryPaging(api, searchText,myPlantId, connectionDialog) }
        ).liveData
    }
}