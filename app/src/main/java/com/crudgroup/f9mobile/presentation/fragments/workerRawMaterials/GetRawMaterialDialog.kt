package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.crudgroup.f9mobile.data.database.LocaleCaches
import com.crudgroup.f9mobile.databinding.DialogGetRawMaterialBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.WorkerRawMaterialsViewModel
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.TimberCalculator
import com.crudgroup.f9mobile.presentation.otherComponents.ViewPagerTabLayout
import java.lang.NumberFormatException

class GetRawMaterialDialog(val fragment: Fragment) {

    private var getMaterialDialog = AlertDialog.Builder(fragment.requireContext()).create()
    private var binding: DialogGetRawMaterialBinding = DialogGetRawMaterialBinding.inflate(LayoutInflater.from(fragment.requireContext()))
    private val viewPagerTabLayout = ViewPagerTabLayout(fragment.requireContext())
    private val viewModel = ViewModelProvider(fragment) [WorkerRawMaterialsViewModel :: class.java]
    private val timberCalculator = TimberCalculator()
    private val supplierList = ArrayList<SupplierModel>()
    private val supplierNameList = ArrayList<String>()
    private val plantsList = ArrayList<PlantModel>()
    private val plantsNameList = ArrayList<String>()
    private val fromEnterpriseList = ArrayList<EnterpriseModel>()
    private val fromEnterpriseNameList = ArrayList<String>()
    private val supplierNameSpinnerAdapter = ArrayAdapter(fragment.requireContext(), R.layout.simple_spinner_dropdown_item, supplierNameList)
    private val fromEnterpriseNameSpinnerAdapter = ArrayAdapter(fragment.requireContext(), R.layout.simple_spinner_dropdown_item, fromEnterpriseNameList)
    private val plantsNameSpinnerAdapter = ArrayAdapter(fragment.requireContext(), R.layout.simple_spinner_dropdown_item, plantsNameList)


    init {
        getMaterialDialog.setView(binding.root)
        getMaterialDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        getMaterialDialog.setCancelable(false)
    }

