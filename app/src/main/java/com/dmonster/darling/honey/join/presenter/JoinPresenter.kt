package com.dmonster.darling.honey.join.presenter

import com.dmonster.darling.honey.join.data.JoinData
import com.dmonster.darling.honey.join.model.JoinModel
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class JoinPresenter: JoinContract.Presenter {

    private lateinit var mModel: JoinModel
    private lateinit var mView: JoinContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: JoinContract.View) {
        this.mView = view
        this.mModel = JoinModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    회원가입 아이디 사용여부    */
    override fun getJoinId(id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setJoinIdIsValid(it.item)
                    }
                }
            }
        }
        mModel.requestJoinId(id, subscriber)
        subscription.add(subscriber)
    }

    /*    회원가입    */
    override fun getJoin(id: String?, email: String?, password: String?, passwordCheck: String?, talkId: String?, age: String?, area01: String?, area02: String?, gender: String?, recommendation: String?, phone: String?) {
        val subscriber = object: DisposableObserver<ResultItem<JoinData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setJoinFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<JoinData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setJoinComplete(it.mbId, it.mbNo, it.mbSn, it.mbSex)
                        }
                    }
                    else {
                        mView.setJoinFailed(item.message)
                    }
                }
            }
        }
        mModel.requestJoin(id, email, password, passwordCheck, talkId, age, area01, area02, gender, recommendation, phone, subscriber)
        subscription.add(subscriber)
    }

    /*    로그인    */
    override fun setLogin(id: String?, password: String?, instanceId: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultItem<LoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setLoginFailed()
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<LoginData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setLoginComplete()
                        }
                    }
                    else {
                        mView.setLoginFailed()
                    }
                }
            }
        }
        mModel.requestLogin(id, password, instanceId, type, subscriber)
        subscription.add(subscriber)
    }

}
