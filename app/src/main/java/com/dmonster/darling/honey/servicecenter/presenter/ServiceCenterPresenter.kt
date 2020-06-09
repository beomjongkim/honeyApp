package com.dmonster.darling.honey.servicecenter.presenter

import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.servicecenter.model.ServiceCenterModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ServiceCenterPresenter: ServiceCenterContract.Presenter {

    private lateinit var mModel: ServiceCenterModel
    private lateinit var mView: ServiceCenterContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ServiceCenterContract.View) {
        this.mView = view
        this.mModel = ServiceCenterModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    앱정보    */
    override fun getAppInfo() {
        val subscriber = object: DisposableObserver<AppInfoData>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setAppInfoFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: AppInfoData) {
                item.let {
                    if(it.isSuccess) {
                        mView.setAppInfo(it.email, it.callNo, it.reportNo, it.bizNo)
                    }
                    else {
                        mView.setAppInfoFailed(it.message)
                    }
                }
            }
        }
        mModel.requestAppInfo(subscriber)
        subscription.add(subscriber)
    }

}
