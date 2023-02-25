package com.crudgroup.f9mobile.presentation.fragments.workerHistories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentAllHistoriesBinding
import com.crudgroup.f9mobile.presentation.otherComponents.FilterAndSearchBar

class AllHistoriesFragment : Fragment() {

    private lateinit var binding: FragmentAllHistoriesBinding
    private lateinit var filterAndSearchBar: FilterAndSearchBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterAndSearchBar = FilterAndSearchBar(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllHistoriesBinding.inflate(inflater, container, false)
        filterAndSearchBar.activeHistoryFragmentFilterAndSearchBar(binding)
        return binding.root
    }
}