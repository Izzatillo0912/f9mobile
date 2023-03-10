package com.crudgroup.f9mobile.presentation.fragments.ordersFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentOrdersPageBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.dialog.ProductionDialog
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersViewModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionCreateModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter.OrdersAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.error
import com.crudgroup.f9mobile.presentation.otherComponents.ApiResult.Companion.success
import com.crudgroup.f9mobile.presentation.otherComponents.ConnectionError
import com.crudgroup.f9mobile.presentation.otherComponents.Constants
import com.crudgroup.f9mobile.presentation.otherComponents.MyConnectivityManager
import com.crudgroup.f9mobile.presentation.otherComponents.MyLoadStateAdapter
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ConnectionDialog
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.ImageViewDialog
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch

class OrdersPageFragment : Fragment(), ConnectionDialog.RefreshClicked, OrdersAdapter.PlantCyclesClickListener,
    ProductionDialog.ProductionClickListener {


    private lateinit var binding: FragmentOrdersPageBinding
    private lateinit var connectivityManager: MyConnectivityManager
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var imageViewDialog: ImageViewDialog
    private lateinit var productionDialog: ProductionDialog
    private val ordersAdapter: OrdersAdapter by lazy { OrdersAdapter(this) }
    private var checkConnected = true
    private var placeHolderPermission = true
    private var productionCreateModel : ProductionCreateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        connectivityManager = MyConnectivityManager(requireContext())
        imageViewDialog = ImageViewDialog(requireContext())
        productionDialog = ProductionDialog(requireContext(), this)
        ordersViewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersPageBinding.inflate(inflater, container, false)
        imageViewDialog.activeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersAdapter.addLoadStateListener { loadStateManager(it) }

        binding.refreshLayout.setOnRefreshListener {
            refreshAllData()
        }

        binding.ordersRv.adapter = ordersAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter(),
            footer = MyLoadStateAdapter()
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {

        connectivityManager.checkConnection()
        connectivityManager.observe(this) { checkConnected = it }

        if(checkConnected){
            ordersViewModel.getProductionOrders("", Hawk.get("my_plant_id")).observe(this) {
                lifecycleScope.launch {
                    ordersAdapter.submitData(it)
                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")

        ordersViewModel.productionCreateLiveDate.observe(this) { apiResult ->
            apiResult.success {
                productionDialog.dismissDialog()
                connectionDialog.showDialog(Constants.IS_CHECK_API, Constants.IS_CHECK_API, "Ishlab chiqarilganligi tasdiqlandi !!")
                ordersViewModel.getProductionOrders("", Hawk.get("my_plant_id")).observe(this) {
                    lifecycleScope.launch {
                        ordersAdapter.submitData(it)
                    }
                }
            }
            apiResult.error {
                connectionError.checkConnectionError(it, connectionDialog, Constants.PRODUCTION_CREATE)
            }
        }
    }

    private fun refreshAllData(){
        binding.refreshLayout.isRefreshing = true
        ordersAdapter.refresh()
        if(checkConnected) ordersViewModel.getProductionOrders("",0)
        else connectionDialog.showDialog(Constants.GET_RAW_MATERIALS, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    private fun shimmerLayoutVisible(visible: Boolean){
        if (visible){
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.ordersRv.visibility = View.GONE
        }else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout.stopShimmer()
            binding.ordersRv.visibility = View.VISIBLE
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

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()

        if(checkConnected) {
            when(refreshType) {
                Constants.PRODUCTION_CREATE -> {
                    productionCreateModel?.let {
                        connectionDialog.showDialog(Constants.PRODUCTION_CREATE, Constants.IS_LOADING, "So'rov yuborilmoqda...")
                        ordersViewModel.productionCreate(it)
                    }
                }
            }
        }
        else connectionDialog.showDialog(Constants.GET_PLANT_CYCLES, Constants.NO_INTERNET, "Tarmoq bilan aloqa mavjud emas !!")
    }

    override fun productionClicked(productionOrdersModel: ProductionOrdersModel) {
        productionDialog.showDialog(productionOrdersModel)
    }

    override fun productImageClicked(productionOrdersModel: ProductionOrdersModel) {
        val image = Constants.IMAGE_BASE_URL + "products/" + productionOrdersModel.product_image
        imageViewDialog.showImage(image)
    }

    override fun productionOrderInfo(productionOrdersModel: ProductionOrdersModel) {
        val bundle = Bundle()
        bundle.putString("product_name", productionOrdersModel.product_name)
        bundle.putInt("cycle_id",productionOrdersModel.cycle_id)
        findNavController().navigate(R.id.action_ordersFragment_to_productionOrderInfoFragment, bundle)
    }

    override fun checkProductionClicked(productionCreateModel: ProductionCreateModel) {
        this.productionCreateModel = productionCreateModel
        if (checkConnected) {
            connectionDialog.showDialog(Constants.PRODUCTION_CREATE, Constants.IS_LOADING, "So'rov yuborilmoqda...")
            ordersViewModel.productionCreate(productionCreateModel)
        }
        else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "Internet aloqasi mavjud emas !!")

    }
}