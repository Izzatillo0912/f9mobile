package com.crudgroup.f9mobile.presentation.otherComponents.model

import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.PlantModel

data class PlantsModel(
    val id : Int,
    val user_id : Int,
    val shift_id: Int,
    val plant_id : Int,
    val plant : PlantModel
)
