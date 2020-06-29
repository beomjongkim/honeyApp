package com.dmonster.darling.honey.magazine.model

import com.dmonster.darling.honey.login.data.FindIDPWData
import com.dmonster.darling.honey.magazine.data.MagazineData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MagazineModel {
    fun readMagazine(subscriber: DisposableObserver<ResultListItem<MagazineData>>) {
        RetrofitProtocol().retrofit.readMagazine(ServerApi.instance.readMagazine)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}