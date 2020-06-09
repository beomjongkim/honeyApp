package com.dmonster.darling.honey.join.model

import com.dmonster.darling.honey.join.data.JoinData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class JoinModel {

    /*    아이디 사용가능 유무    */
    fun requestJoinId(id: String?, subscriber: DisposableObserver<ResultItem<String>>) {
        RetrofitProtocol().retrofit.requestJoinId(ServerApi.instance.joinIdMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }


    /*    회원가입    */
    fun requestJoin(id: String?, email:String?, password:String?, passwordCheck:String?, talkId:String?, age: String?, area01: String?, area02: String?,
                    gender: String?, recommendation: String?, phone: String?, subscriber: DisposableObserver<ResultItem<JoinData>>) {
        RetrofitProtocol().retrofit.requestJoin(ServerApi.instance.joinMethod, id, email, password, passwordCheck, talkId, age, area01, area02, gender, recommendation, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    로그인    */
    fun requestLogin(id: String?, password: String?, instanceId: String?, type: String?, subscriber: DisposableObserver<ResultItem<LoginData>>) {
        RetrofitProtocol().retrofit.requestLogin(ServerApi.instance.loginMethod, id, password, instanceId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
