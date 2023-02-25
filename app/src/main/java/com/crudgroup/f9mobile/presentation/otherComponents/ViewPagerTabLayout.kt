package com.crudgroup.f9mobile.presentation.otherComponents

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.LayoutGetMaterialFromSupplierBinding

class ViewPagerTabLayout(val context: Context) {

    var leftBtn : TextView? = null
    var rightBtn : TextView? = null
    var viewPager2 : ViewPager2? = null

    fun connectViewPager(leftBtn : TextView, rightBtn : TextView, viewPager2: ViewPager2) {

        this.leftBtn = leftBtn
        this.rightBtn = rightBtn
        this.viewPager2 = viewPager2

        leftBtn.setOnClickListener {
            viewPager2.currentItem = 0
        }
        rightBtn.setOnClickListener {
            viewPager2.currentItem = 1
        }
    }

    fun connectBtn(leftBtn: TextView, rightBtn: TextView) {
        this.leftBtn = leftBtn
        this.rightBtn = rightBtn
    }

    fun changerBtnColor(position : Int) {

        if (position == 0) {
            leftBtn!!.background = ContextCompat.getDrawable(context, R.drawable.ripple_black_no_corner)
            rightBtn!!.background = ContextCompat.getDrawable(context, R.drawable.ripple_white_no_corner)
            leftBtn!!.setTextColor(Color.parseColor("#F0F0F3"))
            rightBtn!!.setTextColor(Color.parseColor("#2e3133"))
        }
        else {
            rightBtn!!.background = ContextCompat.getDrawable(context, R.drawable.ripple_black_no_corner)
            leftBtn!!.background = ContextCompat.getDrawable(context, R.drawable.ripple_white_no_corner)
            leftBtn!!.setTextColor(Color.parseColor("#2e3133"))
            rightBtn!!.setTextColor(Color.parseColor("#F0F0F3"))
        }
    }
}