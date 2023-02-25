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
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.data.database.LocaleCaches.categoryList
import com.crudgroup.f9mobile.data.database.LocaleCaches.getSupplyAlertDialog
import com.crudgroup.f9mobile.databinding.FragmentGetSupplyFromEnterpriseBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.*
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import java.lang.NumberFormatException


class GetSupplyFromEnterpriseFragment : Fragment() {

    private lateinit var binding: FragmentGetSupplyFromEnterpriseBinding
    private lateinit var getSupplyViewModel: GetSupplyViewModel
    private val categoryNameList = ArrayList<String>()
    private val rawMaterialsList = ArrayList<SupplyMaterialsModel>()
    private val rawMaterialsNameList = ArrayList<String>()
    private val fromEnterpriseList = ArrayList<EnterpriseModel>()
    private val fromEnterpriseNameList = ArrayList<String>()
    private val toEnterpriseList = ArrayList<PlantModel>()
    private val toEnterpriseNameList = ArrayList<String>()
    private lateinit var rawMaterialSpinnerAdapter : ArrayAdapter<String>
    private lateinit var categoryNameSpinnerAdapter : ArrayAdapter<String>
    private lateinit var fromEnterpriseNameSpinnerAdapter : ArrayAdapter<String>
    private lateinit var toEnterpriseNameSpinnerAdapter : ArrayAdapter<String>
    private var materialId = 0
    private var olchovId  = 0
    private var olchov = ""
    private var fromEnterpriseId = 0
    private var toEnterpriseId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupplyViewModel = ViewModelProvider(this)[GetSupplyViewModel :: class.java]
        rawMaterialSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,rawMaterialsNameList)
        categoryNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNameList)
        fromEnterpriseNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, fromEnterpriseNameList)
        toEnterpriseNameSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, toEnterpriseNameList)

        categoryNameList.add("Kategoriya : ")

        initObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGetSupplyFromEnterpriseBinding.inflate(inflater, container, false)
        binding.searchCategoryNameSpinner.adapter = categoryNameSpinnerAdapter
        getSupplyViewModel.getPlants()
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchCategoryNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    binding.productNameSpinner.setSelection(0)
                    val categoryId = categoryList[position-1].Material_Type.id
                    getSupplyViewModel.getRawMaterials(categoryId)
                    binding.productNameLayout.visibility = View.VISIBLE
                }
                else {
                    materialId = 0
                    olchovId = 0
                    fromEnterpriseId = 0
                    toEnterpriseId = 0
                    binding.productNameLayout.visibility = View.GONE
                    binding.supplierVolumeLayout.visibility = View.GONE
                    binding.fromEnterpriseLayout.visibility = View.GONE
                    binding.toEnterpriseLayout.visibility = View.GONE
                    binding.productNameSpinner.setSelection(0)
                    binding.fromEnterpriseNameSpinner.setSelection(0)
                    binding.toEnterpriseNameSpinner.setSelection(0)
                }
            }
        }

        binding.productNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0) {
                    materialId = rawMaterialsList[position-1].id
                    olchov = rawMaterialsList[position-1].olchov
                    olchovId = rawMaterialsList[position-1].olchov_id
                    getSupplyViewModel.getFromEnterprise(materialId)
                    binding.getSupplyVolumeType.text = olchov

                    binding.fromEnterpriseLayout.visibility = View.VISIBLE
                    binding.fromEnterpriseNameSpinner.setSelection(0)
                }
                else {
                    materialId = 0
                    olchovId = 0
                    fromEnterpriseId = 0
                    toEnterpriseId = 0
                    binding.fromEnterpriseLayout.visibility = View.GONE
                    binding.toEnterpriseLayout.visibility = View.GONE
                    binding.supplierVolumeLayout.visibility = View.GONE
                    binding.fromEnterpriseNameSpinner.setSelection(0)
                    binding.toEnterpriseNameSpinner.setSelection(0)
                    binding.getSupplyVolumeInput.text.clear()
                }

            }
        }
        binding.fromEnterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    fromEnterpriseId = fromEnterpriseList[position -1].plant.id
                    binding.toEnterpriseLayout.visibility = View.VISIBLE
                    binding.toEnterpriseNameSpinner.setSelection(0)
                }
                else {
                    fromEnterpriseId = 0
                    toEnterpriseId = 0
                    binding.toEnterpriseLayout.visibility = View.GONE
                    binding.supplierVolumeLayout.visibility = View.GONE
                    binding.toEnterpriseNameSpinner.setSelection(0)
                    binding.getSupplyVolumeInput.text.clear()
                }

            }
        }

        binding.toEnterpriseNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0) {
                    toEnterpriseId = toEnterpriseList[position -1].id
                    binding.supplierVolumeLayout.visibility = View.VISIBLE
                    binding.getSupplyVolumeInput.text.clear()
                }
                else {
                    toEnterpriseId = 0
                    binding.supplierVolumeLayout.visibility = View.GONE
                    binding.getSupplyVolumeInput.text.clear()
                }
            }
        }

        binding.getSupplyBtn.setOnClickListener {
            try {
                val volume = binding.getSupplyVolumeInput.text.toString().toDouble()

                if (volume > 0) {
                    val transferModel = TransferModel(materialId, fromEnterpriseId, volume, toEnterpriseId)
                    Log.e(Constants.GSFETAG, " Material transfer : $transferModel")
                    getSupplyViewModel.materialTransfer(transferModel)
                }
                else {
                    binding.getSupplyVolumeInput.error = "Hajmini to'g'ri kiriting !"
                }
            }
            catch (_: NumberFormatException) {
                binding.getSupplyVolumeInput.error = "Hajmini kiriting !"
            }
        }
    }

    @SuppressLint("LongLogTag")
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
                binding.productNameSpinner.adapter = rawMaterialSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromEnterprise", "Get Raw Materials ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Hom ashyolar malumotida hatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.fromEnterPriseLiveDate.observe(this) { apiResult ->
            apiResult.success {
                fromEnterpriseNameList.clear()
                fromEnterpriseList.clear()
                fromEnterpriseNameList.add("Qaysi sehdan :")

                for (i in it!!) {
                    fromEnterpriseList.add(i)
                    fromEnterpriseNameList.add(i.plant.name)
                }
                binding.fromEnterpriseNameSpinner.adapter = fromEnterpriseNameSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromEnterprise", "Get FromEnterprise ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Beruvchi ombor malumotida hatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.plantsLiveData.observe(this) { apiResult ->
            apiResult.success {
                toEnterpriseList.clear()
                toEnterpriseNameList.clear()
                toEnterpriseNameList.add("Qaysi sehga :")

                for (i in it!!) {
                    toEnterpriseList.add(i)
                    toEnterpriseNameList.add(i.name)
                }
                binding.toEnterpriseNameSpinner.adapter = toEnterpriseNameSpinnerAdapter
            }
            apiResult.error {
                Log.e("GetSupplyFromEnterprise", "Get toEnterprise ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Oluvchi ombor malumotida hatolik", Toast.LENGTH_SHORT).show()
            }
        }

        getSupplyViewModel.materialTransferLiveData.observe(this) { apiResult ->
            apiResult.success {
                Toast.makeText(requireContext(), "Transfer bajarildi !", Toast.LENGTH_SHORT).show()
                getSupplyAlertDialog?.dismiss()
            }
            apiResult.error {
                Log.e(Constants.GSFETAG, "Transfer : ${it?.localizedMessage}")
                Toast.makeText(requireContext(), "Transferda xatolik mavjud", Toast.LENGTH_SHORT).show()
            }
        }
    }

}