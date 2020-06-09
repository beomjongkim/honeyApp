package com.dmonster.darling.honey.alarm.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface AlarmContract {

    interface View: BaseView {
        fun getAlarmSetting(all: String?, newTalk: String?, good: String?, newMember: String?, loveCard: String?, notice: String?)    // 설정된 알림 불러오기

        fun setAlarmSettingComplete()    // 알림 설정하기

        fun setAlarmSettingFailed(error: String?)    // 알림 설정 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getAlarmSetting(id: String?)    // 설정된 알림 호출

        fun setAlarm(id: String?, all: String?, newTalk: String?, good: String?, newMember: String?, loveCard: String?, notice: String?)    // 알림 설정하기
    }

}
