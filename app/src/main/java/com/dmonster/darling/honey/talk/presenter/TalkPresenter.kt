package com.dmonster.darling.honey.talk.presenter

import android.net.Uri
import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.talk.model.TalkModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class TalkPresenter: TalkContract.Presenter {

    private lateinit var mModel: TalkModel
    private lateinit var mView: TalkContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: TalkContract.View) {
        this.mView = view
        this.mModel = TalkModel()
        subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    채팅내역    */
    override fun getTalkList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, roomNo: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<TalkData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<TalkData>) {
                item.let {
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<TalkData>()
                            mList.addAll(it)
                            mView.setTalkListComplete(isScroll, mList)
                        }
                    }
                    else {
                        mView.setTalkListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestTalkList(startCnt, limitCnt, id, roomNo, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 확인    */
    override fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemCheckFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.setItemCheckComplete(it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    /*    채팅 메세지 전송    */
    override fun setSendTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?) {
        val subscriber = object: DisposableObserver<ResultItem<TalkData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setSendTalkFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<TalkData>) {
                item.let {
                    if(it.isSuccess) {
                        it.item?.let { it1 ->
                            mView.setSendTalkComplete(it1.chatSendDate, it1.chatMsg, it1.chatImg)
                        }
                    }
                    else {
                        mView.setSendTalkFailed(it.message)
                    }
                }
            }
        }
        mModel.requestSendTalk(id, otherId, message, talkImage, subscriber)
        subscription.add(subscriber)
    }

    /*    회원 차단    */
    override fun setBlock(id: String?, mbNo: String?, type: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setBlockFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let {
                    if(it.isSuccess) {
                        mView.setBlockComplete(it.message)
                    }
                    else {
                        mView.setBlockFailed(it.message)
                    }
                }
            }
        }
        mModel.requestBlock(id, mbNo, type, subscriber)
        subscription.add(subscriber)
    }

    /*    채팅방 삭제    */
    override fun setDelete(id: String?, roomNo: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setDeleteFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let {
                    if(it.isSuccess) {
                        mView.setDeleteComplete()
                    }
                    else {
                        mView.setDeleteFailed(it.message)
                    }
                }
            }
        }
        mModel.requestDelete(id, roomNo, subscriber)
        subscription.add(subscriber)
    }

    /*    즐겨찾기    */
    override fun setBookmark(id: String?, mbNo: String?, type: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setBookmark(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let {
                    mView.setBookmark(it.message)
                }
            }
        }
        mModel.requestBookmark(id, mbNo, type, subscriber)
        subscription.add(subscriber)
    }

}
