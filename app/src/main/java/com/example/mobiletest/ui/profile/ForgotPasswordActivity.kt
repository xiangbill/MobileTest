package com.example.mobiletest.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import com.example.mobiletest.MobileApplication
import com.example.mobiletest.base.BaseActivity
import com.example.mobiletest.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(ActivityForgotPasswordBinding::inflate) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun initView() {
        // 绑定 ViewModel 到布局 (单向/双向绑定的核心)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 设置 Toolbar 标题
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // 注意：按钮点击事件已经在 XML 中通过 android:onClick="@{() -> viewModel.resetPasswordCoroutine()}" 绑定
        // 也可以在这里手动绑定逻辑

        observeViewModel()
    }

    private fun observeViewModel() {
        // 实时观察 LiveData，并在 Logcat 打印变化，展示双向绑定的“实时性”
        viewModel.oldPassword.observe(this) { value ->
            android.util.Log.d("BindingTest", "Two-Way -> oldPassword changed in ViewModel: '$value'")
        }
        viewModel.newPassword.observe(this) { value ->
            android.util.Log.d("BindingTest", "Two-Way -> newPassword changed in ViewModel: '$value'")
        }

        // 观察 Loading 状态
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        // 观察错误状态
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                MobileApplication.showToast(it)
                viewModel.setError(null)
            }
        }

        // 观察重置成功状态
        viewModel.resetSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                MobileApplication.showToast("Password reset successfully! ✅")
                finish()
            }
        }
    }
}
