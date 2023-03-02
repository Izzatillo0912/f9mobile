package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionCreateModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrderInfoModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.ProductionOrderInfoPaging
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.ProductionOrdersPaging
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.model.ResponseDetail
import retrofit2.Response

class OrdersRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    fun getProductionOrders(searchText : String, plantId : Int): LiveData<PagingData<ProductionOrdersModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { ProductionOrdersPaging(api, searchText, plantId) }
        ).liveData
    }

    fun getProductionOrderInfo(searchText : String, cycleId : Int): LiveData<PagingData<ProductionOrderInfoModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { ProductionOrderInfoPaging(api, searchText, cycleId) }
        ).liveData
    }

    suspend fun productionCreate(productionCreateModel: ProductionCreateModel) : Response<ResponseDetail> {
        return api.productionCreate(productionCreateModel)
    }
}