package com.dmonster.darling.honey.alarm.model

import com.dmonster.darling.honey.alarm.data.AlarmData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AlarmModel {

    /*    알림설정 목록    */
    fun requestAlarm(id: String?, subscriber: DisposableObserver<ResultItem<AlarmData>>) {
        RetrofitProtocol().retrofit.requestAlarm(ServerApi.instance.alarmMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    알림설정    */
    fun requestAlarmSetting(id: String?, all: String?, newTalk: String?, good: String?, newMember: String?, loveCard: String?, notice: String?, subscriber: DisposableObserver<ResultItem<AlarmData>>) {
        RetrofitProtocol().retrofit.requestAlarmSetting(ServerApi.instance.alarmSettingMethod, id, all, newTalk, good, newMember, loveCard, notice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
