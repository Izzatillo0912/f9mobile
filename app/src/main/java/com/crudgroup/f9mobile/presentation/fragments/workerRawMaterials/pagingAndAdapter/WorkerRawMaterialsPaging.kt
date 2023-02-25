package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.orhanobut.hawk.Hawk
import retrofit2.HttpException

class WorkerRawMaterialsPaging(private val f9Api: F9Api, private val categoryId : Int,
                               private val searchText : String) : PagingSource<Int, MaterialStoresModel>() {

    override fun getRefreshKey(state: PagingState<Int, MaterialStoresModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MaterialStoresModel> {
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize
            val response = f9Api.getWorkerRawMaterials(searchText, categoryId, Hawk.get("my_plant_id"), pageNumber, 10)

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