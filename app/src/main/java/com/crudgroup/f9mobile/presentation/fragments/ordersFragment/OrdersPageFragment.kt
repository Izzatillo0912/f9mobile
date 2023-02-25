package com.crudgroup.f9mobile.presentation.fragments.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentOrdersPageBinding

class OrdersPageFragment : Fragment() {


    private lateinit var binding: FragmentOrdersPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrdersPageBinding.inflate(inflater, container, false)
        return binding.root
    }
}