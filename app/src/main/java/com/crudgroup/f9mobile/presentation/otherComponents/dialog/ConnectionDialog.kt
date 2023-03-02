package com.crudgroup.f9mobile.presentation.otherComponents.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import com.crudgroup.f9mobile.databinding.DialogLoadingAndRequestResultBinding
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class ConnectionDialog(val context: Context, private val refreshClicked : RefreshClicked) {

    private var connectionDialog : AlertDialog? = null
    private lateinit var connectionBinding : DialogLoadingAndRequestResultBinding
    
    interface RefreshClicked{
        fun connectDialogRefreshClicked(refreshType:String)
    }

    private fun createDialog() {
        connectionDialog = AlertDialog.Builder(context).create()
        connectionBinding = DialogLoadingAndRequestResultBinding.inflate(LayoutInflater.from(context))
        connectionDialog?.setView(connectionBinding.root)
        connectionDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        connectionDialog?.setCancelable(false)
        connectionDialog?.show()
    }
    
    fun showDialog(refreshType: String, animationType : String, message : String) {
        dismissDialog()
        if (connectionDialog == null) {
            createDialog()
            animationChanger(animationType, message)
            connectionBinding.connectionDialogRetryBtn.setOnClickListener {
                refreshClicked.connectDialogRefreshClicked(refreshType)
            }
            connectionBinding.connectionDialogCloseBtn.setOnClickListener {
                dismissDialog()
            }
        }
    }

    fun animationChanger(key: String, message: String) {

        if (connectionDialog == null) {
            createDialog()
        }

        when(key) {
            Constants.NO_INTERNET -> {
                connectionBinding.lottieConnectionDialog.setAnimation("no_internet.json")
                connectionBinding.connectionDialogMessage.text = message
                connectionBinding.connectionDialogRetryBtn.visibility = View.VISIBLE
                connectionBinding.connectionDialogCloseBtn.visibility = View.VISIBLE
            }
            Constants.IS_LOADING -> {
                connectionBinding.lottieConnectionDialog.setAnimation("loading.json")
                connectionBinding.connectionDialogMessage.text = message
            }
            Constants.IS_CHECK_API -> {
                connectionBinding.lottieConnectionDialog.setAnimation("check.json")
                connectionBinding.lottieConnectionDialog.loop(false)
                connectionBinding.lottieConnectionDialog.playAnimation()
                connectionBinding.connectionDialogMessage.text = message
                object : CountDownTimer(1000,1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        dismissDialog()
                    }
                }.start()
            }
            Constants.IS_NOT_CHECKED -> {
                connectionBinding.lottieConnectionDialog.setAnimation("not.json")
                connectionBinding.lottieConnectionDialog.loop(false)
                connectionBinding.lottieConnectionDialog.playAnimation()
                connectionBinding.connectionDialogMessage.text = message
                connectionBinding.connectionDialogRetryBtn.visibility = View.VISIBLE
                connectionBinding.connectionDialogCloseBtn.visibility = View.VISIBLE
            }
        }
    }

    fun dismissDialog() {
        connectionDialog?.dismiss()
        connectionDialog = null
    }
}