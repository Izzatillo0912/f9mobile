package com.crudgroup.f9mobile.presentation.otherComponents

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.crudgroup.f9mobile.data.api.RetrofitInstance
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.model.ErrorResponseDetail
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class ConnectionError(val context: Context) {

    private fun parseError(errorBody: ResponseBody?): String {
        val converter: Converter<ResponseBody, ErrorResponseDetail> = RetrofitInstance.retrofit
            .responseBodyConverter(ErrorResponseDetail::class.java, arrayOfNulls<Annotation>(0))
        return try { if (errorBody == null) ""
            else converter.convert(errorBody)!!.detail
        } catch (e: IOException) { return "" }
    }

    @SuppressLint("LongLogTag")
    fun checkConnectionError(t: Throwable?, connectionDialog : ConnectionDialog, refreshType : String){
        when (t) {
            is HttpException -> {
                Log.e("CheckConnection error : ", t.response().toString())

                val errorMassage = parseError(t.response()?.errorBody())

                if (errorMassage.isEmpty()) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED,"So'rov bilan xatolik mavjud !!") }
                else if (t.code() == 401) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED,"Login yoki parol noto'g'ri !!") }
                else if (t.code() == 500) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Malumotlarar mavjuda emas !!") }
                else if (t.code() == 404) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Malumotlar manzili topilmadi !!") }
                else connectionDialog.animationChanger(Constants.IS_NOT_CHECKED, "So'rov bilan xatolik mavjud !!")

            }
            is SocketTimeoutException, is InterruptedIOException, is TimeoutException ->
                connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Tarmoqqa ulanish vaqti tugadi ! Qayta urunib ko'ring")

            else -> {
                Log.e("checkConnectionError: ", t?.localizedMessage.toString())
                connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "So'rov bilan xatolik mavjud !!")
            }
        }
    }
}