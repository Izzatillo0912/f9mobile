package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.ItemOrdersBinding
import com.crudgroup.f9mobile.databinding.ItemRawMaterialBinding
import com.crudgroup.f9mobile.databinding.ItemWarehouseCategoryBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.OrdersModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.RawMaterialModel
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class WorkerRawMaterialAdapter(private val rawMaterialItemClickListener: RawMaterialItemClickListener)
    : PagingDataAdapter<MaterialStoresModel, WorkerRawMaterialAdapter.ViewHolder>(OrdersDiffItemCallback()){

    interface RawMaterialItemClickListener {
        fun rawMaterialHistoryClicked(rawMaterialModel: MaterialStoresModel)
        fun rawMaterialGetSupplyClicked(rawMaterialModel: MaterialStoresModel)
    }

    class ViewHolder(val binding: ItemRawMaterialBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRawMaterialBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        if (item!=null) {
            holder.binding.apply {

                itemRawMaterialName.text = item.material.name
                itemRawMaterialQuantitiy.text = "Hajmi : ${item.value}  ${item.olchov.name}"

                rawMaterialHistoryBtn.setOnClickListener {
                    rawMaterialItemClickListener.rawMaterialHistoryClicked(item)
                }

                rawMaterialGetSupplyBtn.setOnClickListener {
                    rawMaterialItemClickListener.rawMaterialGetSupplyClicked(item)
                }
            }
        }
    }

    class OrdersDiffItemCallback : DiffUtil.ItemCallback<MaterialStoresModel>() {
        override fun areItemsTheSame(oldItem: MaterialStoresModel, newItem: MaterialStoresModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MaterialStoresModel, newItem: MaterialStoresModel): Boolean {
            return oldItem == newItem
        }
    }
}