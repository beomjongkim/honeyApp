package com.dmonster.darling.honey.profile.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.profile.data.ProfileDetailData
import com.dmonster.darling.honey.profile.data.ProfilePictureData
import com.dmonster.darling.honey.profile.model.GoodModel
import com.dmonster.darling.honey.profile.model.ImageDetailModel
import com.dmonster.darling.honey.profile.model.ProfileModel
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ImageDetailPresenter: ImageDetailContract.Presenter {

    private lateinit var mModel: ImageDetailModel
    private lateinit var mView: ImageDetailContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ImageDetailContract.View) {
        this.mView = view
        this.mModel = ImageDetailModel()
        subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
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
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.setTalkCheck(it[0], it[1])
                        }
                    }
                }
            }
        }
        mModel.requestTalkCheck(id, otherId, subscriber)
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

    /*    아이템 보유 확인    */
    override fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.setItemUseComplete(itemId, it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 사용    */
    override fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?) {
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

}
