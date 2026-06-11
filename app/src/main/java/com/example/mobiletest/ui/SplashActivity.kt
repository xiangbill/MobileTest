package com.example.mobiletest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.mobiletest.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null
    private var isSkipped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. 开启沉浸式，并强制指定状态栏为浅色模式（透明背景+深色图标），不带任何阴影
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        
        setContentView(R.layout.activity_splash)

        val tvSkip = findViewById<TextView>(R.id.tvSkip)

        // 适配沉浸式状态栏：给跳过按钮增加顶部边距，防止被刘海或状态栏遮挡
        ViewCompat.setOnApplyWindowInsetsListener(tvSkip) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top + 40
            }
            insets
        }

        // 5秒倒计时
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000 + 1
                tvSkip.text = getString(R.string.text_count, seconds)
            }

            override fun onFinish() {
                if (!isSkipped) {
                    navigateToMain()
                }
            }
        }.start()

        tvSkip.setOnClickListener {
            isSkipped = true
            countDownTimer?.cancel()
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
