package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model

import androidx.room.Dao

data class SupplyCreateModel(
    val material_id : Int,
    val supplier_id : Int,
    val value : Double,
    val price : Double,
    val olchov_id : Int,
    val plant_id : Int
)