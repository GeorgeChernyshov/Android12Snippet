package com.example.post31.ui.navigation

import androidx.annotation.StringRes
import com.example.post31.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object UserExperience : Screen("storage", R.string.label_user_experience)
}