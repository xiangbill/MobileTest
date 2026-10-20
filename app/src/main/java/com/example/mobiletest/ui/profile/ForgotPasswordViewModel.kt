package com.example.mobiletest.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.base.BaseViewModel
import com.example.mobiletest.request.ForgotPasswordRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ForgotPasswordViewModel : BaseViewModel() {

    private val repository = ForgotPasswordRepository()

    // 双向绑定使用的字段
    val oldPassword = MutableLiveData<String>("")
    val newPassword = MutableLiveData<String>("")

    private val _resetSuccess = MutableLiveData<Boolean>()
    val resetSuccess: LiveData<Boolean> = _resetSuccess

    fun resetPassword() {
        oldPassword.postValue("123456")
        newPassword.postValue("123456")
    }

    /**
     * 使用协程实现重置密码
     */
    fun resetPasswordCoroutine() {
        val oldPwd = oldPassword.value ?: ""
        val newPwd = newPassword.value ?: ""
        if (oldPwd.isEmpty() || newPwd.isEmpty()) {
            setError("Password cannot be empty!")
            return
        }
        viewModelScope.launch {
            try {
                setLoading(true)
                delay(3000)
                repository.resetPasswordSuspend(oldPwd, newPwd)
                _resetSuccess.value = true
            } catch (e: Exception) {
                setError("Coroutine Error: ${e.message}")
                _resetSuccess.value = true
            } finally {
                setLoading(false)
            }
        }
    }
}
