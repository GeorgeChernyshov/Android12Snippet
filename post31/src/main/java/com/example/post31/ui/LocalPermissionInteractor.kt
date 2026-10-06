package com.example.post31.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.post31.interactor.permission.PermissionInteractor

val LocalPermissionInteractor = staticCompositionLocalOf<PermissionInteractor> {
    error("PermissionInteractor was not provided")
}
