package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model

data class SupplyMaterialsModel(
    val id : Int,
    val name: String,
    val price : Number,
    val has_volume : Boolean,
    val olchov : String,
    val olchov_id : Int,
    val material_type : String
)
