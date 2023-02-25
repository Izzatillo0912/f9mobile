package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter.WorkerRawMaterialsPaging
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.model.PaginationModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.ResponseDetail
import retrofit2.Response

class WorkerRawMaterialsRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    fun getWorkerRawMaterials(categoryId : Int, searchText : String): LiveData<PagingData<MaterialStoresModel>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = Constants.PAGE_SIZE),
            pagingSourceFactory = { WorkerRawMaterialsPaging(api, categoryId, searchText ) }
        ).liveData
    }

    suspend fun getSuppliers() : Response<PaginationModel<SupplierModel>> {
        return api.getSuppliers()
    }

    suspend fun getPlants() : Response<PaginationModel<PlantModel>> {
        return api.getPlants()
    }

    suspend fun getMaterialTransferPlant(materialId : Int) : Response<List<EnterpriseModel>> {
        return api.getEnterprise(materialId)
    }

    suspend fun supplyCreate(supplyCreateModel: SupplyCreateModel) : Response<ResponseDetail> {
        return api.supplyCreate(supplyCreateModel)
    }

    suspend fun materialTransfer(transferModel: TransferModel) : Response<ResponseDetail> {
        return api.materialTransfer(transferModel)
    }

}