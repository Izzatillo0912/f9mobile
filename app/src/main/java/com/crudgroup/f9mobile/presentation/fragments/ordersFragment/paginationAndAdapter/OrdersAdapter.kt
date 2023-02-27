package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.ItemOrdersBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.PlantCyclesModel

class OrdersAdapter :
    PagingDataAdapter<PlantCyclesModel, OrdersAdapter.ViewHolder>(OrdersDiffItemCallback()) {

    class ViewHolder(val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrdersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            orderProductName.text = item!!.cycle.product.name
        }

    }


    class OrdersDiffItemCallback : DiffUtil.ItemCallback<PlantCyclesModel>() {
        override fun areItemsTheSame(
            oldItem: PlantCyclesModel,
            newItem: PlantCyclesModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PlantCyclesModel,
            newItem: PlantCyclesModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}