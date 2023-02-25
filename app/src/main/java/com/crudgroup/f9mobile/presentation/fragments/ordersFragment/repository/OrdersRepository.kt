package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.PlantCyclesModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.PlantCyclesPagination
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class OrdersRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    fun getPlantCycles(searchText : String, plantId : Int): LiveData<PagingData<PlantCyclesModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { PlantCyclesPagination(api, searchText, plantId) }
        ).liveData
    }
}