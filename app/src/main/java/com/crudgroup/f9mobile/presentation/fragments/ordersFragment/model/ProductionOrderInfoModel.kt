package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model

import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.RawMaterialModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.CategoryType

data class ProductionOrderInfoModel(
    val material_type : CategoryType,
    val material_type_id : Int,
    val id : Int,
    val cycle_id : Int,
    val component_items : List<ComponentModel>
)
