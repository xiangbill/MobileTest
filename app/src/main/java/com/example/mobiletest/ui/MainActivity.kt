package com.example.mobiletest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobiletest.R
import com.example.mobiletest.base.BaseActivity
import com.example.mobiletest.databinding.ActivityMainBinding
import com.example.mobiletest.ui.alphabet.AlphabeticalFragment
import com.example.mobiletest.ui.home.HomeFragment
import com.example.mobiletest.ui.profile.ProfileFragment
import com.example.mobiletest.ui.waterfall.WaterfallFragment

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupInitialFragment()
        } else {
            // 在 super.onCreate 之后，BottomNavigationView 会自动恢复它的 selectedItemId
            // 我们需要等待布局完成后或者直接在之后调用恢复逻辑
            binding.root.post {
                restoreFragments()
            }
        }
    }

    override fun initView() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val tag = when (item.itemId) {
                R.id.navigation_home -> "home"
                R.id.navigation_waterfall -> "waterfall"
                R.id.navigation_alphabet -> "alphabet"
                R.id.navigation_profile -> "profile"
                else -> "home"
            }
            switchFragment(tag)
            true
        }
    }

    private fun setupInitialFragment() {
        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, fragment, "home")
            .commit()
        currentFragment = fragment
    }

    private fun restoreFragments() {
        val fm = supportFragmentManager
        val home = fm.findFragmentByTag("home")
        val waterfall = fm.findFragmentByTag("waterfall")
        val alphabet = fm.findFragmentByTag("alphabet")
        val profile = fm.findFragmentByTag("profile")

        val fragments = mapOf(
            "home" to home,
            "waterfall" to waterfall,
            "alphabet" to alphabet,
            "profile" to profile
        )
        
        val selectedId = binding.bottomNav.selectedItemId
        val targetTag = when (selectedId) {
            R.id.navigation_home -> "home"
            R.id.navigation_waterfall -> "waterfall"
            R.id.navigation_alphabet -> "alphabet"
            R.id.navigation_profile -> "profile"
            else -> "home"
        }

        val transaction = fm.beginTransaction()
        fragments.forEach { (tag, fragment) ->
            if (fragment != null) {
                if (tag == targetTag) {
                    transaction.show(fragment)
                    currentFragment = fragment
                } else {
                    transaction.hide(fragment)
                }
            }
        }
        transaction.commit()
    }

    private fun switchFragment(tag: String) {
        if (currentFragment?.tag == tag) return

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        
        // 1. 隐藏当前 Fragment
        currentFragment?.let { transaction.hide(it) }

        // 2. 查找目标 Fragment
        var target = fm.findFragmentByTag(tag)
        if (target == null) {
            // 3. 如果不存在则创建
            target = when (tag) {
                "home" -> HomeFragment()
                "waterfall" -> WaterfallFragment()
                "alphabet" -> AlphabeticalFragment()
                "profile" -> ProfileFragment()
                else -> HomeFragment()
            }
            transaction.add(R.id.nav_host_fragment, target, tag)
        } else {
            // 4. 如果存在则显示
            transaction.show(target)
        }
        
        currentFragment = target
        transaction.commit()
    }
}
