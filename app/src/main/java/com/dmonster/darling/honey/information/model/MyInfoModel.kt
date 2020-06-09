package com.dmonster.darling.honey.information.model

import com.dmonster.darling.honey.information.data.MyInfoData
import com.dmonster.darling.honey.information.data.PhoneAuthData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MyInfoModel {

    /*    기본정보 받아오기    */
    fun requestMyInfo(id: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<MyInfoData>>) {
        RetrofitProtocol().retrofit.requestMyInfo(ServerApi.instance.myInfoMethod, id, mbNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    기본정보 수정하기    */
    fun requestMyInfoEdit(id: String?, talkId: String?, age: String?, type: String?, area01: String?, area02: String?, password: String?,
                          newPassword: String?, newPasswordCheck: String?, phone: String?, subscriber: DisposableObserver<ResultItem<MyInfoData>>) {
        RetrofitProtocol().retrofit.requestMyInfoEdit(ServerApi.instance.myInfoEditMethod, id, talkId, age, type, area01, area02, password, newPassword, newPasswordCheck, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    핸드폰번호 문자인증    */
    fun requestPhoneAuth( phone: String?, subscriber: DisposableObserver<ResultItem<PhoneAuthData>>) {
        RetrofitProtocol().retrofit.requestPhoneAuth(ServerApi.instance.phoneAuthMethod,  phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    핸드폰번호 문자인증확인    */
    fun requestPhoneAuthCheck(phone: String?, authNo: String?, subscriber: DisposableObserver<ResultItem<PhoneAuthData>>) {
        RetrofitProtocol().retrofit.requestPhoneAuthCheck(ServerApi.instance.phoneAuthCheckMethod, phone, authNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
