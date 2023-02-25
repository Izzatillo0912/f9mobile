package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.ItemOrdersBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersModel

class OrdersAdapter : PagingDataAdapter<OrdersModel, OrdersAdapter.ViewHolder>(OrdersDiffItemCallback()){

    class ViewHolder(val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrdersBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 20

    class OrdersDiffItemCallback : DiffUtil.ItemCallback<OrdersModel>() {
        override fun areItemsTheSame(oldItem: OrdersModel, newItem: OrdersModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrdersModel, newItem: OrdersModel): Boolean {
            return oldItem == newItem
        }
    }
}