package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.ItemOrderInfoBinding
import com.crudgroup.f9mobile.databinding.ItemOrdersBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrderInfoModel
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class OrdersInfoAdapter(private val orderInfoClickListener: OrderInfoClickListener)
    : PagingDataAdapter<ProductionOrderInfoModel, OrdersInfoAdapter.ViewHolder>(OrdersInfoDiffItemCallback()) {


    interface OrderInfoClickListener {
        fun getSupplyBtn(productionOrderInfoModel: ProductionOrderInfoModel)
    }

    class ViewHolder(val binding: ItemOrderInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            if (item != null) {

                infoProductName.text = item.component_items[0].material.name
                infoQuantity.text = "${item.component_items[0].value}  " + item.component_items[0].material.olchov_name

                orderGetSupplyBtn.setOnClickListener {
                    orderInfoClickListener.getSupplyBtn(item)
                }
            }
        }

    }


    class OrdersInfoDiffItemCallback : DiffUtil.ItemCallback<ProductionOrderInfoModel>() {
        override fun areItemsTheSame(
            oldItem: ProductionOrderInfoModel,
            newItem: ProductionOrderInfoModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductionOrderInfoModel,
            newItem: ProductionOrderInfoModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}