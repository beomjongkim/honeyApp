package com.dmonster.darling.honey.myactivity.presenter

import android.net.Uri
import com.dmonster.darling.honey.myactivity.data.MemberData
import com.dmonster.darling.honey.myactivity.data.SpouseAreaData
import com.dmonster.darling.honey.myactivity.model.MyActModel
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class NewMemberListPresenter: NewMemberListContract.Presenter {

    private lateinit var mModel: MyActModel
    private lateinit var mView: NewMemberListContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: NewMemberListContract.View) {
        this.mView = view
        this.mModel = MyActModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    이상형 지역    */
    override fun getSpouseArea(id: String?, mbNo: String?) {
        val subscriber = object: DisposableObserver<ResultItem<SpouseAreaData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<SpouseAreaData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setSpouseArea(it.item?.mbHopeAddr)
                    }
                }
            }
        }
        mModel.requestSpouseArea(id, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    신규회원 목록    */
    override fun getNewMemberList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<MemberData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setNewMemberListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<MemberData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<MemberData>()
                            mList.addAll(it)
                            mView.setNewMemberListComplete(isScroll, mList)
                        }
                    }
                    else {
                        mView.setNewMemberListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestNewMemberList(startCnt, limitCnt, id, subscriber)
        subscription.add(subscriber)
    }

    /*    채팅방여부 확인    */
    override fun getTalkCheck(id: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<ArrayList<String>>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<ArrayList<String>>) {
                item.let { it ->
                    it.item?.let {
                        mView.setTalkCheck(it[0], it[1])
                    }
                }
            }
        }
        mModel.requestTalkCheck(id, otherId, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 보유 확인    */
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
                    mView.setItemCheckComplete(it.item, itemId)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    override fun checkPass(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<CheckFreePassData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setPassNeed()
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<CheckFreePassData>) {
                item.let { it ->
                    if(it.isSuccess)
                        mView.setItemCheckComplete("Y",itemId)
                    else
                        mView.setPassNeed()
                }
            }
        }
        ItemModel().check_own_freepass(id,  subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 사용    */
    override fun setItemUse(id: String?, itemId: String?, mbNo: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if(it.isSuccess) {
                        getTalk(id, otherId, "", null)
                    }
                    else {
                        mView.setItemUseFailed(it.message)
                    }
                }
            }
        }
        mModel.requestItemUse(id, itemId, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    톡하기    */
    override fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?) {
        val subscriber = object: DisposableObserver<ResultItem<TalkData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<TalkData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setTalkComplete()
                    }
                    else {
                        mView.setTalkFailed(it.message)
                    }
                }
            }
        }
        mModel.requestTalk(id, otherId, message, talkImage, subscriber)
        subscription.add(subscriber)
    }

}
