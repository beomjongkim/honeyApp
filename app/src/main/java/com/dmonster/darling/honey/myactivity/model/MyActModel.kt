package com.dmonster.darling.honey.myactivity.model

import android.net.Uri
import com.dmonster.darling.honey.myactivity.data.MemberData
import com.dmonster.darling.honey.myactivity.data.ProfileData
import com.dmonster.darling.honey.myactivity.data.SpouseAreaData
import com.dmonster.darling.honey.myactivity.data.TalkListData
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

class MyActModel {

    /*    나의톡 목록    */
    fun requestMyTalkList(id: String?, limitCnt: String?, subscriber: DisposableObserver<ResultListItem<TalkListData>>) {
        RetrofitProtocol().retrofit.requestMyTalkList(ServerApi.instance.myTalkListMethod, id, limitCnt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    러브카드 목록    */
    fun requestLoveList(startCnt: String?, limitCnt: String?, id: String?, subscriber: DisposableObserver<ResultListItem<MemberData>>) {
        RetrofitProtocol().retrofit.requestNewMemberList(ServerApi.instance.loveListMethod, startCnt, limitCnt, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    나의톡 삭제    */
    fun requestMyTalkDelete(id: String?, idx: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestMyTalkDelete(ServerApi.instance.myTalkDeleteMethod, id, idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    신규회원 목록    */
    fun requestNewMemberList(startCnt: String?, limitCnt: String?, id: String?, subscriber: DisposableObserver<ResultListItem<MemberData>>) {
        RetrofitProtocol().retrofit.requestNewMemberList(ServerApi.instance.newMemberListMethod, startCnt, limitCnt, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    희망배우자 지역    */
    fun requestSpouseArea(id: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<SpouseAreaData>>) {
        RetrofitProtocol().retrofit.requestSpouseArea(ServerApi.instance.profileDetailMethod, id, mbNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    프로필열람 목록    */
    fun requestProfileList(startCnt: String?, limitCnt: String?, id: String?, type: String?, subscriber: DisposableObserver<ResultListItem<ProfileData>>) {
        RetrofitProtocol().retrofit.requestProfileList(ServerApi.instance.profileListMethod, startCnt, limitCnt, id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    프로필열람 목록 삭제    */
    fun requestProfileDelete(id: String?, type: String?, idx: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestProfileListDelete(ServerApi.instance.profileListDeleteMethod, id, type, idx)
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

}
