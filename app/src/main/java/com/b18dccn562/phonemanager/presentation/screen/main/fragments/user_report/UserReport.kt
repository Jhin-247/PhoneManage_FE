package com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report

import com.b18dccn562.phonemanager.common.Constants

class UserReport {
    var appName: String = ""
    var appTime: String = ""
    var appIcon: String = ""
}

fun getAppIcon(appPackage: String): String {
    return "${Constants.ApiReferences.BASE_URL}${Constants.ApiReferences.IMAGE_REFERENCE}/${Constants.ApiReferences.GET_APP_ICON}/$appPackage"
}