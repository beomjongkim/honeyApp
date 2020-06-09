package com.dmonster.darling.honey.agreement.model

import com.dmonster.darling.honey.agreement.data.UseData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class UseModel {

    /*    이용방법    */
    fun requestUse(subscriber: DisposableObserver<ResultListItem<UseData>>) {
        RetrofitProtocol().retrofit.requestUse(ServerApi.instance.useMethod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
