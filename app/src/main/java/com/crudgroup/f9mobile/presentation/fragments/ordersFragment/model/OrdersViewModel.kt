package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.repository.OrdersRepository

class OrdersViewModel : ViewModel() {

    private val repository = OrdersRepository()

    fun getOrdersList(): LiveData<PagingData<OrdersModel>> {
        return repository.getWarehouseOrders()
    }

}