package com.crudgroup.f9mobile.presentation.fragments.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentProductionOrderInfoBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.dialog.ProductionDialog
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersViewModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrderInfoModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.OrdersAdapter
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.OrdersInfoAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.ConnectionError
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.MyConnectivityManager
import com.crudgroup.f9mobile.presentation.otherComponents.MyLoadStateAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ImageViewDialog
import kotlinx.coroutines.launch


class ProductionOrderInfoFragment : Fragment(), OrdersInfoAdapter.OrderInfoClickListener, ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentProductionOrderInfoBinding
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var imageViewDialog: ImageViewDialog
    private val ordersInfoAdapter by lazy { OrdersInfoAdapter(this) }
    private var checkConnected = true
    private var placeHolderPermission = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        connectivityManager = MyConnectivityManager(requireContext())
        imageViewDialog = ImageViewDialog(requireContext())
        ordersViewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductionOrderInfoBinding.inflate(inflater, container, false)
        onBackPressed()

        binding.appBar.pageTitle.text = requireArguments().getString("product_name")
        binding.appBar.backPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productionOrderInfoFragment_to_ordersFragment)
        }

        imageViewDialog.activeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersInfoAdapter.addLoadStateListener { loadStateManager(it) }

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.ordersInfoRv.adapter = ordersInfoAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter(),
            footer = MyLoadStateAdapter()
        )
    }

    private fun initObserver() {
        ordersViewModel.getProductionOrderInfo("", requireArguments().getInt("cycle_id")).observe(this) {
            lifecycleScope.launch {
                ordersInfoAdapter.submitData(it)
            }
        }
    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        ordersInfoAdapter.refresh()
        if(checkConnected) ordersViewModel.getProductionOrders("",0)
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.ordersInfoRv.visibility = View.GONE
        }else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.ordersInfoRv.visibility = View.VISIBLE
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
                if (!checkConnected) connectionDialog.showDialog(Constants.GET_PLANT_CYCLES, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
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

    private fun onBackPressed() {
        val onBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_productionOrderInfoFragment_to_ordersFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBack)
    }


    override fun connectDialogRefreshClicked(refreshType: String) {

    }

    override fun getSupplyBtn(productionOrderInfoModel: ProductionOrderInfoModel) {

    }
}