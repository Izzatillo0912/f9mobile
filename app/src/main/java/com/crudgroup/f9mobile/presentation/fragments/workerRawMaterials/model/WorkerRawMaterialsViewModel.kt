package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.repository.WorkerRawMaterialsRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WorkerRawMaterialsViewModel : ViewModel() {

    private val workerRawMaterialsRepository = WorkerRawMaterialsRepository()

    val suppliersLiveData = MutableLiveData<ApiResult<List<SupplierModel>>>()

    val fromEnterPriseLiveDate = MutableLiveData<ApiResult<List<EnterpriseModel>>>()

    val plantsLiveData = MutableLiveData<ApiResult<List<PlantModel>>>()

    var supplyCreateLiveData = MutableLiveData<ApiResult<Any>>()

    var materialTransferLiveData = MutableLiveData<ApiResult<Any>>()

    fun getWorkerRawMaterials(categoryId : Int, searchText : String): LiveData<PagingData<MaterialStoresModel>> {
        return workerRawMaterialsRepository.getWorkerRawMaterials(categoryId, searchText)
    }

    fun getSuppliers(){
        viewModelScope.launch {
            try {
                val response = workerRawMaterialsRepository.getSuppliers()
                if (response.code() == 200) suppliersLiveData.postValue(ApiResult.success(response.body()!!.data))
                else suppliersLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                suppliersLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun getFromEnterprise(materialId : Int){
        viewModelScope.launch {
            try {
                val response = workerRawMaterialsRepository.getMaterialTransferPlant(materialId)
                if (response.code() == 200) fromEnterPriseLiveDate.postValue(ApiResult.success(response.body()!!))
                else fromEnterPriseLiveDate.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                fromEnterPriseLiveDate.postValue(ApiResult.error(t))
            }
        }
    }

    fun getPlants(){
        viewModelScope.launch {
            try {
                val response = workerRawMaterialsRepository.getPlants()
                if (response.code() == 200) plantsLiveData.postValue(ApiResult.success(response.body()!!.data))
                else plantsLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                plantsLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun supplyCreate(supplyCreateModel: SupplyCreateModel) {
        viewModelScope.launch {
            try {
                val response = workerRawMaterialsRepository.supplyCreate(supplyCreateModel)
                if (response.code() == 200) supplyCreateLiveData.postValue(ApiResult.success(response.body()!!.detail))
                else supplyCreateLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                supplyCreateLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun materialTransfer(transferModel: TransferModel) {
        viewModelScope.launch {
            try {
                val response = workerRawMaterialsRepository.materialTransfer(transferModel)
                if (response.code() == 200) materialTransferLiveData.postValue(ApiResult.success(response.body()!!.detail))
                else materialTransferLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                materialTransferLiveData.postValue(ApiResult.error(t))
            }
        }
    }
}