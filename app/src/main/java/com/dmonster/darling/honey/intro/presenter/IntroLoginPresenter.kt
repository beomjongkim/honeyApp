package com.dmonster.darling.honey.intro.presenter

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.intro.model.IntroLoginModel
import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.util.Random

class IntroLoginPresenter: IntroLoginContract.Presenter {

    private lateinit var mModel: IntroLoginModel
    private lateinit var mView: IntroLoginContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: IntroLoginContract.View) {
        this.mView = view
        this.mModel = IntroLoginModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }


    /*    로그인    */
    override fun setLogin(id: String?, password: String?, instanceId: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultItem<IntroLoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setLoginFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<IntroLoginData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {

                            mView.setLoginComplete(it.mbId,it.mbNo,it.mbNick, it.mbSn, it.mbSex, it.mbSleep, it.mbProfileState)
                        }
                    }
                    else {
                        mView.setLoginFailed(it.message)
                    }
                }
            }
        }
        mModel.requestIntroLogin(id, password, instanceId, type, subscriber)
        subscription.add(subscriber)
    }

    //인트로 액티비티 들어올 시 보여주는 랜딩 이미지를 랜덤으로 보여주도록 하는 함수
    override fun setRandomImageView(view: ImageView, intArray: IntArray) {
        val random = Random().nextInt(intArray.size)
        val id = intArray[random]
        id.let {
            Glide.with(view.context).load(id).apply(RequestOptions.centerCropTransform().centerCrop()).into(view)
        }
//        view.setImageResource(intArray[random])
    }
}
