package com.dmonster.darling.honey.talk.model

import android.net.Uri
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

class TalkModel {

    /*    톡하기 목록    */
    fun requestTalkList(startCnt: String?, limitCnt: String?, id: String?, roomNo: String?, subscriber: DisposableObserver<ResultListItem<TalkData>>) {
        RetrofitProtocol().retrofit.requestTalkList(ServerApi.instance.talkListMethod, startCnt, limitCnt, id, roomNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    내아이템 확인    */
    fun requestItemCheck(id: String?, itemId: String?, subscriber: DisposableObserver<ResultItem<String>>) {
        RetrofitProtocol().retrofit.requestMyItemCheck(ServerApi.instance.myItemCheckMethod, id, itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    톡하기    */
    fun requestSendTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?, subscriber: DisposableObserver<ResultItem<TalkData>>) {
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

    /*    차단    */
    fun requestBlock(id: String?, mbNo: String?, type: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestBlockClear(ServerApi.instance.blockMethod, id, mbNo, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    톡삭제    */
    fun requestDelete(id: String?, idx: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestMyTalkDelete(ServerApi.instance.myTalkDeleteMethod, id, idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    즐겨찾기    */
    fun requestBookmark(id: String?, mbNo: String?, type: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestBookmark(ServerApi.instance.bookmarkMethod, id, mbNo, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
