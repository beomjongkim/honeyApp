package com.dmonster.darling.honey.main.model

import android.net.Uri
import com.dmonster.darling.honey.main.data.BannerData
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainListModel {

    /*    메인목록    */
    fun requestMainList(startCnt: String?, limitCnt: String?, id: String?, area: String?, gender: String?, age: String?, subscriber: DisposableObserver<ResultListItem<MainListData>>) {
        RetrofitProtocol().retrofit.requestMainList(ServerApi.instance.mainListMethod, startCnt, limitCnt, id, area, gender, age, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    메인목록    */
    fun requestFilterList(startCnt: String?, limitCnt: String?, id: String?, area: String?, latitude : String?, longitude : String?, sst : String?, gender: String?, age: String?, marry : String?, religion : String?, income : String?, property : String?, blood : String?, edu_level : String?, drink : String?, mb_baby : String?, certify_mb_img: String?, certify_mb_marry_img : String?, subscriber: DisposableObserver<ResultListItem<MainListData>>) {
        RetrofitProtocol().retrofit.requestFilterList(ServerApi.instance.mainListMethod, startCnt, limitCnt, id, area, latitude, longitude, sst, gender, age, marry,religion, income, property, blood,edu_level,drink,mb_baby,certify_mb_img,certify_mb_marry_img,"1")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    내아이템 확인(자동갱신 버튼)    */
    fun requestItemCheck(id: String?, itemId: String?, subscriber: DisposableObserver<ResultItem<String>>) {
        RetrofitProtocol().retrofit.requestMyItemCheck(ServerApi.instance.myItemCheckMethod, id, itemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    자동갱신    */
    fun requestRefreshList(id: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestAutoList(ServerApi.instance.autoListMethod, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    공지사항 목록    */
    fun requestNoticeList(id: String?, subscriber: DisposableObserver<ResultListItem<NoticeData>>) {
        RetrofitProtocol().retrofit.requestNotice(ServerApi.instance.noticeMethod, id, "notice")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    톡하기 여부    */
    fun requestTalkCheck(id: String?, otherId: String?, subscriber: DisposableObserver<ResultItem<ArrayList<String>>>) {
        RetrofitProtocol().retrofit.requestTalkCheck(ServerApi.instance.talkCheckMethod, id, otherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    톡하기 사용    */
    fun requestItemUse(id: String?, itemId: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<String>>) {
        RetrofitProtocol().retrofit.requestItemUse(ServerApi.instance.myItemUseMethod, id, itemId, mbNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    톡하기    */
    fun requestTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?, subscriber: DisposableObserver<ResultItem<TalkData>>) {
        val requestMethod = RequestBody.create(MediaType.parse("text/plain"), ServerApi.instance.talkMethod)
        val requestId = id?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestOtherId = otherId?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestMessage = message?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        var requestImage: MultipartBody.Part? = null
        talkImage?.path?.let {
            val file = File(it)
            val fileName = file.name
            requestImage = MultipartBody.Part.createFormData("img", fileName, RequestBody.create(MediaType.parse("multipart/form-data"), file))
        }

        RetrofitProtocol().retrofit.requestTalk(requestMethod, requestId, requestOtherId, requestMessage, requestImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
    /*    배너 정보 가져오기    */
    fun requestBannerInfo(subscriber: DisposableObserver<ResultListItem<BannerData>>) {
        RetrofitProtocol().retrofit.requestBannerInfo(ServerApi.instance.bannerInfoMethod)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}
