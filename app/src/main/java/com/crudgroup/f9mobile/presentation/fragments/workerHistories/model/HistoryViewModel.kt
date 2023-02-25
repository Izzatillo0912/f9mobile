package com.crudgroup.f9mobile.presentation.fragments.workerHistories.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.repository.HistoryRepository

class HistoryViewModel : ViewModel() {

    private val historyRepository = HistoryRepository()

    fun getSupplyHistory(supplierId : Int, fromDate : String, toDate : String,searchText : String): LiveData<PagingData<GetSupplyHistoryModel>> {
        return historyRepository.getSupplyHistory(supplierId, fromDate, toDate, searchText)
    }
}