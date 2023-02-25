package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.repository

import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.otherComponents.model.PaginationModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.ResponseDetail
import retrofit2.Response

class GetSupplyRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    suspend fun getRawMaterialsList(materialTypeId : Int) : Response<PaginationModel<SupplyMaterialsModel>> {
        return api.getSupplyMaterials(materialTypeId)
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