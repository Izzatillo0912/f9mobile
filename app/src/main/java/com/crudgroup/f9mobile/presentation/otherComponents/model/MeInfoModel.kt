package com.crudgroup.f9mobile.presentation.otherComponents.model

data class MeInfoModel(
    val name : String,
    val username : String,
    val role : String,
    val nation : String,
    val brithDate : String,
    val specialty : String,
    val id : Int,
    val plant_id : Int,
    val passport : String,
    val agreement_expiration : String,
    val workBeginDate : String,
    val phones : ArrayList<PhoneNumberModel>,
    val plant_users : ArrayList<PlantsModel>
)
