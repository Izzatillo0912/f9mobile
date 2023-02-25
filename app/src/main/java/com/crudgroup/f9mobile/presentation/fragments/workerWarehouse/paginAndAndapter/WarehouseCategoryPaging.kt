package com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.paginAndAndapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.database.LocaleCaches.categoryList
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.orhanobut.hawk.Hawk
import retrofit2.HttpException

class WarehouseCategoryPaging(private val f9Api: F9Api, private val searchText : String,
                              private val connectionDialog: ConnectionDialog) : PagingSource<Int, WarehouseCategoryModel>() {

    override fun getRefreshKey(state: PagingState<Int, WarehouseCategoryModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WarehouseCategoryModel> {
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response = f9Api.getWarehouseCategory(searchText, pageNumber, 10, Hawk.get("my_plant_id"))

            if (response.isSuccessful) {
                if (response.code() == 500) connectionDialog.showDialog(Constants.GET_CATEGORIES, Constants.IS_NOT_CHECKED, "Malumotlarda xatolik bor !!")

                categoryList = response.body()?.data as ArrayList<WarehouseCategoryModel>
                val warehouseCategory = response.body()!!
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