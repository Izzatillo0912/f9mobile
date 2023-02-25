package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.data.database.LocaleCaches.getSupplyAlertDialog
import com.crudgroup.f9mobile.databinding.DialogGetSupplyBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.adapter.GetSupplyViewPagerAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.ViewPagerTabLayout

class GetSupplyDialog(val fragment: Fragment) {

    private var getSupplyDialog = AlertDialog.Builder(fragment.requireContext()).create()
    private var binding: DialogGetSupplyBinding = DialogGetSupplyBinding.inflate(LayoutInflater.from(fragment.requireContext()))
    private val viewPagerTabLayout = ViewPagerTabLayout(fragment.requireContext())
    init {
        getSupplyAlertDialog = getSupplyDialog
        getSupplyDialog.setView(binding.root)
        getSupplyDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showGetSupplyDialog() {
        getSupplyDialog.show()

        viewPagerTabLayout.connectViewPager(binding.getSupplyFromSupplier, binding.getSupplyFromEnterprice, binding.getSupplyChangeViewPager)
        binding.getSupplyChangeViewPager.adapter = GetSupplyViewPagerAdapter(fragment)

        binding.getSupplyChangeViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerTabLayout.changerBtnColor(position)
            }
        })

        binding.getSupplDialogCloseBtn.setOnClickListener {
            getSupplyDialog.dismiss()
        }
    }
}