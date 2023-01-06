package com.b18dccn562.phonemanager.local_database.shared_preference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.b18dccn562.phonemanager.common.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountPreference @Inject constructor(
    application: Application
) {

    private val sharedPreference: SharedPreferences =
        application.getSharedPreferences(Constants.Preference.PREF, Context.MODE_PRIVATE)

    private val editor = sharedPreference.edit()

    fun saveAccount(
        email: String,
        password: String
    ) {
        editor.putString(Constants.Preference.EMAIL, email)
        editor.putString(Constants.Preference.PASSWORD, password)
        editor.apply()
    }

    fun hasAccount(): Boolean {
        val email = sharedPreference.getString(Constants.Preference.EMAIL, "")
        return email != null && email.isNotEmpty()
    }

    fun getEmail(): String {
        val email = sharedPreference.getString(Constants.Preference.EMAIL, "")
        return if (email == null || email.isEmpty()) {
            ""
        } else {
            email
        }
    }

    fun getPassword(): String {
        val email = sharedPreference.getString(Constants.Preference.PASSWORD, "")
        return if (email == null || email.isEmpty()) {
            ""
        } else {
            email
        }
    }

    fun logout() {
        saveAccount("","")
    }

}