package com.example.post31.ui.navigation

import androidx.annotation.StringRes
import com.example.post31.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object UserExperience : Screen("userExperience", R.string.label_user_experience)
    object Performance : Screen("performance", R.string.label_performance)
}