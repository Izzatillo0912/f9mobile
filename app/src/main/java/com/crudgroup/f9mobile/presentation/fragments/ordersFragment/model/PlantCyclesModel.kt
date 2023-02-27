package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.PlantModel

data class PlantCyclesModel(
    val cycles_id : Int,
    val plant_id : Int,
    val id : Int,
    val cycle : CyclesModel,
    val plant: PlantModel
)
