package com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.paginAndAndapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.ItemWarehouseCategoryBinding
import com.crudgroup.f9mobile.presentation.fragments.workerWarehouse.model.WarehouseCategoryModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class WarehouseCategoryAdapter(private val categoryItemClickListener: CategoryItemClickListener) : PagingDataAdapter<WarehouseCategoryModel, WarehouseCategoryAdapter.ViewHolder>(OrdersDiffItemCallback()){

    interface CategoryItemClickListener {
        fun categoryItemClicked(warehouseCategoryModel: WarehouseCategoryModel)
    }

    class ViewHolder(val binding: ItemWarehouseCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWarehouseCategoryBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        if (item!=null) {
            holder.binding.apply {
                val image = Constants.IMAGE_BASE_URL + "material_types/" + item.Material_Type.image
                if (!image.isNullOrBlank()) Glide.with(root.context).load(image).into(warehouseCategoryImage)

                warehouseCategoryName.text = item.Material_Type.name
                itemCountInCategory.text = "Turi : ${item.material_count} ta"

                categoryRightArrow.setOnClickListener {
                    categoryItemClickListener.categoryItemClicked(item)
                }
            }
        }
    }

    class OrdersDiffItemCallback : DiffUtil.ItemCallback<WarehouseCategoryModel>() {
        override fun areItemsTheSame(oldItem: WarehouseCategoryModel, newItem: WarehouseCategoryModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WarehouseCategoryModel, newItem: WarehouseCategoryModel): Boolean {
            return oldItem == newItem
        }
    }
}