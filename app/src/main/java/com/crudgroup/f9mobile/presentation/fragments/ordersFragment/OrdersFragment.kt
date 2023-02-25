package com.crudgroup.f9mobile.presentation.fragments.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.crudgroup.f9mobile.databinding.FragmentOrdersBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.OrdersViewPagerAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.FilterAndSearchBar
import com.crudgroup.f9mobile.presentation.otherComponents.ViewPagerTabLayout


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var filterAndSearchBar: FilterAndSearchBar
    private lateinit var viewPagerTabLayout: ViewPagerTabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterAndSearchBar = FilterAndSearchBar(this)
        viewPagerTabLayout = ViewPagerTabLayout(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        filterAndSearchBar.activeOrdersFragmentFilterAndSearchBar(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerTabLayout.connectViewPager(binding.appBar.ordersPageBtn, binding.appBar.finishedOrdersPageBtn, binding.getOrdersChangeViewPager)
        binding.getOrdersChangeViewPager.adapter = OrdersViewPagerAdapter(this)

        binding.getOrdersChangeViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerTabLayout.changerBtnColor(position)
            }
        })

    }
}