package com.crudgroup.f9mobile.presentation.fragments.roleFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentWorkerProfileBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.PlantModel
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.model.LoginViewModel
import com.crudgroup.f9mobile.presentation.otherComponents.AllAnimations
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.ConnectionError
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.MyConnectivityManager
import com.crudgroup.f9mobile.presentation.otherComponents.adapter.PhoneNumberAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.adapter.phoneNumberList
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.model.MeInfoModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.PhoneNumberModel
import com.crudgroup.f9mobile.presentation.otherComponents.model.PlantsModel
import com.orhanobut.hawk.Hawk


class WorkerProfileFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentWorkerProfileBinding
    private lateinit var allAnimations: AllAnimations
    private lateinit var phoneNumberAdapter: PhoneNumberAdapter
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private var myPlantsList = Constants.meInfoModel?.plant_users
    private var myPlantsNameList = ArrayList<String>()
    private var themeList = arrayListOf("Tongi rejim", "Tungi rejim")
    private var languageList = arrayListOf("Qozoq tili", "Rus tili")
    private var checkConnection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allAnimations = AllAnimations(requireContext())
        loginViewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        connectivityManager = MyConnectivityManager(requireContext())
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        getPlantsList(Constants.meInfoModel?.plant_users ?: ArrayList())
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerProfileBinding.inflate(inflater, container, false)
        getMeInfo(Constants.meInfoModel!!)

        if (checkConnection) {
            connectionDialog.showDialog(Constants.GET_ME_INFO, Constants.IS_LOADING, "Iltimos kuting !\n Malumotlar qidirilmoqda..")
            loginViewModel.getMeInfo(Hawk.get("user_id"))
        }else {
            connectionDialog.showDialog(Constants.GET_ME_INFO, Constants.IS_NOT_CHECKED, "Internet aloqasi mavjud emas !")
        }


        binding.appLanguageSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.themeModeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, themeList)
        binding.myPlantsSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, myPlantsNameList)

        for (i in languageList.withIndex()) {
            if (i.value == Hawk.get("app_language")) {
                binding.appLanguageSpinner.setSelection(i.index)
            }
        }

        for (i in themeList.withIndex()) {
            if (i.value == Hawk.get("theme_mode")) {
                binding.themeModeSpinner.setSelection(i.index)
            }
        }

        if(!myPlantsList.isNullOrEmpty()) {
            for (i in myPlantsList!!.withIndex()) {
                if (i.value.plant.id == Hawk.get("my_plant_id") && i.value.plant.name == Hawk.get("my_plant_name")) {
                    binding.myPlantsSpinner.setSelection(i.index)
                }
                else binding.myPlantsSpinner.setSelection(0)
            }
            binding.myPlantsSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, myPlantsNameList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myPlantsSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (myPlantsList!![position].plant.id != Hawk.get("my_plant_id")) {
                    Hawk.put("my_plant_id", myPlantsList!![position].plant.id)
                    Hawk.put("my_plant_name", myPlantsList!![position].plant.name)
                }
            }
        }

        binding.appLanguageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (languageList[position] != Hawk.get("app_language")) {
                    Hawk.put("app_language", languageList[position])
                }
            }
        }

        binding.themeModeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (themeList[position] != Hawk.get("theme_mode")) {
                    Hawk.put("theme_mode", themeList[position])
                }
            }
        }

        binding.addPhoneNumberBtn.setOnClickListener {
            phoneNumberAdapter.addPhoneNumber()
        }

        binding.workerInfoChangeBtn.setOnClickListener {
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        }

        binding.goLoginPage.setOnClickListener {
            findNavController().navigate(R.id.action_workerFragment_to_loginFragment)
        }

    }

    private fun initObserver() {

        connectivityManager.checkConnection()
        connectivityManager.observe(this) { checkConnection = it }

        loginViewModel.getMeInfo.observe(this) { apiResult ->
            apiResult.success {
                connectionDialog.dismissDialog()
                if (it != null) {
                    getMeInfo(it)
                }
            }
            apiResult.error {
                if (!checkConnection) connectionDialog.showDialog(Constants.GET_ME_INFO, Constants.NO_INTERNET, "Internet aloqasi mavjud emas !")
                else connectionError.checkConnectionError(it, connectionDialog, Constants.GET_ME_INFO)
            }
        }
    }

    private fun getMeInfo(meInfoModel: MeInfoModel) {
        Constants.meInfoModel = meInfoModel
        phoneNumberAdapter = PhoneNumberAdapter(meInfoModel.phones)
        binding.phoneNumberRv.adapter = phoneNumberAdapter
        binding.workerEditName.setText(meInfoModel.name)
        binding.workerEditUsername.setText(meInfoModel.username)
        binding.workerEditPassword.setText(Hawk.get<String>("user_password"))
        getPlantsList(meInfoModel.plant_users)

    }

    private fun getPlantsList(plantsList: ArrayList<PlantsModel>) {
        if (!plantsList.isNullOrEmpty()){
            myPlantsList = plantsList
            myPlantsNameList.clear()
            for (i in myPlantsList!!) {
                myPlantsNameList.add(i.plant.name)
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        when(refreshType) {
            Constants.GET_ME_INFO -> {
                loginViewModel.getMeInfo(Hawk.get("user_id"))
            }
            Constants.CHANGE_ME_INFO -> {

            }
        }
    }

}