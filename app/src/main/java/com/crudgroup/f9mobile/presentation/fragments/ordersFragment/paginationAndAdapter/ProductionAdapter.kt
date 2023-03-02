package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.ItemDetailProductionBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.DetailModel

class ProductionAdapter(private val detailProductionList : ArrayList<DetailModel>, val productionClickListener: ProductionClickListener)
    : RecyclerView.Adapter<ProductionAdapter.ViewHolder>() {

    interface ProductionClickListener{
        fun checkProductionClicked(detailModel: DetailModel)
    }

    class ViewHolder(val binding: ItemDetailProductionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDetailProductionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = detailProductionList[position]

        holder.binding.apply {
            infoProductName.text = item.productModel.name

            productionBtn.setOnClickListener {
                productionClickListener.checkProductionClicked(item)
            }
        }
    }

    override fun getItemCount() = detailProductionList.size
}