package com.crudgroup.f9mobile.presentation.fragments.roleFragments.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crudgroup.f9mobile.presentation.fragments.roleFragments.repository.WorkerRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.HttpException

class WorkerViewModel : ViewModel() {

    private val repository = WorkerRepository()

    var updateMeInfo = MutableLiveData<ApiResult<Any>>()

    val getMeInfo = MutableLiveData<ApiResult<MeInfoModel>>()

    fun updateMeInfo(myId : Int, meInfoChangeModel: MeInfoChangeModel) {
        viewModelScope.launch {
            try {
                val response = repository.updateMeInfo(myId, meInfoChangeModel)
                if (response.code() == 200) updateMeInfo.postValue(ApiResult.success(response.body()!!.detail))
                else updateMeInfo.postValue(ApiResult.error(HttpException(response)))
            }catch (t: Throwable){
                updateMeInfo.postValue(ApiResult.error(t))
            }
        }
    }

    fun getMeInfo(userId : Int){
        viewModelScope.launch {
            try {
                val response = repository.getMeInfo(userId)
                if (response.code() == 200) getMeInfo.postValue(ApiResult.success(response.body(), "Shaxsingiz tasdiqlandi! Xush kelibsiz"))
                else if (response.code() == 401) getMeInfo.postValue(ApiResult.error(HttpException(response),"Login yoki parol noto'g'ri !"))
                else getMeInfo.postValue(ApiResult.error(HttpException(response)))

            }catch (t: Throwable){ getMeInfo.postValue(ApiResult.error(t)) }
        }
    }

}