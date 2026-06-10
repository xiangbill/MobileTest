package com.example.mobiletest

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast

class MobileApplication : Application() {

    companion object {
        private lateinit var
                instance: MobileApplication

        fun getInstance(): MobileApplication {
            return instance
        }

        fun getContext(): Context {
            return instance.applicationContext
        }

        /**
         * 在主线程弹出 Toast
         */
        fun showToast(message: String) {
            Toast.makeText(instance, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    /**
     * 获取应用版本名称
     */
    fun getAppVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (ignored: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(): Int {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(): Int {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
