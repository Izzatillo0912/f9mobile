package com.crudgroup.f9mobile.presentation.fragments.workerHistories.pagingAndAdapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.GetSupplyHistoryModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.RawMaterialModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.orhanobut.hawk.Hawk
import retrofit2.HttpException

class GetSupplyHistoryPaging(private val f9Api: F9Api, private val supplierId : Int,
                             private val fromDate : String, private val toDate : String,
                             private val searchText : String) : PagingSource<Int, GetSupplyHistoryModel>() {

    override fun getRefreshKey(state: PagingState<Int, GetSupplyHistoryModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetSupplyHistoryModel> {
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response = f9Api.getSupplyHistory(0, Hawk.get("my_plant_id"), searchText, pageNumber, 10)

            if (response.isSuccessful) {
                val warehouseCategory = response.body()!!
                Log.e(Constants.WFTAG, "PAGING LOAD BODY SIZE : ${warehouseCategory.data.size}", )
                val nextPageNumber = if (warehouseCategory.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(warehouseCategory.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            LoadResult.Error(t)
        }
    }
}