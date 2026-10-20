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
        // 使用一键封装的请求调度，极其清爽
        val disposable = NetworkModule.waterfallService.getItemDetails(item.id)
            .delay(3, TimeUnit.SECONDS, true) // 模拟 3 秒网络延迟
            .composeRequest()
            .subscribe({ result ->
                _requestResult.value = result
            }, { error ->
                _requestResult.value = item
            })

        addDisposable(disposable)
    }
}
