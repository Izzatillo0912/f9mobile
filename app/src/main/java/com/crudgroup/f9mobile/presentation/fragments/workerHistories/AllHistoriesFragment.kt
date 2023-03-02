package com.crudgroup.f9mobile.presentation.fragments.workerHistories

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentAllHistoriesBinding
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.HistoryViewModel
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.pagingAndAdapter.GetSupplyHistoryAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.*
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.FilterDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class AllHistoriesFragment : Fragment(), ConnectionDialog.RefreshClicked, FilterDialog.FilterBtnClicked {

    private lateinit var binding: FragmentAllHistoriesBinding
    private lateinit var filterDialog: FilterDialog
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var historyViewModel: HistoryViewModel
    private val contentFlow = MutableSharedFlow<String>()
    private val getSupplyHistoryAdapter: GetSupplyHistoryAdapter by lazy { GetSupplyHistoryAdapter() }
    private var checkConnected = true
    private var placeHolderPermission = true
    private var productionHistory = true
    private var supplyHistory = false
    private var transferHistory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterDialog = FilterDialog(this, this)
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        connectivityManager = MyConnectivityManager(requireContext())
        historyViewModel = ViewModelProvider(this)[HistoryViewModel :: class.java]

        initObserver()
        subscribeFlows()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllHistoriesBinding.inflate(inflater, container, false)
        binding.appBar.pageTitle.text = "Tarixlar"
        binding.appBar.backPageBtn.visibility = View.GONE
        binding.productionHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_black_btn)
        binding.supplyHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
        binding.transferHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
        binding.productionHistoryBtn.setTextColor(Color.parseColor("#F0F0F3"))
        binding.supplyHistoryBtn.setTextColor(Color.parseColor("#2e3133"))
        binding.transferHistoryBtn.setTextColor(Color.parseColor("#2e3133"))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSupplyHistoryAdapter.addLoadStateListener { loadStateManager(it) }

        binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter(),
            footer = MyLoadStateAdapter(),
        )

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.appBar.searchInput.addTextChangedListener {
            emitContent(it.toString())
        }

        binding.appBar.filterBtn.setOnClickListener {
            filterDialog.showDialog()
        }

        binding.productionHistoryBtn.setOnClickListener {
            if (!productionHistory) {
                productionHistory = true
                supplyHistory = false
                transferHistory = false
                binding.productionHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_black_btn)
                binding.supplyHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.transferHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.productionHistoryBtn.setTextColor(Color.parseColor("#F0F0F3"))
                binding.supplyHistoryBtn.setTextColor(Color.parseColor("#2e3133"))
                binding.transferHistoryBtn.setTextColor(Color.parseColor("#2e3133"))

//                binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
//                    header = MyLoadStateAdapter(),
//                    footer = MyLoadStateAdapter(),
//                )
            }
        }

        binding.supplyHistoryBtn.setOnClickListener {
            if (!supplyHistory) {
                productionHistory = false
                supplyHistory = true
                transferHistory = false
                binding.supplyHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_black_btn)
                binding.productionHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.transferHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.supplyHistoryBtn.setTextColor(Color.parseColor("#F0F0F3"))
                binding.productionHistoryBtn.setTextColor(Color.parseColor("#2e3133"))
                binding.transferHistoryBtn.setTextColor(Color.parseColor("#2e3133"))

                binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
                    header = MyLoadStateAdapter(),
                    footer = MyLoadStateAdapter(),
                )
            }
        }

        binding.transferHistoryBtn.setOnClickListener {
            if (!transferHistory) {
                productionHistory = false
                supplyHistory = false
                transferHistory = true
                binding.transferHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_black_btn)
                binding.productionHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.supplyHistoryBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ripple_white_btn)
                binding.transferHistoryBtn.setTextColor(Color.parseColor("#F0F0F3"))
                binding.productionHistoryBtn.setTextColor(Color.parseColor("#2e3133"))
                binding.supplyHistoryBtn.setTextColor(Color.parseColor("#2e3133"))

//                binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
//                    header = MyLoadStateAdapter(),
//                    footer = MyLoadStateAdapter(),
//                )
            }
        }
    }

    private fun subscribeFlows() = lifecycleScope.launch {
        contentFlow
            .debounce(500)
            .collect {
                when {
                    productionHistory -> {

                    }

                    supplyHistory -> {
                        historyViewModel.getSupplyHistory(0,"", "", it).observe(viewLifecycleOwner){
                            lifecycleScope.launch {
                                getSupplyHistoryAdapter.submitData(it)
                            }
                        }

                        binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
                            header = MyLoadStateAdapter(),
                            footer = MyLoadStateAdapter(),
                        )
                    }

                    transferHistory -> {

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

        if(checkConnected) {
            when {

                productionHistory -> {

                }

                supplyHistory -> {
                    historyViewModel.getSupplyHistory(0,"", "", "").observe(viewLifecycleOwner){
                        lifecycleScope.launch {
                            getSupplyHistoryAdapter.submitData(it)
                        }
                    }

                    binding.historiesRv.adapter = getSupplyHistoryAdapter.withLoadStateHeaderAndFooter(
                        header = MyLoadStateAdapter(),
                        footer = MyLoadStateAdapter(),
                    )
                }

                transferHistory -> {

                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        getSupplyHistoryAdapter.refresh()
        if(checkConnected) {
            when {
                productionHistory -> {

                }

                supplyHistory -> {
                    historyViewModel.getSupplyHistory(0,"","","")
                }

                transferHistory -> {

                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.historiesRv.visibility = View.GONE
        }else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.historiesRv.visibility = View.VISIBLE
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
        if(checkConnected) {
            when(refreshType) {
                "productionHistory" -> {

                }

                "supplyHistory" -> {
                    historyViewModel.getSupplyHistory(0,"","","")
                }

                "transferHistory" -> {

                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")

    }

    override fun onResume() {
        super.onResume()
        binding.appBar.searchInput.text.clear()
    }

    override fun clearFilterClicked() {

    }

    override fun setFilterClicked(fromDate: String, toDate: String) {

    }
}