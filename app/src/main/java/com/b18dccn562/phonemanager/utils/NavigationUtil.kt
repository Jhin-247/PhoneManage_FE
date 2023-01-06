package com.b18dccn562.phonemanager.utils

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController

fun navigateTo(view: View, action: NavDirections) {
    view.findNavController().safeNavigate(action)
}

fun navigateUp(view: View) {
    view.findNavController().navigateUp()
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
    }
}