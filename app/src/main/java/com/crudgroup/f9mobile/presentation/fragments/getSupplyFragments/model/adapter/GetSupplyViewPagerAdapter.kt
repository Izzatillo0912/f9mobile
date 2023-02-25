package com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.GetSupplyFromEnterpriseFragment
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.GetSupplyFromSupplierFragment

class GetSupplyViewPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->  GetSupplyFromSupplierFragment()
            1 -> GetSupplyFromEnterpriseFragment()
            else -> GetSupplyFromSupplierFragment()
        }
    }
}