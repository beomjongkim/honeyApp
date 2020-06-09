package com.dmonster.darling.honey.information.presenter

import com.dmonster.darling.honey.information.data.MyInfoData
import com.dmonster.darling.honey.information.model.MyInfoModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MyInfoPresenter: MyInfoContract.Presenter {

    private lateinit var mModel: MyInfoModel
    private lateinit var mView: MyInfoContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: MyInfoContract.View) {
        this.mView = view
        this.mModel = MyInfoModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    기본정보 불러오기    */
    override fun getMyInfo(id: String?, mbNo: String?) {
        val subscriber = object: DisposableObserver<ResultItem<MyInfoData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMyInfoFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInfoData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            val sucEmail = it.mbId
                            val sucTalkId = it.mbNick
                            val sucAge = it.mbBirth
                            val sucType = it.mbChar
                            val sucArea01 = it.mbAddr1
                            val sucArea02 = it.mbAddr2
                            val sucPhone = it.mbHp
                            val sucTalkIdState = it.mbNickCnt
                            val sucAgeState = it.mbBirthCnt
                            val sucTypeState = it.mbCharCnt

                            mView.setMyInfoComplete(sucEmail, sucTalkId, sucAge, sucType, sucArea01, sucArea02, sucPhone, sucTalkIdState, sucAgeState, sucTypeState)
                        }
                    }
                    else {
                        mView.setMyInfoFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyInfo(id, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    기본정보 수정    */
    override fun setMyInfoEdit(id: String?, talkId: String?, age: String?, type: String?, area01: String?, area02: String?, password: String?, newPassword: String?, newPasswordCheck: String?, phone: String?) {
        val subscriber = object: DisposableObserver<ResultItem<MyInfoData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMyInfoEditFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInfoData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            val sucTalkId = it.mbNick
                            val sucAge = it.mbAge
                            val sucBirth = it.mbBirth
                            val sucType = it.mbChar
                            val sucArea01 = it.mbAddr1
                            val sucArea02 = it.mbAddr2
                            val sucPhone = it.mbHp

                            mView.setMyInfoEditComplete(sucTalkId, sucAge, sucBirth, sucType, sucArea01, sucArea02, sucPhone)
                        }
                    }
                    else {
                        mView.setMyInfoEditFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyInfoEdit(id, talkId, age, type, area01, area02, password, newPassword, newPasswordCheck, phone, subscriber)
        subscription.add(subscriber)
    }

}
