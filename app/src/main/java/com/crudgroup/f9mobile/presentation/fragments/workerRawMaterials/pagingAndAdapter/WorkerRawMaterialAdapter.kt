package com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.pagingAndAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.ItemRawMaterialBinding
import com.crudgroup.f9mobile.presentation.fragments.workerRawMaterials.model.MaterialStoresModel

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