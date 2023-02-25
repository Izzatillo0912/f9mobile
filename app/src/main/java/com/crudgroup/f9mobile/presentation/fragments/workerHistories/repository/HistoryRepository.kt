package com.crudgroup.f9mobile.presentation.fragments.workerHistories.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.GetSupplyHistoryModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.pagingAndAdapter.GetSupplyHistoryPaging
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class HistoryRepository {


    private val api: F9Api by lazy { RetrofitInstance.api }

    fun getSupplyHistory(supplierId : Int, fromDate : String,
                         toDate : String,searchText : String): LiveData<PagingData<GetSupplyHistoryModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { GetSupplyHistoryPaging(api, supplierId, fromDate, toDate, searchText) }
        ).liveData
    }

}