package com.crudgroup.f9mobile.presentation.fragments.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentFinishedOrdersBinding


class FinishedOrdersFragment : Fragment() {

    private lateinit var binding: FragmentFinishedOrdersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFinishedOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

}