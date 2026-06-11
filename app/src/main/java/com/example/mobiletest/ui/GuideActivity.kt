package com.example.mobiletest.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mobiletest.manager.SpManager
import com.example.mobiletest.R

class GuideActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button

    private val guideData = listOf(
        GuideItem("欢迎来到 MobileTest", "这是一个功能强大的移动测试演示项目。"),
        GuideItem("探索无限可能", "我们提供了各种各样的组件展示和交互体验。"),
        GuideItem("立即开启旅程", "点击下方按钮进入应用，开始您的探索。")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guide)

        viewPager = findViewById(R.id.viewPager)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)

        setupViewPager()
        setupIndicators()
        setupButtons()
    }

    private fun setupViewPager() {
        viewPager.adapter = GuideAdapter(guideData)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateUI(position)
            }
        })
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(guideData.size)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]?.apply {
                setImageDrawable(androidx.core.content.ContextCompat.getDrawable(this@GuideActivity, R.drawable.guide_dot_selector))
                this.layoutParams = layoutParams
            }
            indicatorLayout.addView(indicators[i])
        }
        updateIndicators(0)
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val child = indicatorLayout.getChildAt(i) as ImageView
            child.isSelected = i == position
        }
    }

    private fun setupButtons() {
        btnPrev.setOnClickListener {
            val current = viewPager.currentItem
            if (current > 0) {
                viewPager.currentItem = current - 1
            }
        }

        btnNext.setOnClickListener {
            val current = viewPager.currentItem
            if (current < guideData.size - 1) {
                viewPager.currentItem = current + 1
            } else {
                finishGuide()
            }
        }
    }

    private fun updateUI(position: Int) {
        btnPrev.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
        btnNext.text = if (position == guideData.size - 1) "立即进入" else "下一步"
        updateIndicators(position)
    }

    private fun finishGuide() {
        // 使用 SpManager 保存“已看过引导页”状态
        SpManager.put("is_first_launch", false)
        
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    data class GuideItem(val title: String, val desc: String)

    class GuideAdapter(private val items: List<GuideItem>) : RecyclerView.Adapter<GuideAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.tvTitle)
            val desc: TextView = view.findViewById(R.id.tvDesc)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guide_page, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.desc.text = item.desc
        }

        override fun getItemCount() = items.size
    }
}
