package com.dmonster.darling.honey.agreement.model

import com.dmonster.darling.honey.agreement.data.AgreementData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AgreementModel {

    /*    이용약관    */
    fun requestAgreement(types: String?, subscriber: DisposableObserver<ResultItem<AgreementData>>) {
        RetrofitProtocol().retrofit.requestAgreement(ServerApi.instance.agreementMethod, types)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
