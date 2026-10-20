package com.example.mobiletest.ui.profile

import androidx.activity.viewModels
import com.example.mobiletest.MobileApplication
import com.example.mobiletest.base.BaseActivity
import com.example.mobiletest.databinding.ActivityForgotPasswordOnewayBinding

class ForgotPasswordOneWayActivity : BaseActivity<ActivityForgotPasswordOnewayBinding>(ActivityForgotPasswordOnewayBinding::inflate) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun initView() {
        // 单向绑定设置
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // 在单向绑定中，因为 et_old_password.text 改变不会自动更新 viewModel.oldPassword
        // 所以我们必须手动调用以参数形式传入的请求方法，或者展示这种对比区别！
        binding.btnConfirm.setOnClickListener {
            val oldPwdText = binding.etOldPassword.text.toString().trim()
            val newPwdText = binding.etNewPassword.text.toString().trim()

            // 演示单向绑定与双向绑定的最大区别：
            // 此时 viewModel.oldPassword.value 依然是初始值空字符串 ""！
            // 我们需要直接传入提取到的文本或者在此处通过业务验证它
            if (oldPwdText.isEmpty() || newPwdText.isEmpty()) {
                MobileApplication.showToast("Password cannot be empty (Checked in Activity)!")
                return@setOnClickListener
            }

            // 为了复用重置请求，我们手动把输入框更改的文本塞给 viewModel（模拟双向绑定所省略的手动赋值）
            viewModel.oldPassword.value = oldPwdText
            viewModel.newPassword.value = newPwdText
            viewModel.resetPasswordCoroutine()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        // 实时观察 LiveData，展示单向绑定下输入框输入时，ViewModel 不会变化
        viewModel.oldPassword.observe(this) { value ->
            android.util.Log.d("BindingTest", "One-Way -> oldPassword changed in ViewModel: '$value'")
        }
        viewModel.newPassword.observe(this) { value ->
            android.util.Log.d("BindingTest", "One-Way -> newPassword changed in ViewModel: '$value'")
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                MobileApplication.showToast(it)
                viewModel.setError(null)
            }
        }

        viewModel.resetSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                MobileApplication.showToast("One-Way Page: Reset Success! ✅")
                finish()
            }
        }
    }
}
