package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

data class CyclesModel(
    val id : Int,
    val product_id : Int,
    val ordinate : Int,
    val name : String,
    val kpi : Double,
    val product : ProductModel
)
