package com.crudgroup.f9mobile.presentation.fragments.workerHistories.pagingAndAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.ItemGetSupplyHistoryBinding
import com.crudgroup.f9mobile.presentation.fragments.workerHistories.model.GetSupplyHistoryModel

class GetSupplyHistoryAdapter : PagingDataAdapter<GetSupplyHistoryModel, GetSupplyHistoryAdapter.ViewHolder>(GetSupplyHistoryDiffUtil()){

    class ViewHolder(val binding: ItemGetSupplyHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGetSupplyHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class GetSupplyHistoryDiffUtil : DiffUtil.ItemCallback<GetSupplyHistoryModel>() {
        override fun areItemsTheSame(oldItem: GetSupplyHistoryModel, newItem: GetSupplyHistoryModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GetSupplyHistoryModel, newItem: GetSupplyHistoryModel): Boolean {
            return oldItem == newItem
        }
    }

}