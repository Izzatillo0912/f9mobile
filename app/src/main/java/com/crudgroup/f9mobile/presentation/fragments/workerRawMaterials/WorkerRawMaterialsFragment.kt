package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentWorkerRawMaterialsBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.GetSupplyDialog
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.RawMaterialModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.WorkerRawMaterialsViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter.WorkerRawMaterialAdapter
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.paginAndAndapter.WarehouseCategoryAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.*
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.FilterDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class WorkerRawMaterialsFragment : Fragment(), ConnectionDialog.RefreshClicked, WorkerRawMaterialAdapter.RawMaterialItemClickListener {

    private lateinit var binding: FragmentWorkerRawMaterialsBinding
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var filterDialog : FilterDialog
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var workerRawMaterialsViewModel: WorkerRawMaterialsViewModel
    private lateinit var getRawMaterialDialog: GetRawMaterialDialog
    private val contentFlow = MutableSharedFlow<String>()
    private val workerRawMaterialAdapter: WorkerRawMaterialAdapter by lazy { WorkerRawMaterialAdapter(this) }
    private var checkConnected = true
    private var placeHolderPermission = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectionDialog = ConnectionDialog(requireContext(),this)
        connectivityManager = MyConnectivityManager(requireContext())
        connectionError = ConnectionError(requireContext())
        filterDialog = FilterDialog(this)
        workerRawMaterialsViewModel = ViewModelProvider(this)[WorkerRawMaterialsViewModel :: class.java]
        initObserver()
        subscribeFlows()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkerRawMaterialsBinding.inflate(inflater, container, false)
        binding.appBar.pageTitle.text = requireArguments().getString("category_name").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workerRawMaterialAdapter.addLoadStateListener { loadStateManager(it) }

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.workerRawMaterialsRv.adapter = workerRawMaterialAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter(),
            footer = MyLoadStateAdapter(),
        )

        binding.appBar.backPageBtn.setOnClickListener {
            findNavController().popBackStack()
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
                workerRawMaterialsViewModel.getWorkerRawMaterials(requireArguments().getInt("category_id"), it).observe(viewLifecycleOwner){
                    lifecycleScope.launch {
                        workerRawMaterialAdapter.submitData(it)
                    }
                }
            }
    }

    private fun emitContent(content: String) = lifecycleScope.launch{
        contentFlow.emit(content)
    }

    private fun initObserver(){

        connectivityManager.checkConnection()
        connectivityManager.observe(this) { checkConnected = it }

        if(checkConnected){
            workerRawMaterialsViewModel.getWorkerRawMaterials(requireArguments().getInt("category_id"), "").observe(this){
                lifecycleScope.launch {
                    workerRawMaterialAdapter.submitData(it)
                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")


        workerRawMaterialsViewModel.supplyCreateLiveData.observe(this) { apiResult ->
            apiResult.success {
                refreshAllData()
            }
            apiResult.error {
                connectionError.checkConnectionError(it, connectionDialog, Constants.GET_RAW_MATERIALS)
            }
        }

        workerRawMaterialsViewModel.materialTransferLiveData.observe(this) { apiResult ->
            apiResult.success {
                refreshAllData()
            }
            apiResult.error {
                connectionError.checkConnectionError(it, connectionDialog, Constants.GET_RAW_MATERIALS)
            }
        }

    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        workerRawMaterialAdapter.refresh()
        if(checkConnected) workerRawMaterialsViewModel.getWorkerRawMaterials(requireArguments().getInt("category_id"),"")
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.workerRawMaterialsRv.visibility = View.GONE
        }else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.workerRawMaterialsRv.visibility = View.VISIBLE
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
                if (!checkConnected) connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
                else connectionError.checkConnectionError(refreshState.error, connectionDialog, Constants.GET_RAW_MATERIALS)
            }
        }

        when(val appendState = it.append){
            is LoadState.Loading -> {}
            is LoadState.NotLoading -> {}
            is LoadState.Error -> {
                connectionError.checkConnectionError(appendState.error, connectionDialog, Constants.GET_RAW_MATERIALS)
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        when(refreshType) {
            Constants.GET_RAW_MATERIALS -> { initObserver() }
        }
    }

    override fun rawMaterialHistoryClicked(rawMaterialModel: MaterialStoresModel) {
        val bundle = Bundle()
        bundle.putString("material_name", rawMaterialModel.material.name)
        bundle.putInt("material_id", rawMaterialModel.material_id)
        findNavController().navigate(R.id.action_workerRawMaterialsFragment_to_getSupplyHistoryFragment, bundle)
    }

    override fun rawMaterialGetSupplyClicked(rawMaterialModel: MaterialStoresModel) {
        getRawMaterialDialog = GetRawMaterialDialog(this)
        getRawMaterialDialog.showGetSupplyDialog(rawMaterialModel)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.searchInput.text.clear()
    }
}