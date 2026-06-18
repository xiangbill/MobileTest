package com.example.mobiletest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobiletest.base.BaseViewModel
import com.example.mobiletest.model.GenericItem
import com.example.mobiletest.request.NetworkModule
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WaterfallViewModel : BaseViewModel() {

    private val _requestResult = MutableLiveData<GenericItem>()
    val requestResult: LiveData<GenericItem> = _requestResult

    /**
     * 封装通用的 RxJava 请求流程，调用 Retrofit Service
     */
    fun loadItemDetails(item: GenericItem) {
        // 实际上这里应该调用网络接口，我们保留一个 Observable.just 作为 Mock 演示，
        // 或者直接调用 waterfallService.getItemDetails(item.id)
        
        val disposable = NetworkModule.waterfallService.getItemDetails(item.id)
            .delay(3, TimeUnit.SECONDS, true) // 模拟 3 秒网络延迟，delayError = true 确保失败也会延迟，以便观察 Loading 弹窗
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .applyCommonErrorHandling() // 使用统一错误处理
            .doOnSubscribe { setLoading(true) }
            .doFinally { setLoading(false) }
            .subscribe({ result ->
                _requestResult.value = result
            }, { error ->
                // 这里处理真实的错误，比如 404
                // setError("Network Error: ${error.message}") // applyCommonErrorHandling 已经处理了 UI 提示
                
                // --- 以下为 Mock 演示：如果接口报错（因为 URL 是假的），我们手动返回一个成功结果 ---
                _requestResult.value = item
            })

        addDisposable(disposable)
    }
}
