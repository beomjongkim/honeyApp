package com.dmonster.darling.honey.agreement.presenter

import com.dmonster.darling.honey.agreement.data.AgreementData
import com.dmonster.darling.honey.agreement.model.AgreementModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class AgreementPresenter: AgreementContract.Presenter {

    private lateinit var mModel: AgreementModel
    private lateinit var mView: AgreementContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: AgreementContract.View) {
        this.mView = view
        this.mModel = AgreementModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    이용방법, 개인정보처리방침    */
    override fun getAgreement(types: String?) {
        val subscriber = object: DisposableObserver<ResultItem<AgreementData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView .agreementFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<AgreementData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.agreementComplete(it.coContent)
                        }
                    }
                    else {
                        mView.agreementFailed(it.message)
                    }
                }
            }
        }
        mModel.requestAgreement(types, subscriber)
        subscription.add(subscriber)
    }

}
