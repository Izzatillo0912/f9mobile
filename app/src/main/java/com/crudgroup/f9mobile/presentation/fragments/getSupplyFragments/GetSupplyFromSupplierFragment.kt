package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.crudgroup.f9mobile.data.database.LocaleCaches.categoryList
import com.crudgroup.f9mobile.data.database.LocaleCaches.getSupplyAlertDialog
import com.crudgroup.f9mobile.databinding.FragmentGetSupplyFromSupplierBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import java.lang.NumberFormatException
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.TimberCalculator

class GetSupplyFromSupplierFragment : Fragment() {

    private lateinit var binding: FragmentGetSupplyFromSupplierBinding
    private lateinit var getSupplyViewModel: GetSupplyViewModel
    private val categoryNameList = ArrayList<String>()
    private val rawMaterialsList = ArrayList<SupplyMaterialsModel>()
    private val rawMaterialsNameList = ArrayList<String>()
    private val supplierList = ArrayList<SupplierModel>()
    private val supplierNameList = ArrayList<String>()
    private val plantsList = ArrayList<PlantModel>()
    private val plantsNameList = ArrayList<String>()
    private lateinit var rawMaterialSpinnerAdapter : ArrayAdapter<String>
    private lateinit var categoryNameSpinnerAdapter : ArrayAdapter<String>
    private lateinit var supplierNameSpinnerAdapter : ArrayAdapter<String>
    private lateinit var plantsNameSpinnerAdapter : ArrayAdapter<String>
    private var materialId  = 0
    private var supplierId  = 0
    private var olchovId  = 0
    private var olchov = ""
    private var plantId  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupplyViewModel = ViewModelProvider(this)[GetSupplyViewModel :: class.java]
        rawMaterialSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,rawMaterialsNameList)
        categoryNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNameList)
        supplierNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, supplierNameList)
        plantsNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, plantsNameList)
        categoryNameList.add("Kategoriya :")

        initObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGetSupplyFromSupplierBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchCategoryNameSpinner.adapter = categoryNameSpinnerAdapter
        getSupplyViewModel.getSuppliers()
        getSupplyViewModel.getPlants()

        binding.searchCategoryNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    binding.searchProductNameSpinner.setSelection(0)
                    val categoryId = categoryList[position-1].Material_Type.id
                    getSupplyViewModel.getRawMaterials(categoryId)
                    binding.getProductNameLayout.visibility = View.VISIBLE
                }
                else {
                    binding.getProductNameLayout.visibility = View.GONE
                    binding.timberLayout.visibility = View.GONE
                    binding.supplierVolumeLayout.visibility = View.GONE
                    binding.getEnterpriseNameLayout.visibility = View.GONE
                    binding.supplyPriceLayout.visibility = View.GONE
                    binding.searchProductNameSpinner.setSelection(0)
                    binding.enterpriseNameSpinner.setSelection(0)
                }
            }
        }

        binding.searchProductNameSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0) {
                    val item = rawMaterialsList[position-1]
                    materialId = item.id
                    olchovId = item.olchov_id
                    olchov = item.olchov
                    binding.getSupplyVolumeType.text = olchov
                    supplierAndCalculatorShow(calculatorVisible = item.has_volume)
                    binding.getEnterpriseNameLayout.visibility = View.VISIBLE
                    binding.supplyPriceLayout.visibility = View.VISIBLE
                }
                else  {
                    materialId = 0
                    olchovId = 0
                    binding.getEnterpriseNameLayout.visibility = View.GONE
                    binding.supplyPriceLayout.visibility = View.GONE
                }
            }
        }

        binding.supplierNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                supplierId = if (position != 0) supplierList[position-1].id else 0
            }
        }

        binding.enterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                plantId = if (position != 0) plantsList[position-1].id else 0
            }
        }

        binding.getSupplyBtn.setOnClickListener {
            try {
                val volume = binding.getSupplyVolumeInput.text.toString().toDouble()
                val price = binding.getSupplyPriceInput.text.toString().toDouble()

                if (volume > 0 && price > 0) {
                    val supplyCreateModel = SupplyCreateModel(materialId, supplierId, volume, price, olchovId, plantId)
                    Log.e(Constants.GSFSTAG, "supply create : $supplyCreateModel")
                    getSupplyViewModel.supplyCreate(supplyCreateModel)
                }
            }
            catch (_: NumberFormatException) {
                Toast.makeText(requireContext(), "Malumotlani to'g'ri kiriting", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getTimberBtn.setOnClickListener {
            try {
                val volume = binding.inputVolume.text.toString().replace("KUB  :  ", "").replace(",", ".").toDouble()
                val price = binding.getSupplyPriceInput.text.toString().toDouble()

                if (volume > 0 && price > 0) {
                    val supplyCreateModel = SupplyCreateModel(materialId, supplierId, volume, price, olchovId, plantId)
                    Log.e(Constants.GSFSTAG, "supply create : $supplyCreateModel")
                    getSupplyViewModel.supplyCreate(supplyCreateModel)
                }
            }
            catch (_: NumberFormatException) {
                Toast.makeText(requireContext(), "Malumotlani to'g'ri kiriting", Toast.LENGTH_SHORT).show()
            }
        }

        TimberCalculator().calculator(binding.inputDiameter, binding.inputLength, binding.inputVolume, true)
    }

    private fun initObservers() {

        for (i in categoryList) { categoryNameList.add(i.Material_Type.name) }

        getSupplyViewModel.rawMaterialsLiveData.observe(this) { apiResult ->
            apiResult.success {

                rawMaterialsList.clear()
                rawMaterialsNameList.clear()
                rawMaterialsNameList.add("Hom-ashyo :")

                for (i in it!!) {
                    rawMaterialsList.add(i)
                    rawMaterialsNameList.add(i.name)
                }
                binding.searchProductNameSpinner.adapter = rawMaterialSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromSupplier", "Get Raw Materials ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Hom ashyolar malumotida hatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.suppliersLiveData.observe(this) { apiResult ->
            apiResult.success {

                supplierList.clear()
                supplierNameList.clear()
                supplierNameList.add("Taminotchi :")

                for (i in it!!) {
                    supplierList.add(i)
                    supplierNameList.add(i.name)
                }
                binding.supplierNameSpinner.adapter = supplierNameSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromSupplier", "Get Suppliers ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Taminotchilar malumotida xatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.plantsLiveData.observe(this) { apiResult ->
            apiResult.success {

                plantsList.clear()
                plantsNameList.clear()
                plantsNameList.add("Qaysi sehga :")

                for (i in it!!) {
                    plantsList.add(i)
                    plantsNameList.add(i.name)
                }
                binding.enterpriseNameSpinner.adapter = plantsNameSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromSupplier", "Get plants ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Korxonalar malumotida xatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.supplyCreateLiveData.observe(this) { apiResult ->
            apiResult.success {
                Toast.makeText(requireContext(), "Taminot olindi !", Toast.LENGTH_SHORT).show()
                getSupplyAlertDialog?.dismiss()
            }
            apiResult.error {
                Log.e("GetSupplyFromSupplier", "Get plants ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Korxonalar malumotida xatolik", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun supplierAndCalculatorShow( calculatorVisible : Boolean) {
        when(calculatorVisible) {
            true -> {
                binding.timberLayout.visibility = View.VISIBLE
                binding.supplierVolumeLayout.visibility = View.GONE
                binding.getSupplyBtn.visibility = View.GONE
            }

            false -> {
                binding.supplierVolumeLayout.visibility = View.VISIBLE
                binding.getSupplyBtn.visibility = View.VISIBLE
                binding.timberLayout.visibility = View.GONE
            }
        }
    }
}