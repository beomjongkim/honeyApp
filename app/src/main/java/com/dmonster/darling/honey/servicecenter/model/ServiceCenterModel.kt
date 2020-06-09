package com.dmonster.darling.honey.servicecenter.model

import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ServiceCenterModel {

    /*    앱정보    */
    fun requestAppInfo(subscriber: DisposableObserver<AppInfoData>) {
        RetrofitProtocol().retrofit.requestAppInfo(ServerApi.instance.appInfoMethod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
