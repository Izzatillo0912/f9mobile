package com.crudgroup.f9mobile.presentation.fragments.loginFragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.repository.LoginRepository
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    private val loginRepository = LoginRepository()

    val getTokenData = MutableLiveData<ApiResult<Token>>()

    val getMeInfo = MutableLiveData<ApiResult<MeInfoModel>>()

    fun getToken(userName: String, password: String){
        viewModelScope.launch {
            try {
                val response = loginRepository.getToken(userName, password)
                if (response.code() == 200) getTokenData.postValue(ApiResult.success(response.body(), "Shaxsingiz tasdiqlandi! Xush kelibsiz"))
                else if (response.code() == 401) getTokenData.postValue(ApiResult.error(HttpException(response),"Login yoki parol noto'g'ri !"))
                else getTokenData.postValue(ApiResult.error(HttpException(response)))

            }catch (t: Throwable){ getTokenData.postValue(ApiResult.error(t)) }
        }
    }

    fun getMeInfo(userId : Int){
        viewModelScope.launch {
            try {
                val response = loginRepository.getMeInfo(userId)
                if (response.code() == 200) getMeInfo.postValue(ApiResult.success(response.body(), "Shaxsingiz tasdiqlandi! Xush kelibsiz"))
                else if (response.code() == 401) getMeInfo.postValue(ApiResult.error(HttpException(response),"Login yoki parol noto'g'ri !"))
                else getMeInfo.postValue(ApiResult.error(HttpException(response)))

            }catch (t: Throwable){ getMeInfo.postValue(ApiResult.error(t)) }
        }
    }

}