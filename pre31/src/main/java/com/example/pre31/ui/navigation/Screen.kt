package com.example.pre31.ui.navigation

import androidx.annotation.StringRes
import com.example.pre31.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object WebIntent : Screen("webIntent", R.string.label_web_intent)
}