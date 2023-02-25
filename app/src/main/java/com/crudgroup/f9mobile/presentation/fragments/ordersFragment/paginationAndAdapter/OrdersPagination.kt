package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import retrofit2.HttpException

class OrdersPagination(private val f9Api: F9Api) : PagingSource<Int, OrdersModel>() {

    override fun getRefreshKey(state: PagingState<Int, OrdersModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrdersModel> {
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response = f9Api.getOrders(pageNumber, 10)

            if (response.isSuccessful) {
                val orders = response.body()!!
                val nextPageNumber = if (orders.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(orders.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            LoadResult.Error(t)
        }
    }
}