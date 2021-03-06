package com.dmonster.darling.honey.myactivity.presenter

import android.net.Uri
import com.dmonster.darling.honey.myactivity.data.ProfileData
import com.dmonster.darling.honey.myactivity.model.MyActModel
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ProfileListPresenter: ProfileListContract.Presenter {

    private lateinit var mModel: MyActModel
    private lateinit var mView: ProfileListContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ProfileListContract.View) {
        this.mView = view
        this.mModel = MyActModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    프로필 열람 목록    */
    override fun getProfileList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<ProfileData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setProfileListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<ProfileData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<ProfileData>()
                            mList.addAll(it)
                            mView.setProfileListComplete(isScroll, mList)
                        }
                    }
                    else {
                        mView.setProfileListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestProfileList(startCnt, limitCnt, id, type, subscriber)
        subscription.add(subscriber)
    }

    /*    프로필 열람 회원 삭제    */
    override fun setProfileDelete(id: String?, type: String?, idx: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setProfileListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let {
                    if(it.isSuccess) {
                        mView.setProfileDeleteComplete(it.message)
                    }
                    else {
                        mView.setProfileDeleteFailed(it.message)
                    }
                }
            }
        }
        mModel.requestProfileDelete(id, type, idx, subscriber)
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
                    mView.setItemCheckComplete(it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
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
