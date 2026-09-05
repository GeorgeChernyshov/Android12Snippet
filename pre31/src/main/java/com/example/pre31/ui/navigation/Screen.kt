package com.example.pre31.ui.navigation

import androidx.annotation.StringRes
import com.example.pre31.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object SecurityAndPrivacy : Screen("securityAndPrivacy", R.string.label_security_and_privacy)
    object UserExperience : Screen("userExperience", R.string.label_user_experience)
}