package com.crudgroup.f9mobile.presentation.fragments.ordersFragment.paginationAndAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crudgroup.f9mobile.databinding.ItemOrdersBinding
import com.crudgroup.f9mobile.presentation.fragments.ordersFragment.model.ProductionOrdersModel
import com.crudgroup.f9mobile.presentation.otherComponents.Constants

class OrdersAdapter(private val plantCyclesClickListener: PlantCyclesClickListener)
    : PagingDataAdapter<ProductionOrdersModel, OrdersAdapter.ViewHolder>(OrdersDiffItemCallback()) {


    interface PlantCyclesClickListener {
        fun productionClicked(productionOrdersModel: ProductionOrdersModel)

        fun productImageClicked(productionOrdersModel: ProductionOrdersModel)

        fun productionOrderInfo(productionOrdersModel: ProductionOrdersModel)
    }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            if (item != null) {
                val image = Constants.IMAGE_BASE_URL + "products/" + item.product_image
                if (!image.isNullOrEmpty()) Glide
                    .with(root.context)
                    .load(image)
                    .centerCrop()
                    .into(orderImage)

                productionOrdersName.text = item.product_name
                productionOrdersVolume.text = "${item.quantity}  " + item.olchov_name
                customerName.text = item.customer_name
                cycleName.text = item.cycle_name

                productionBtn.setOnClickListener {
                    plantCyclesClickListener.productionClicked(item)
                }

                orderImage.setOnClickListener {
                    plantCyclesClickListener.productImageClicked(item)
                }

                productionOrderInfoBtn.setOnClickListener {
                    plantCyclesClickListener.productionOrderInfo(item)
                }
            }
        }

    }


    class OrdersDiffItemCallback : DiffUtil.ItemCallback<ProductionOrdersModel>() {
        override fun areItemsTheSame(
            oldItem: ProductionOrdersModel,
            newItem: ProductionOrdersModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductionOrdersModel,
            newItem: ProductionOrdersModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}