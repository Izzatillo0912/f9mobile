package com.crudgroup.f9mobile.presentation.fragments.workerHistories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentGetSupplyHistoryBinding
import com.crudgroup.f9mobile.presentation.fragments.getSupplyFragments.model.GetSupplyViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.HistoryViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.pagingAndAdapter.GetSupplyHistoryAdapter
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.WorkerRawMaterialsViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter.WorkerRawMaterialAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.*
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.FilterDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class GetSupplyHistoryFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentGetSupplyHistoryBinding
    private lateinit var filterDialog: FilterDialog
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var historyViewModel: HistoryViewModel
    private val contentFlow = MutableSharedFlow<String>()
    private val getSupplyHistoryAdapter: GetSupplyHistoryAdapter by lazy { GetSupplyHistoryAdapter() }
    private var checkConnected = true
    private var placeHolderPermission = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterDialog = FilterDialog(this)
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        connectivityManager = MyConnectivityManager(requireContext())
        historyViewModel = ViewModelProvider(this)[HistoryViewModel :: class.java]

        initObserver()
        subscribeFlows()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGetSupplyHistoryBinding.inflate(inflater, container, false)
        binding.appBar.pageTitle.text = requireArguments().getString("material_name").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSupplyHistoryAdapter.addLoadStateListener { loadStateManager(it) }

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.getSupplyHistoryRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
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
                historyViewModel.getSupplyHistory(0,"", "", it).observe(viewLifecycleOwner){
                    lifecycleScope.launch {
                        getSupplyHistoryAdapter.submitData(it)
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
            historyViewModel.getSupplyHistory(0,"","","").observe(this){
                lifecycleScope.launch {
                    getSupplyHistoryAdapter.submitData(it)
                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        getSupplyHistoryAdapter.refresh()
        if(checkConnected) historyViewModel.getSupplyHistory(0,"","","")
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.getSupplyHistoryRv.visibility = View.GONE
        }else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.getSupplyHistoryRv.visibility = View.VISIBLE
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
                else connectionError.checkConnectionError(refreshState.error, connectionDialog, "")
            }
        }

        when(val appendState = it.append){
            is LoadState.Loading -> {}
            is LoadState.NotLoading -> {}
            is LoadState.Error -> {
                connectionError.checkConnectionError(appendState.error, connectionDialog, "")
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.searchInput.text.clear()
    }
}