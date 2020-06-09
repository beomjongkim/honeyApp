package com.dmonster.darling.honey.option.model

import com.dmonster.darling.honey.option.data.SimpleMyInfoData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class OptionModel {
    fun requestSimpleMyInfo(id: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<SimpleMyInfoData>>) {
        RetrofitProtocol().retrofit.requestSimpleMyInformation(ServerApi.instance.simpleMyInformationMethod, id, mbNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}