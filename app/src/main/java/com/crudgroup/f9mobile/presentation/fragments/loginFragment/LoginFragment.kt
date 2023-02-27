package com.crudgroup.f9mobile.presentation.fragments.loginFragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentLoginBinding
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.model.LoginViewModel
import com.crudgroup.f9mobile.presentation.otherComponents.*
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.orhanobut.hawk.Hawk


class LoginFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var allAnimations: AllAnimations
    private lateinit var viewModel : LoginViewModel
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private var isConnected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allAnimations = AllAnimations(requireContext())
        viewModel = ViewModelProvider(this)[LoginViewModel:: class.java]
        connectivityManager = MyConnectivityManager(requireContext())
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())

        initObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allAnimations.loginTitleAnim(binding.loginTitle)
        allAnimations.loginFurnitureAnim(binding.loginFurniture)
        allAnimations.loginInputCardAnim(binding.loginInputCard)

        binding.loginSignInBtn.setOnClickListener {
            getToken()
        }
    }

    private fun initObservers() {

        connectivityManager.checkConnection()
        connectivityManager.observe(this) { isConnected = it }

        viewModel.getTokenData.observe(this) { apiResult ->

            val message = apiResult.message

            apiResult.success {

                if (it!!.user.role == Constants.ROLE_PLANT_ADMIN) {
                    Hawk.put("token", it.access_token)
                    viewModel.getMeInfo(it.user.id)
                }
                else connectionDialog.animationChanger(Constants.IS_NOT_CHECKED, "$message")
            }

            apiResult.error {
                connectionError.checkConnectionError(it, connectionDialog, "")
            }
        }

        viewModel.getMeInfo.observe(this) { it ->
            it.success {
                if (it != null) {
                    Constants.meInfoModel = it
                    Hawk.put("user_id", it.id)
                    Hawk.put("user_username", binding.loginUsernameInput.text.toString())
                    Hawk.put("user_password", binding.loginPasswordInput.text.toString())
                    Hawk.put("my_plant_id", it.plant_users[0].plant.id)
                    Hawk.put("my_plant_name", it.plant_users[0].plant.name)
                    connectionDialog.animationChanger(Constants.IS_CHECK_API, "Shaxsingiz tasdiqlandi ! Xush kelibsiz")
                    object : CountDownTimer(1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            findNavController().navigate(R.id.action_loginFragment_to_workerFragment)
                        }
                    }.start()
                }
            }
            it.error {
                connectionError.checkConnectionError(it, connectionDialog, "")
            }
        }
    }

    private fun getToken() {
        if(isConnected) {
            val username = binding.loginUsernameInput.text.toString()
            val password = binding.loginPasswordInput.text.toString()

            when {
                username.isEmpty() -> {
                    binding.loginUsernameInput.error = "Loginni kiriting"
                }
                password.isEmpty() -> {
                    binding.loginPasswordInput.error = "parolni kiriting"
                }
                else -> {
                    connectionDialog.showDialog(Constants.GET_TOKEN, Constants.IS_LOADING,"Malumotlaringiz qidirilmoqda ...")
                    viewModel.getToken(username, password)
                }
            }
        }
        else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET,"Internet aloqasi mavjud emas !")
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        if(isConnected) getToken()

        else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "Internet aloqasi mavjud emas !")
    }
}