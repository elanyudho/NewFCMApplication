package com.dicoding.fcmapplication.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.fcmapplication.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

class Session
@Inject constructor(
    @ApplicationContext context: Context
) {
    private val gson = Gson()
    private val sp : SharedPreferences by lazy {
        context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE)
    }

    private val spe : SharedPreferences.Editor by lazy {
        sp.edit()
    }

    fun clear() {
        spe.clear().apply()
    }

    var isFirstTime: Boolean
        get() = sp.getBoolean(IS_FIRST_TIME, true)
        set(value) = spe.putBoolean(IS_FIRST_TIME, value).apply()

    var isLogin: Boolean
        get() = sp.getBoolean(IS_LOGIN, false)
        set(value) = spe.putBoolean(IS_LOGIN, value).apply()

    var user: User?
        get() = getUserData()
        set(value) {
            val json = gson.toJson(value)
            spe.putString(USER_PROFILE,json)
            spe.apply()
        }

    companion object {
        private const val SESSION_NAME = "BUMNSession"
        private const val IS_FIRST_TIME = "first_time"
        private const val USER_PROFILE = "user_profile"
        private const val IS_LOGIN = "is login"
    }
    private fun getUserData(): User? {
        return try {
            gson.fromJson(
                sp.getString(USER_PROFILE,""),User::class.java)
        }catch (e: Exception){
            User()
        }
    }
}