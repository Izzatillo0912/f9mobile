package com.crudgroup.f9mobile.presentation.fragments.roleFragments.repository

import com.crudgroup.f9mobile.data.api.F9Api
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.fragments.roleFragments.model.MeInfoChangeModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.ResponseDetail
import okhttp3.RequestBody
import retrofit2.Response

class WorkerRepository {

    private val api: F9Api by lazy { RetrofitInstance.api}


    suspend fun getMeInfo(userId : Int): Response<MeInfoModel> {
        return api.getMeInfo(userId)
    }

    suspend fun updateMeInfo(myId : Int, meInfoChangeModel: MeInfoChangeModel) : Response<ResponseDetail> {
        return api.updateMeInfo(myId, meInfoChangeModel )
    }

}