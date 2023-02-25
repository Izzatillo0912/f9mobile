package com.crudgroup.f9mobile.presentation.otherComponents

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.crudgroup.f9mobile.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AllAnimations(private val context: Context) {

    private val onlyAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
    private val loginTitleAnimation = AnimationUtils.loadAnimation(context, R.anim.login_title_animation)
    private val topToReturnInstead = AnimationUtils.loadAnimation(context, R.anim.top_to_return_instead)
    private val bottomToReturnInstead = AnimationUtils.loadAnimation(context, R.anim.bottom_to_return_instead)

    fun splashLogoAnim(image : ImageView) {
        image.animation = onlyAlpha
    }

    fun loginTitleAnim(text : TextView) {
        text.animation = loginTitleAnimation
    }

    fun loginFurnitureAnim(image: ImageView) {
        image.animation = topToReturnInstead
    }

    fun loginInputCardAnim(inputCardView: CardView) {
        inputCardView.animation = bottomToReturnInstead
    }

    fun bottomAnimation(bottomBar: BottomNavigationView) {
        bottomBar.animation = bottomToReturnInstead
        bottomBar.animation.duration = 800
    }
}