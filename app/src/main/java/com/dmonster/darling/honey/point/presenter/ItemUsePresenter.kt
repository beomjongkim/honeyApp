package com.dmonster.darling.honey.point.presenter

import com.dmonster.darling.honey.point.data.ItemUseData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ItemUsePresenter: ItemUseContract.Presenter {

    private lateinit var mModel: ItemModel
    private lateinit var mView: ItemUseContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ItemUseContract.View) {
        this.mView = view
        this.mModel = ItemModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    아이템 사용내역    */
    override fun getItemUseList(isScroll: Boolean, id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<ItemUseData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<ItemUseData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<ItemUseData>()
                            mList.addAll(it)
                            mView.setItemUseListComplete(isScroll, mList)
                        }
                    }
                    else {
                        mView.setItemUseListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestItemUseList(id, subscriber)
        subscription.add(subscriber)
    }

}
