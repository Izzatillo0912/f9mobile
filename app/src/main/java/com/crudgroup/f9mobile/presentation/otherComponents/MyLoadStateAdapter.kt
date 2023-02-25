package com.crudgroup.f9mobile.presentation.otherComponents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.LayoutItemLoadingBinding

class MyLoadStateAdapter : LoadStateAdapter<MyLoadStateAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(LayoutItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        with(holder){
            holder.itemView.isVisible = loadState is LoadState.Loading
        }
    }


}