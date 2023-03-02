package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.SupplyCreateModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.repository.OrdersRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OrdersViewModel : ViewModel() {

    private val repository = OrdersRepository()

    val productionCreateLiveDate = MutableLiveData<ApiResult<Any>>()

    fun getProductionOrders(searchText : String, plantId : Int): LiveData<PagingData<ProductionOrdersModel>> {
        return repository.getProductionOrders(searchText, plantId)
    }

    fun getProductionOrderInfo(searchText : String, cycleId : Int): LiveData<PagingData<ProductionOrderInfoModel>> {
        return repository.getProductionOrderInfo(searchText, cycleId)
    }

    fun productionCreate(productionCreateModel: ProductionCreateModel) {
        viewModelScope.launch {
            try {
                val response = repository.productionCreate(productionCreateModel)
                if (response.code() == 200) productionCreateLiveDate.postValue(ApiResult.success(response.body()!!.detail))
                else productionCreateLiveDate.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                productionCreateLiveDate.postValue(ApiResult.error(t))
            }
        }
    }
}