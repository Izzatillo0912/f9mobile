package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model

import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.CategoryType

data class RawMaterialModel(
    val id : Int,
    val name: String,
    val price : Number,
    val has_volume : Boolean,
    val olchov : String,
    val olchov_id : Int,
    val material_type : CategoryType
)