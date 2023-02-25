package com.crudgroup.f9mobile.data.database

import android.app.AlertDialog
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel

object LocaleCaches {

    var categoryList : ArrayList<WarehouseCategoryModel> = ArrayList()
    var getSupplyAlertDialog : AlertDialog? = null
}