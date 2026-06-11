package com.example.mobiletest.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.mobiletest.MobileApplication

/**
 * SharedPreferences 统一管理工具类
 */
object SpManager {
    private const val SP_NAME = "app_prefs"
    
    private val prefs: SharedPreferences by lazy {
        MobileApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据
     */
    fun put(key: String, value: Any) {
        val editor = prefs.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> editor.putString(key, value.toString())
        }
        editor.apply()
    }

    /**
     * 获取数据
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> prefs.getString(key, defaultValue) as T
            is Int -> prefs.getInt(key, defaultValue) as T
            is Boolean -> prefs.getBoolean(key, defaultValue) as T
            is Float -> prefs.getFloat(key, defaultValue) as T
            is Long -> prefs.getLong(key, defaultValue) as T
            else -> defaultValue
        }
    }

    /**
     * 移除某个 Key
     */
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /**
     * 是否包含某个 Key
     */
    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
}
