package com.dmonster.darling.honey.intro.model

import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class IntroLoginModel {



    /*    로그인    */
    fun requestIntroLogin(id: String?, password: String?, instanceId: String?, type: String?, subscriber: DisposableObserver<ResultItem<IntroLoginData>>) {
        RetrofitProtocol().retrofit.requestIntroLogin(ServerApi.instance.loginMethod, id, password, instanceId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
