package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model

import com.crudgroup.f9mobile.presentation.otherComponents.model.OlchovModel

data class MaterialStoresModel (

    val material_id : Int,
    val olchov_id : Int,
    val value : Number,
    val material: RawMaterialModel,
    val olchov: OlchovModel
)