package com.dmonster.darling.honey.block.presenter

import com.dmonster.darling.honey.block.data.BlockData
import com.dmonster.darling.honey.block.model.BlockModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class BlockPresenter: BlockContract.Presenter {

    private lateinit var mModel: BlockModel
    private lateinit var mView: BlockContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: BlockContract.View) {
        this.mView = view
        this.mModel = BlockModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    차단회원목록    */
    override fun getBlockList(isScroll: Boolean, id: String?, limitCnt: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<BlockData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setBlockListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<BlockData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<BlockData>()
                            mList.addAll(it)
                            mView.setBlockList(isScroll, mList)
                        }
                    }
                    else {
                        mView.setBlockListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestBlockList(id, limitCnt, subscriber)
        subscription.add(subscriber)
    }

    /*    차단회원 차단해제    */
    override fun setBlockClear(id: String?, mbNo: String?, type: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setBlockClearFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setBlockClearComplete(it.message)
                    }
                    else {
                        mView.setBlockClearFailed(it.message)
                    }
                }
            }
        }
        mModel.requestBlockClear(id, mbNo, type, subscriber)
        subscription.add(subscriber)
    }

}
