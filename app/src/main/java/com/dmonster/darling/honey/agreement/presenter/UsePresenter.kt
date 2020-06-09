package com.dmonster.darling.honey.agreement.presenter

import com.dmonster.darling.honey.agreement.data.UseData
import com.dmonster.darling.honey.agreement.model.UseModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class UsePresenter: UseContract.Presenter {

    private lateinit var mModel: UseModel
    private lateinit var mView: UseContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: UseContract.View) {
        this.mView = view
        this.mModel = UseModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    이용방법    */
    override fun getUseList() {
        val subscriber = object: DisposableObserver<ResultListItem<UseData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.useListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<UseData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<UseData>()
                            mList.addAll(it)
                            mView.useListComplete(mList)
                        }
                    }
                    else {
                        mView.useListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestUse(subscriber)
        subscription.add(subscriber)
    }

}
