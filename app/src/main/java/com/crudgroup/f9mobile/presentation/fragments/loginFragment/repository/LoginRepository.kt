package com.crudgroup.f9mobile.presentation.fragments.loginFragment.repository

import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.model.Token
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import retrofit2.Response

class LoginRepository {

    private val api: F9Api by lazy { RetrofitInstance.api }

    suspend fun getToken(username: String, password: String): Response<Token> {
        return api.getToken(username, password)
    }

    suspend fun getMeInfo(userId : Int): Response<MeInfoModel> {
        return api.getMeInfo(userId)
    }
}