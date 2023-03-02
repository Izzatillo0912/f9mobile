package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

data class ProductionOrdersModel(
    val quantity : Double,
    val cycle_name : String,
    val product_name : String,
    val customer_name : String,
    val product_id : Int,
    val cycle_id : Int,
    val olchov_name : String,
    val product_image : String,
    val trade_id : Int
)
