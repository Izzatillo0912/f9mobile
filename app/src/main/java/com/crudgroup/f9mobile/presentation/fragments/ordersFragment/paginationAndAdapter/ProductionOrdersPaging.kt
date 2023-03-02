package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import retrofit2.HttpException

class ProductionOrdersPaging(private val f9Api: F9Api, private val searchText : String,
                             private val plantId : Int) : PagingSource<Int, ProductionOrdersModel>() {

    override fun getRefreshKey(state: PagingState<Int, ProductionOrdersModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductionOrdersModel> {
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response = f9Api.getProductionOrders(pageNumber, 10, searchText)

            if (response.isSuccessful) {
                val orders = response.body()!!
                Log.e("PlantCycles", "load: ${response.code()}  ${response.body()!!.data.size}", )
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