package com.crudgroup.f9mobile.presentation.otherComponents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.LayoutPhoneNumberBinding
import com.crudgroup.f9mobile.presentation.otherComponents.model.PhoneNumberModel

class PhoneNumberAdapter(private val phoneList : ArrayList<PhoneNumberModel>) : RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder>() {

    class ViewHolder(val binding:LayoutPhoneNumberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutPhoneNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = phoneList[position]
        holder.binding.phoneNumber.text = item.number.replace("+", "")

    }
    override fun getItemCount() = phoneList.size

}