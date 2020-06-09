package com.dmonster.darling.honey.login.presenter

import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.login.model.LoginModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class LoginPresenter: LoginContract.Presenter {

    private lateinit var mModel: LoginModel
    private lateinit var mView: LoginContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: LoginContract.View) {
        this.mView = view
        this.mModel = LoginModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    로그인    */
    override fun setLogin(id: String?, password: String?, instanceId: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultItem<LoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setLoginFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<LoginData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setLoginComplete(it.mbId, it.mbNo, it.mbSn, it.mbSex, it.mbSleep, it.mbProfileState)
                        }
                    }
                    else {
                        mView.setLoginFailed(it.message)
                    }
                }
            }
        }
        mModel.requestLogin(id, password, instanceId, type, subscriber)
        subscription.add(subscriber)
    }

    /*    소셜 로그인    */
    override fun setSocialLogin(id: String?, instanceId: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultItem<LoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<LoginData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setSocialLoginComplete(it.mbId, it.mbNo, it.mbSn, it.mbSex, it.mbSleep, it.mbProfileState)
                        }
                    }
                    else {
                        mView.setSocialLoginFailed()
                    }
                }
            }
        }
        mModel.requestLogin(id, "", instanceId, type, subscriber)
        subscription.add(subscriber)
    }

}
