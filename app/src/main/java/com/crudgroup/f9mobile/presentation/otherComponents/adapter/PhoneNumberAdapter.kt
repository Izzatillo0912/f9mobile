package com.crudgroup.f9mobile.presentation.otherComponents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.f9mobile.databinding.LayoutPhoneNumberBinding
import com.crudgroup.f9mobile.presentation.otherComponents.model.PhoneNumberModel


var phoneNumberList = ArrayList<PhoneNumberModel>() as MutableList<PhoneNumberModel>
class PhoneNumberAdapter(private val phoneList : ArrayList<PhoneNumberModel>) : RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder>() {

    private var itemCount: Int = 1

    class ViewHolder(val binding:LayoutPhoneNumberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutPhoneNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = phoneList[position]
        phoneNumberList.add(PhoneNumberModel(0, ""))

        holder.binding.apply {

            phoneNumber.setText(item.number.replace("+", ""))

            phoneNumber.addTextChangedListener {
                try {
                    phoneNumberList[position] = PhoneNumberModel(0,phoneNumber.text.toString())
                }
                catch (n : java.lang.NumberFormatException) {
                    phoneNumberList[position] = PhoneNumberModel(0, "")
                }

            }
        }
    }

    override fun getItemCount() = itemCount


    @SuppressLint("NotifyDataSetChanged")
    fun addPhoneNumber() {
        itemCount++
        phoneList.add(PhoneNumberModel(0, ""))
        notifyItemInserted(itemCount)
    }
}