package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.FinishedOrdersFragment
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.OrdersPageFragment

class OrdersViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) OrdersPageFragment()
        else FinishedOrdersFragment()
    }
}