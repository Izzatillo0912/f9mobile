package com.crudgroup.f9mobile.presentation.fragments.loginFragment.model

import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel

data class Token(
    val access_token: String,
    val user : UserModel
)
