package com.dmonster.darling.honey.login.model

import com.dmonster.darling.honey.login.data.FindIDPWData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LoginModel {

    /*    로그인    */
    fun requestLogin(id: String?, password: String?, instanceId: String?, type: String?, subscriber: DisposableObserver<ResultItem<LoginData>>) {
        RetrofitProtocol().retrofit.requestLogin(ServerApi.instance.loginMethod, id, password, instanceId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    아이디/비밀번호 찾기    */
    fun requestFidIDPW(type: String?, phone: String?, id: String?, subscriber: DisposableObserver<ResultItem<FindIDPWData>>) {
        RetrofitProtocol().retrofit.requestFindIDPW(ServerApi.instance.findIDPWMethod, type, phone, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
