package com.example.mobiletest.startup

import android.content.Context
import android.util.Log
import androidx.startup.Initializer

import com.example.mobiletest.manager.SpManager

class CommonInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // 1. 预加载 SpManager (通过触发 lazy 加载)
        // 这样在进入 SplashActivity 或 GuideActivity 时，SP 文件已经加载进内存，响应更快
        Log.d("CommonInitializer", "Pre-warming SpManager...")
//        SpManager.contains("is_first_launch")

        // 2. 初始化日志工具 (示例)
        // 如果你有 Timber 等工具，也可以在这里初始化
        
        Log.d("CommonInitializer", "All common components initialized via App Startup")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 如果有依赖其他的 Initializer，可以在这里返回
        return emptyList()
    }
}
