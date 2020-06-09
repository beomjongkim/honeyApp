package com.dmonster.darling.honey.information.presenter

import com.dmonster.darling.honey.information.data.PhoneAuthData
import com.dmonster.darling.honey.information.model.MyInfoModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class PhoneAuthPresenter : PhoneAuthContract.Presenter {

    private lateinit var mModel: MyInfoModel
    private lateinit var mView: PhoneAuthContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: PhoneAuthContract.View) {
        this.mView = view
        this.mModel = MyInfoModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    인증번호 받기    */
    override fun getPhoneAuth(id: String?, phone: String?) {
        val subscriber = object : DisposableObserver<ResultItem<PhoneAuthData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setPhoneAuthFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<PhoneAuthData>) {
                item.let { it ->
                    mView.setPhoneAuthComplete(it.message)
                }
            }
        }
        mModel.requestPhoneAuth(phone, subscriber)
        subscription.add(subscriber)
    }

    /*    휴대폰 인증    */
    override fun getPhoneAuthCheck(phone: String?, authNo: String?) {
        val subscriber = object : DisposableObserver<ResultItem<PhoneAuthData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setPhoneAuthCheckFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<PhoneAuthData>) {
                item.let { it ->
                    if (it.isSuccess) {
                        mView.setPhoneAuthCheckComplete()
                    } else {
                        mView.setPhoneAuthCheckFailed(it.message)
                    }
                }
            }
        }
        mModel.requestPhoneAuthCheck(phone, authNo, subscriber)
        subscription.add(subscriber)
    }

}
