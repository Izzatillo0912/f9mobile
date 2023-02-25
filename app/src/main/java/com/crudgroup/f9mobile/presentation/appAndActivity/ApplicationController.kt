package com.crudgroup.f9mobile.presentation.appAndActivity

import android.app.Application
import com.orhanobut.hawk.Hawk

class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }
}