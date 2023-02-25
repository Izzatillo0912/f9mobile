package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.repository.GetSupplyRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GetSupplyViewModel : ViewModel() {

    private val repository = GetSupplyRepository()

    val rawMaterialsLiveData = MutableLiveData<ApiResult<List<SupplyMaterialsModel>>>()

    val suppliersLiveData = MutableLiveData<ApiResult<List<SupplierModel>>>()

    val plantsLiveData = MutableLiveData<ApiResult<List<PlantModel>>>()

    val fromEnterPriseLiveDate = MutableLiveData<ApiResult<List<EnterpriseModel>>>()

    val supplyCreateLiveData = MutableLiveData<ApiResult<Any>>()

    val materialTransferLiveData = MutableLiveData<ApiResult<Any>>()

    fun getRawMaterials(materialTypeId : Int){
        viewModelScope.launch {
            try {
                val response = repository.getRawMaterialsList(materialTypeId)
                if (response.code() == 200) rawMaterialsLiveData.postValue(ApiResult.success(response.body()!!.data))
                else rawMaterialsLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                rawMaterialsLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun getSuppliers(){
        viewModelScope.launch {
            try {
                val response = repository.getSuppliers()
                if (response.code() == 200) suppliersLiveData.postValue(ApiResult.success(response.body()!!.data))
                else suppliersLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                suppliersLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun getPlants(){
        viewModelScope.launch {
            try {
                val response = repository.getPlants()
                if (response.code() == 200) plantsLiveData.postValue(ApiResult.success(response.body()!!.data))
                else plantsLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                plantsLiveData.postValue(ApiResult.error(t))
            }
        }
    }


    fun getFromEnterprise(materialId : Int){
        viewModelScope.launch {
            try {
                val response = repository.getMaterialTransferPlant(materialId)
                if (response.code() == 200) fromEnterPriseLiveDate.postValue(ApiResult.success(response.body()!!))
                else fromEnterPriseLiveDate.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                fromEnterPriseLiveDate.postValue(ApiResult.error(t))
            }
        }
    }

    fun supplyCreate(supplyCreateModel: SupplyCreateModel) {
        viewModelScope.launch {
            try {
                val response = repository.supplyCreate(supplyCreateModel)
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
                val response = repository.materialTransfer(transferModel)
                if (response.code() == 200) materialTransferLiveData.postValue(ApiResult.success(response.body()!!.detail))
                else materialTransferLiveData.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                materialTransferLiveData.postValue(ApiResult.error(t))
            }
        }
    }
}