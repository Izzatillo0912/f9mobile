package com.crudgroup.f9mobile.presentation.appAndActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.ActivityMainBinding
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.orhanobut.hawk.Hawk
import javax.annotation.meta.When

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideBottomNavigationPanel()
        setAppTheme()
        gooLoginPage()
    }

    private fun hideBottomNavigationPanel() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private fun setAppTheme() {
        when(Hawk.get<String>("theme_mode")) {
            "Tongi rejim" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Tungi rejim" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> {
                Hawk.put("theme_mode", "Tongi rejim")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun gooLoginPage() {
        Constants.loginBtnListener.observe(this) {
            if (it) {
                Navigation.findNavController(binding.mainContainer).navigate(R.id.loginFragment)
                Constants.loginBtnListener.postValue(false)
            }
        }
    }
}