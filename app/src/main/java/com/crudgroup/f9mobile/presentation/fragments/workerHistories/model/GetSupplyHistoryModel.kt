package com.crudgroup.f9mobile.presentation.fragments.workerHistories.model

data class GetSupplyHistoryModel(
    val supply_id : Int,
    val supplier_id : Int,
    val supplier : String,
    val material : String,
    val olchov : String,
    val user : String,
    val value : Number,
    val price : Number,
    val created_at : String

)
