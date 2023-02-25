package com.crudgroup.f9mobile.presentation.otherComponents

import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel

object Constants {

    //Network Retrofit
    const val BASE_URL = "https://api.f9.crud.uz/"
    const val IMAGE_BASE_URL = "https://api.f9.crud.uz/images/"
    const val SHARED_KEY = "locale_data_key"


    //Auth
    const val ROLE_WORKER = "worker"
    const val ROLE_ADMIN = "admin"
    const val ROLE_PLANT_ADMIN = "plant_admin"
    const val ROLE_ANALYTIC = "analytic"
    var meInfoModel : MeInfoModel? = null

    //Pager 3
    const val PAGE_SIZE = 10
    const val INITIAL_PAGE_NUMBER = 1

    //Refresh key
    const val GET_TOKEN = "get_token"
    const val GET_ME_INFO = "get_me_info"
    const val CHANGE_ME_INFO = "change_me_info"
    const val GET_CATEGORIES = "getWarehouseCategories"
    const val GET_RAW_MATERIALS = "getRawMaterials"
    const val GET_PLANT_CYCLES = "get_plant_cycles"

    //TAG
    const val CMTAG = "Connectivity Manager"
    const val LFTAG = "Login Fragment"
    const val WFTAG = "Warehouse Fragment"
    const val GSFSTAG = "Get Supply From Supplier Fragment"
    const val GMDTAG = "Get Material Dialog"
    const val GSFETAG = "Get Supply From Enterprise Fragment"

    //Connection dialog animation type
    const val NO_INTERNET = "no_internet"
    const val IS_LOADING = "wait"
    const val IS_CHECK_API = "checked"
    const val IS_NOT_CHECKED = "not_checked"
}