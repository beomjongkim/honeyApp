package com.dmonster.darling.honey.item.presenter

import com.dmonster.darling.honey.item.data.ItemData
import com.dmonster.darling.honey.item.data.ItemUseData
import com.dmonster.darling.honey.item.model.ItemModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ItemAdminPresenter: ItemAdminContract.Presenter {

    private lateinit var mModel: ItemModel
    private lateinit var mView: ItemAdminContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ItemAdminContract.View) {
        this.mView = view
        this.mModel = ItemModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    보유아이템 목록    */
    override fun getItemList(id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<ItemData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<ItemData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<ItemData>()
                            mList.addAll(it)
                            mView.setItemListComplete(mList)
                        }
                    }
                    else {
                        mView.setItemListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestItemList(id, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 사용내역    */
    override fun getItemUseList(id: String?) {
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
                            mView.setItemUseListComplete(mList)
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
