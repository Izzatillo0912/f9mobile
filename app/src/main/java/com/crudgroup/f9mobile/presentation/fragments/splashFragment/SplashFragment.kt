package com.crudgroup.f9mobile.presentation.fragments.splashFragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentSplashBinding
import com.crudgroup.f9mobile.presentation.fragments.loginFragment.model.LoginViewModel
import com.crudgroup.f9mobile.presentation.otherComponents.AllAnimations
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.orhanobut.hawk.Hawk


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var allAnimations: AllAnimations
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        allAnimations = AllAnimations(requireContext())

        getMeInfo()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSplashBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allAnimations.splashLogoAnim(binding.splashLogoImage)

        object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val id : Int = Hawk.get("my_id", 0)
                if (id == 0) findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                else loginViewModel.getMeInfo(id)

            }
        }.start()

    }

    private fun getMeInfo() {

        loginViewModel.getMeInfo.observe(this) { it ->
            it.success {
                Constants.meInfoModel = it
                findNavController().navigate(R.id.action_splashFragment_to_workerFragment)
            }
            it.error {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }

    }
}