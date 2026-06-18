package com.example.mobiletest.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val disposables = CompositeDisposable()

    /**
     * RxJava 统一错误处理扩展方法
     */
    fun <T : Any> Observable<T>.applyCommonErrorHandling(): Observable<T> {
        return this.doOnError { error ->
            val displayMessage = when (error) {
                is SocketTimeoutException -> "网络连接超时，请稍后重试"
                is ConnectException, is UnknownHostException -> "无法连接到服务器，请检查网络设置"
                else -> "请求失败: ${error.message ?: "未知错误"}"
            }
            // 切换到主线程更新错误 LiveData (防止在非主线程抛出异常)
            _errorMessage.postValue(displayMessage)
        }
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    private var loadingCount = 0

    fun setLoading(loading: Boolean) {
        if (loading) loadingCount++ else loadingCount--
        if (loadingCount < 0) loadingCount = 0

        val shouldShow = loadingCount > 0
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            _isLoading.value = shouldShow
        } else {
            _isLoading.postValue(shouldShow)
        }
    }

    fun setError(message: String?) {
        _errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
