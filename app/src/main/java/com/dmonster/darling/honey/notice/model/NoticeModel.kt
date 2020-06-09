package com.dmonster.darling.honey.notice.model

import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NoticeModel {

    /*    공지사항 목록    */
    fun requestNoticeList(id: String?, subscriber: DisposableObserver<ResultListItem<NoticeData>>) {
        RetrofitProtocol().retrofit.requestNotice(ServerApi.instance.noticeMethod, id, "notice")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
