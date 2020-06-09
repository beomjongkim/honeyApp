package com.dmonster.darling.honey.myinformation.model

import com.dmonster.darling.honey.myinformation.data.RecommendData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class RecommendModel {

    /*    추천인 현황    */
    fun requestRecommend(id: String?, startCnt: String?, limitCnt: String?, subscriber: DisposableObserver<ResultListItem<RecommendData>>) {
        RetrofitProtocol().retrofit.requestRecommend(ServerApi.instance.recommendMethod, id, startCnt, limitCnt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
