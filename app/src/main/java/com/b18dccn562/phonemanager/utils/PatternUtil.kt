package com.b18dccn562.phonemanager.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.common.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatternUtil @Inject constructor(
    var application: Application
) {

    companion object {
        const val TAG = "PatternUtil"
    }

    fun hasPattern(): Boolean {
        return application.getSharedPreferences(
            application.getString(R.string.app_name),
            Context.MODE_PRIVATE
        ).getString(Constants.pattern, "") != ""
    }

    fun checkPattern(pattern: List<Int>): Boolean {
        var patternAsString = ""
        for (number in pattern) {
            patternAsString = patternAsString.plus(number.toString())
        }
        val savedPattern = getPattern()
        if (savedPattern == patternAsString) {
            return true
        }
        return false
    }

    fun savePattern(pattern: List<Int>) {
            var patternAsString = ""
            for (number in pattern) {
                patternAsString = patternAsString.plus(number.toString())
            }
            Log.i(TAG, "savePattern: $patternAsString")
            return application.getSharedPreferences(
                application.getString(R.string.app_name),
                Context.MODE_PRIVATE
            ).edit().putString(Constants.pattern, patternAsString).apply()
    }

    private fun getPattern(): String? {
        return application.getSharedPreferences(
            application.getString(R.string.app_name),
            Context.MODE_PRIVATE
        ).getString(Constants.pattern, "")
    }
}