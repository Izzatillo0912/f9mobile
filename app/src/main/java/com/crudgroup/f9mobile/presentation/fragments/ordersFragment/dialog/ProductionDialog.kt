package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.DialogProductionBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionCreateModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.orhanobut.hawk.Hawk

class ProductionDialog(val context: Context,  val productionClickListener: ProductionClickListener){

    interface ProductionClickListener{
        fun checkProductionClicked(productionCreateModel: ProductionCreateModel)
    }

    var dialog : AlertDialog? = null
    private lateinit var binding: DialogProductionBinding

    private fun createDialog() {
        dialog = AlertDialog.Builder(context).create()
        binding = DialogProductionBinding.inflate(LayoutInflater.from(context))
        dialog?.setView(binding.root)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()
    }

    @SuppressLint("SetTextI18n")
    fun showDialog(productionOrdersModel: ProductionOrdersModel) {
        dismissDialog()
        if (dialog == null) {
            createDialog()

            val image = Constants.IMAGE_BASE_URL + "products/" + productionOrdersModel.product_image
            if (!image.isNullOrEmpty()) Glide
                .with(binding.root.context)
                .load(image)
                .centerCrop()
                .into(binding.productionOrderCreate.infoImage)

            binding.dialogProductionTitle.text = productionOrdersModel.product_name
            binding.productionOrderCreate.infoProductName.text = productionOrdersModel.product_name
            binding.productionOrderCreate.infoQuantity.text = "${productionOrdersModel.quantity}  ${productionOrdersModel.olchov_name}"
            binding.productionOrderCreate.inputVolumeType.text = productionOrdersModel.olchov_name
            binding.closeDialog.setOnClickListener { dismissDialog()  }


            binding.productionOrderCreate.productionBtn.setOnClickListener {
                try {
                    val volume = binding.productionOrderCreate.inputVolume.text.toString().toDouble()

                    if (volume > 0) {
                        val productionCreateModel = ProductionCreateModel(
                            productionOrdersModel.cycle_id,
                            Hawk.get("my_plant_id"), volume)

                        productionClickListener.checkProductionClicked(productionCreateModel)
                    }

                }catch (n : NumberFormatException) {
                    binding.productionOrderCreate.inputVolume.error = "Hajimini to'g'ri kiriting !!"
                }
            }
        }
    }

    fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }
}