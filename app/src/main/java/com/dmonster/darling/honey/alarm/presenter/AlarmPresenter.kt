package com.dmonster.darling.honey.alarm.presenter

import com.dmonster.darling.honey.alarm.data.AlarmData
import com.dmonster.darling.honey.alarm.model.AlarmModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class AlarmPresenter: AlarmContract.Presenter {

    private lateinit var mModel: AlarmModel
    private lateinit var mView: AlarmContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: AlarmContract.View) {
        this.mView = view
        this.mModel = AlarmModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    설정된 알림 불러오기    */
    override fun getAlarmSetting(id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<AlarmData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<AlarmData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            mView.getAlarmSetting(it.mbPushYn, it.mbPushMsg, it.mbPushHogam, it.mbPushMb, it.mbPushLoveCard, it.mbPushNotice)
                        }
                    }
                }
            }
        }
        mModel.requestAlarm(id, subscriber)
        subscription.add(subscriber)
    }

    /*    알림 설정하기    */
    override fun setAlarm(id: String?, all: String?, newTalk: String?, good: String?, newMember: String?, loveCard: String?, notice: String?) {
        val subscriber = object: DisposableObserver<ResultItem<AlarmData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setAlarmSettingFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<AlarmData>) {
                item.let {
                    if(it.isSuccess) {
                        mView.setAlarmSettingComplete()
                    }
                    else {
                        mView.setAlarmSettingFailed(it.message)
                    }
                }
            }
        }
        mModel.requestAlarmSetting(id, all, newTalk, good, newMember, loveCard, notice, subscriber)
        subscription.add(subscriber)
    }

}
