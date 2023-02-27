package com.crudgroup.f9mobile.presentation.fragments.workerWarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentWorkerWarehouseBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.GetSupplyDialog
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.OrdersAdapter
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.paginAndAndapter.WarehouseCategoryAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.*
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.FilterDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.TimberCalculatorDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class WorkerWarehouseFragment : Fragment(), ConnectionDialog.RefreshClicked, WarehouseCategoryAdapter.CategoryItemClickListener {

    private lateinit var binding: FragmentWorkerWarehouseBinding
    private lateinit var warehouseViewModel : WarehouseViewModel
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var filterDialog: FilterDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var getSupplyDialog: GetSupplyDialog
    private val contentFlow = MutableSharedFlow<String>()
    private val warehouseCategoryAdapter: WarehouseCategoryAdapter by lazy { WarehouseCategoryAdapter(this) }
    private var checkConnection = true
    private var placeHolderPermission = true
    private var searchOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityManager = MyConnectivityManager(requireContext())
        connectionError = ConnectionError(requireContext())
        connectionDialog = ConnectionDialog(requireContext(), this)
        getSupplyDialog = GetSupplyDialog(this)
        filterDialog = FilterDialog(this)
        warehouseViewModel = ViewModelProvider(this)[WarehouseViewModel :: class.java]

        initObservers()
        subscribeFlows()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerWarehouseBinding.inflate(inflater, container, false)
        binding.appBar.backPageBtn.visibility = View.GONE
        binding.appBar.pageTitle.text = "Hom ashyo turlari"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        warehouseCategoryAdapter.addLoadStateListener { loadStateManager(it) }

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.workerWarehouseCategoryRv.adapter = warehouseCategoryAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter(),
            footer = MyLoadStateAdapter()
        )

        binding.openGetSupplyDialogBtn.setOnClickListener {
            getSupplyDialog.showGetSupplyDialog()
        }

        binding.appBar.filterBtn.setOnClickListener {
            filterDialog.showDialog()
        }

        binding.appBar.searchInput.addTextChangedListener {
            emitContent(it.toString())
        }
    }

    private fun subscribeFlows() = lifecycleScope.launch {
        contentFlow
            .debounce(500)
            .collect {
                warehouseViewModel.getWarehouseCategory(it, connectionDialog).observe(viewLifecycleOwner){
                    lifecycleScope.launch {
                        warehouseCategoryAdapter.submitData(it)
                    }
                }
            }
    }

    private fun emitContent(content: String) = lifecycleScope.launch{
        contentFlow.emit(content)
    }

    private fun initObservers() {

        connectivityManager.checkConnection()
        connectivityManager.observe(this) { checkConnection = it }

        if(checkConnection){
            warehouseViewModel.getWarehouseCategory("", connectionDialog).observe(this){
                lifecycleScope.launch {
                    warehouseCategoryAdapter.submitData(it)
                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_CATEGORIES, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")


    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        warehouseCategoryAdapter.refresh()
        if(checkConnection) warehouseViewModel.getWarehouseCategory("", connectionDialog)
        else connectionDialog.showDialog(Constants.GET_CATEGORIES, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.workerWarehouseCategoryRv.visibility = View.GONE
        }else{
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.workerWarehouseCategoryRv.visibility = View.VISIBLE
        }
    }

    private fun loadStateManager(it: CombinedLoadStates){
        when(val refreshState = it.refresh){
            is LoadState.Loading -> {
                if (placeHolderPermission) {
                    shimmerLayoutVisible(true)
                }
            }
            is LoadState.NotLoading -> {
                shimmerLayoutVisible(false)
                placeHolderPermission = false
                binding.refreshLayout.isRefreshing = false
            }
            is LoadState.Error -> {
                binding.refreshLayout.isRefreshing = false
                if (!checkConnection) connectionDialog.showDialog(Constants.GET_CATEGORIES, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
                else connectionError.checkConnectionError(refreshState.error, connectionDialog, Constants.GET_CATEGORIES)
            }
        }

        when(val appendState = it.append){
            is LoadState.Loading -> {}
            is LoadState.NotLoading -> {}
            is LoadState.Error -> {
                connectionError.checkConnectionError(appendState.error, connectionDialog, Constants.GET_CATEGORIES)
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        when(refreshType) {
            Constants.GET_CATEGORIES -> { initObservers() }
        }
    }

    override fun categoryItemClicked(warehouseCategoryModel: WarehouseCategoryModel) {
        val bundle = Bundle()
        bundle.putInt("category_id", warehouseCategoryModel.Material_Type.id)
        bundle.putString("category_name", warehouseCategoryModel.Material_Type.name)
        findNavController().navigate(R.id.action_workerWarehouseFragment_to_workerRawMaterialsFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.searchInput.text.clear()
    }
}