package com.example.mobiletest.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.model.ApiResult
import com.example.mobiletest.model.UserProfile
import com.example.mobiletest.request.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    // 1. 使用 StateFlow 处理 UI 状态（如用户信息显示）
    private val _userProfile = MutableStateFlow<ApiResult<UserProfile>>(ApiResult.Loading)
    val userProfile: StateFlow<ApiResult<UserProfile>> = _userProfile.asStateFlow()

    // 2. 使用 SharedFlow 处理 UI 事件（如 Toast、Dialog、页面跳转等单次操作）
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    init {
        refreshProfile()
    }

    fun refreshProfile() {
        viewModelScope.launch {
            repository.fetchUserProfile().collect { result ->
                _userProfile.value = result
            }
        }
    }

    /**
     * 模拟修改头像的事件，通过 SharedFlow 发送成功的 UI 通知
     */
    fun updateAvatar() {
        viewModelScope.launch {
            // 模拟上传耗时
            _uiEvent.emit("Starting upload...")
            kotlinx.coroutines.delay(1500)
            
            // 发送最终成功的事件通知
            _uiEvent.emit("Avatar updated successfully! ✅")
        }
    }

    /**
     * 实时积分流：持续观察型 Flow 封装
     */
    val pointsDisplay: StateFlow<String> = repository.getPointsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1000
        ).let { flow ->
            val derived = MutableStateFlow("Points: 1000")
            viewModelScope.launch {
                flow.collect { derived.value = "Points: $it" }
            }
            derived.asStateFlow()
        }
}
