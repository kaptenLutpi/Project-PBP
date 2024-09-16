package com.example.tugas_akhir

import android.content.Context


class LoginPreference (context: Context) {

    private val preference = context.getSharedPreferences("login_data", Context.MODE_PRIVATE)

    fun saveLogin(data: String) {

        val editor = preference.edit()
        editor.putString("name", data)
        editor.apply()
    }

    fun removeLogin() {

        val editor = preference.edit().clear()

        editor.apply()

    }
    fun getName():String?{
        return preference.getString("name", null)
    }

}