package com.example.mobiletest.ui.profile

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentProfileBinding
import com.example.mobiletest.model.ApiResult
import com.example.mobiletest.model.UserProfile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    
    private val viewModel: ProfileViewModel by viewModels()

    override fun initView() {
        setupListeners()
    }

    override fun initData() {
        observeFlows()
    }

    private fun setupListeners() {
        binding.profileImage.setOnClickListener {
            viewModel.updateAvatar()
        }
    }

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 观察 UI 事件 (SharedFlow)
                launch {
                    viewModel.uiEvent.collect { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                    }
                }

                // 观察用户信息请求流 (StateFlow)
                launch {
                    viewModel.userProfile.collect { result ->
                        when (result) {
                            is ApiResult.Loading -> {
                                binding.securityStatusText.text = "Loading profile..."
                                binding.securityStatusText.setTextColor(android.graphics.Color.GRAY)
                            }
                            is ApiResult.Success -> {
                                updateUI(result.data)
                            }
                            is ApiResult.Error -> {
                                binding.securityStatusText.text = "Error: ${result.message}"
                                binding.securityStatusText.setTextColor(android.graphics.Color.RED)
                                Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                
                // 观察积分实时更新流
                launch {
                    viewModel.pointsDisplay.collect { points ->
                        binding.pointsText.text = points
                    }
                }
            }
        }
    }

    private fun updateUI(user: UserProfile) {
        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
        binding.securityStatusText.text = "Security Score: ${user.securityScore} (${user.level})"
        binding.securityStatusText.setTextColor(android.graphics.Color.parseColor("#388E3C")) // Holo Green Dark
    }
}
