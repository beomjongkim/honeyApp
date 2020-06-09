package com.dmonster.darling.honey.login.presenter

import com.dmonster.darling.honey.login.data.FindIDPWData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.login.model.LoginModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class FindIDPWPresenter: FindIDPWContract.Presenter {

    private lateinit var mModel: LoginModel
    private lateinit var mView: FindIDPWContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: FindIDPWContract.View) {
        this.mView = view
        this.mModel = LoginModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    아이디 찾기    */
    override fun setFindID(type: String?, phone: String?, id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<FindIDPWData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setFindIDFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<FindIDPWData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setFindIDComplete(it.mbId)
                        }
                    }
                    else {
                        mView.setFindIDFailed(it.message)
                    }
                }
            }
        }
        mModel.requestFidIDPW(type, phone, id, subscriber)
        subscription.add(subscriber)
    }

    /*    비밀번호 찾기    */
    override fun setFindPW(type: String?, phone: String?, id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<FindIDPWData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setFindPWFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<FindIDPWData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setFindPWComplete(it.message)
                    }
                    else {
                        mView.setFindPWFailed(it.message)
                    }
                }
            }
        }
        mModel.requestFidIDPW(type, phone, id, subscriber)
        subscription.add(subscriber)
    }

}
