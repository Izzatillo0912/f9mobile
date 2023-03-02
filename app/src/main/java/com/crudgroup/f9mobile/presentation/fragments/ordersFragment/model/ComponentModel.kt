package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

data class ComponentModel(
    val id : Int,
    val product_id : Int,
    val value : Double,
    val material_id : Int,
    val component_id : Int,
    val material : ProductModel
)