    @SuppressLint("LongLogTag")
    fun showGetSupplyDialog(rawMaterialModel: MaterialStoresModel) {
        getMaterialDialog.show()
        binding.dialogGetMaterialTitle.text = rawMaterialModel.material.name
        var supplierId = 0
        var fromEnterpriseId = 0
        var toEnterpriseId = 0
        var plantId = 0
        initObserver()

        viewPagerTabLayout.connectBtn(binding.getSupplyFromSupplier, binding.getSupplyFromEnterprice)

        viewModel.getSuppliers()
        viewModel.getPlants()
        viewModel.getFromEnterprise(rawMaterialModel.material_id)

        timberCalculator.calculator(binding.getMaterialFromSupplier.inputDiameter, binding.getMaterialFromSupplier.inputLength, binding.getMaterialFromSupplier.inputVolume, true)

        binding.getSupplyFromSupplier.setOnClickListener {
            viewPagerTabLayout.changerBtnColor(0)
            binding.getMaterialFromSupplier.root.visibility = View.VISIBLE
            binding.getMaterialFromEnterprise.root.visibility = View.GONE
        }

        binding.getSupplyFromEnterprice.setOnClickListener {
            viewPagerTabLayout.changerBtnColor(1)
            binding.getMaterialFromSupplier.root.visibility = View.GONE
            binding.getMaterialFromEnterprise.root.visibility = View.VISIBLE
        }

        if(rawMaterialModel.material.has_volume) {
            binding.getMaterialFromSupplier.timberLayout.visibility = View.VISIBLE
            binding.getMaterialFromSupplier.supplierVolumeLayout.visibility = View.GONE
            binding.getMaterialFromSupplier.getSupplyBtn.visibility = View.GONE

        }
        else {
            binding.getMaterialFromSupplier.supplierVolumeLayout.visibility = View.VISIBLE
            binding.getMaterialFromSupplier.getSupplyBtn.visibility = View.VISIBLE
            binding.getMaterialFromSupplier.timberLayout.visibility = View.GONE
        }

        binding.getMaterialDialogCloseBtn.setOnClickListener {
            getMaterialDialog.dismiss()
        }

        binding.getMaterialFromSupplier.supplierNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                supplierId = if (position != 0) supplierList[position-1].id else 0
            }
        }

        binding.getMaterialFromEnterprise.enterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromEnterpriseId = if (position != 0) fromEnterpriseList[position -1].plant.id else 0
            }
        }

        binding.getMaterialFromEnterprise.toEnterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toEnterpriseId = if (position != 0) plantsList[position -1].id else 0
            }
        }

        binding.getMaterialFromSupplier.enterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                plantId = if (position != 0) plantsList[position-1].id else 0
            }
        }

        binding.getMaterialFromSupplier.getSupplyBtn.setOnClickListener {
            try {
                val volume = binding.getMaterialFromSupplier.getSupplyVolumeInput.text.toString().toDouble()
                val price = binding.getMaterialFromSupplier.getSupplyPriceInput.text.toString().toDouble()

                if (volume > 0 && price > 0 && plantId > 0) {
                    val supplyCreateModel = SupplyCreateModel(rawMaterialModel.material_id, supplierId, volume, price, rawMaterialModel.olchov_id, plantId)
                    Log.e(Constants.GMDTAG, "supply create : $supplyCreateModel")
                    viewModel.supplyCreate(supplyCreateModel)
                }
                else {
                    Toast.makeText(fragment.requireContext(), "Malumotlarda xatolik bor", Toast.LENGTH_SHORT).show()
                }
            }
            catch (_: NumberFormatException) {
                Toast.makeText(fragment.requireContext(), "Malumotlani to'g'ri kiriting", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getMaterialFromSupplier.getTimberBtn.setOnClickListener {
            try {
                val volume = binding.getMaterialFromSupplier.inputVolume.text.toString().replace("KUB  :  ", "").replace(",", ".").toDouble()
                val price = binding.getMaterialFromSupplier.getSupplyPriceInput.text.toString().toDouble()

                if (volume > 0 && price > 0 && plantId > 0) {
                    val supplyCreateModel = SupplyCreateModel(rawMaterialModel.material_id, supplierId, volume, price, rawMaterialModel.olchov_id, plantId)
                    Log.e(Constants.GMDTAG, "supply create : $supplyCreateModel")
                    viewModel.supplyCreate(supplyCreateModel)
                }
                else {
                    Toast.makeText(fragment.requireContext(), "Malumotlarda xatolik bor", Toast.LENGTH_SHORT).show()
                }
            }
            catch (_: NumberFormatException) {
                Toast.makeText(fragment.requireContext(), "Malumotlani to'g'ri kiriting", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getMaterialFromEnterprise.getSupplyBtn.setOnClickListener {
            try {
                val volume = binding.getMaterialFromEnterprise.getSupplyVolumeInput.text.toString().toDouble()

                if (volume > 0 && fromEnterpriseId > 0 && toEnterpriseId > 0) {
                    val transferModel = TransferModel(rawMaterialModel.material_id, fromEnterpriseId, volume, toEnterpriseId)
                    viewModel.materialTransfer(transferModel)
                }
                else {
                    Toast.makeText(fragment.requireContext(), "Malumotlarda xatolik bor", Toast.LENGTH_SHORT).show()
                }

            }
            catch (_: NumberFormatException) {
                binding.getMaterialFromEnterprise.getSupplyVolumeInput.error = "Hajmini to'g'ri kiriting !"
            }
        }


    }

    private fun initObserver() {

        viewModel.suppliersLiveData.observe(fragment.viewLifecycleOwner) { apiResult ->
            apiResult.success {

                supplierList.clear()
                supplierNameList.clear()
                supplierNameList.add("Taminotchi :")

                for (i in it!!) {
                    supplierList.add(i)
                    supplierNameList.add(i.name)
                }
                binding.getMaterialFromSupplier.supplierNameSpinner.adapter = supplierNameSpinnerAdapter
            }
            apiResult.error {
                Log.e(Constants.GMDTAG, "Get Suppliers ${it?.localizedMessage}")
                Toast.makeText(fragment.requireContext(), "Taminotchilar malumotida xatolik", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.plantsLiveData.observe(fragment.viewLifecycleOwner) { apiResult ->
            apiResult.success {

                plantsList.clear()
                plantsNameList.clear()
                plantsNameList.add("Sehga : ")

                for (i in it!!) {
                    plantsList.add(i)
                    plantsNameList.add(i.name)
                }
                binding.getMaterialFromSupplier.enterpriseNameSpinner.adapter = plantsNameSpinnerAdapter
                binding.getMaterialFromEnterprise.toEnterpriseNameSpinner.adapter = plantsNameSpinnerAdapter
            }
            apiResult.error {
                Log.e(Constants.GMDTAG, "Get plants ${it?.localizedMessage}")
                Toast.makeText(fragment.requireContext(), "Korxonalar malumotida xatolik", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fromEnterPriseLiveDate.observe(fragment.viewLifecycleOwner) { apiResult ->
            apiResult.success {
                fromEnterpriseList.clear()
                fromEnterpriseNameList.clear()
                fromEnterpriseNameList.add("Sehdan :")
                for (i in it!!) {
                    fromEnterpriseList.add(i)
                    fromEnterpriseNameList.add(i.plant.name)
                }
                binding.getMaterialFromEnterprise.enterpriseNameSpinner.adapter = fromEnterpriseNameSpinnerAdapter
            }
            apiResult.error {
                Log.e(Constants.GMDTAG, "Get FromEnterprise ${it?.localizedMessage}")
                Toast.makeText(fragment.requireContext(), "Beruvchi ombor malumotida hatolik", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.supplyCreateLiveData.observe(fragment.viewLifecycleOwner) { apiResult ->
            apiResult.success {
                Toast.makeText(fragment.requireContext(), "Taminot olindi !", Toast.LENGTH_SHORT).show()
                getMaterialDialog.dismiss()
                viewModel.supplyCreateLiveData = MutableLiveData()
            }
            apiResult.error {
                Log.e(Constants.GMDTAG, "Get plants ${it?.localizedMessage}")
                Toast.makeText(fragment.requireContext(), "Malumotlarda xatolik bor", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.materialTransferLiveData.observe(fragment.viewLifecycleOwner) { apiResult ->
            apiResult.success {
                Toast.makeText(fragment.requireContext(), "Transfer bajarildi !", Toast.LENGTH_SHORT).show()
                getMaterialDialog.dismiss()
                viewModel.materialTransferLiveData = MutableLiveData()
            }
            apiResult.error {
                Log.e(Constants.GMDTAG, "Transfer : ${it?.localizedMessage}")
                Toast.makeText(fragment.requireContext(), "Transferda xatolik mavjud", Toast.LENGTH_SHORT).show()
            }
        }

    }
}