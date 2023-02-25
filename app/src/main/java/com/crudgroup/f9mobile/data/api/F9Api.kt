package com.crudgroup.f9mobile.data.api

import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.model.Token
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.GetSupplyHistoryModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.RawMaterialModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.PaginationModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.ResponseDetail
import retrofit2.Response
import retrofit2.http.*

interface F9Api {

    @FormUrlEncoded
    @POST("token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Token>

    @GET("user_one")
    suspend fun getMeInfo(
        @Query("id") user_id : Int = 15
    ):Response<MeInfoModel>

    @GET("get_orders")
    suspend fun getOrders(
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ):Response<PaginationModel<OrdersModel>>

    @GET("material_types")
    suspend fun getWarehouseCategory(
        @Query("search") search : String,
        @Query("page") page : Int,
        @Query("for_plant_id") forPlantId : Int,
        @Query("limit") limit : Int
    ):Response<PaginationModel<WarehouseCategoryModel>>

    @GET("materials")
    suspend fun getSupplyMaterials(
        @Query("material_type_id") material_type_id : Int
    ):Response<PaginationModel<SupplyMaterialsModel>>

    @GET("material_stores")
    suspend fun getWorkerRawMaterials(
        @Query("search") search : String,
        @Query("material_type_id") material_type_id: Int,
        @Query("plant_id") plant_id: Int,
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ):Response<PaginationModel<MaterialStoresModel>>

    @GET("suppliers")
    suspend fun getSuppliers() : Response<PaginationModel<SupplierModel>>

    @GET("plants")
    suspend fun getPlants(
        @Query("disabled") disabled : Boolean = false
    ) : Response<PaginationModel<PlantModel>>


    @GET("material_transfer_plants")
    suspend fun getEnterprise(
        @Query("material_id") material_id : Int
    ) : Response<List<EnterpriseModel>>

    @POST("supply/create")
    suspend fun supplyCreate(
        @Body supplyCreateModel: SupplyCreateModel
    ) : Response<ResponseDetail>

    @POST("material_transfer/create")
    suspend fun materialTransfer(
        @Body transferModel: TransferModel
    ) : Response<ResponseDetail>


    @GET("supplies")
    suspend fun getSupplyHistory(
        @Query("supplier_id") supplier_id : Int,
        @Query("plant_id") plant_id: Int,
//        @Query("from_date") from_date : String,
//        @Query("to_date") to_date : String,
        @Query("search") search : String,
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ):Response<PaginationModel<GetSupplyHistoryModel>>

}