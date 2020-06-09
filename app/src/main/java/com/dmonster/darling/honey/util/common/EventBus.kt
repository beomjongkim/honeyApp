package com.dmonster.darling.honey.util.common

import com.dmonster.darling.honey.information.data.MyInfoData
import com.dmonster.darling.honey.main.data.PurchaseInfoData
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.common.EventBus.subjectChargeCoin
import com.dmonster.darling.honey.util.common.EventBus.subjectCoin
import com.dmonster.darling.honey.util.common.EventBus.subjectCoinCharge
import com.dmonster.darling.honey.util.common.EventBus.subjectIdealType
import com.dmonster.darling.honey.util.common.EventBus.subjectItemFrag
import com.dmonster.darling.honey.util.common.EventBus.subjectMainFrag
import com.dmonster.darling.honey.util.common.EventBus.subjectMyInfo
import com.dmonster.darling.honey.util.common.EventBus.subjectPhoneAuth
import com.dmonster.darling.honey.util.common.EventBus.subjectProductId
import com.dmonster.darling.honey.util.common.EventBus.subjectTalk
import com.dmonster.darling.honey.util.common.EventBus.subjectTalkDelete
import io.reactivex.subjects.PublishSubject

object EventBus {

    val subjectProfile: PublishSubject<String> = PublishSubject.create<String>()
    val subjectCoin: PublishSubject<String> = PublishSubject.create<String>()
    val subjectTalk: PublishSubject<TalkData> = PublishSubject.create<TalkData>()
    val subjectTalkDelete: PublishSubject<String> = PublishSubject.create<String>()
    val subjectProductId: PublishSubject<String> = PublishSubject.create<String>()
    val subjectChargeCoin: PublishSubject<PurchaseInfoData> = PublishSubject.create<PurchaseInfoData>()
    val subjectMainFrag: PublishSubject<String> = PublishSubject.create<String>()
    val subjectMyInfo: PublishSubject<MyInfoData> = PublishSubject.create<MyInfoData>()
    val subjectItemFrag: PublishSubject<String> = PublishSubject.create<String>()
    val subjectPhoneAuth: PublishSubject<String> = PublishSubject.create<String>()
    val subjectCoinCharge: PublishSubject<String> = PublishSubject.create<String>()
    val subjectIdealType: PublishSubject<String> = PublishSubject.create<String>()
    val subjectInquiry: PublishSubject<String> = PublishSubject.create<String>()

    fun sendEventProfile(str: String) {
        subjectProfile.onNext(str)
    }

    fun sendEventCoin(str: String) {
        subjectCoin.onNext(str)
    }

    fun sendEventTalk(talkData: TalkData) {
        subjectTalk.onNext(talkData)
    }

    fun sendEventTalkDelete(str: String) {
        subjectTalkDelete.onNext(str)
    }

    fun sendEventProductId(str: String) {
        subjectProductId.onNext(str)
    }

    fun sendEventChargeCoin(purchaseInfoData: PurchaseInfoData) {
        subjectChargeCoin.onNext(purchaseInfoData)
    }

    fun sendEventMainFrag(str: String) {
        subjectMainFrag.onNext(str)
    }

    fun sendEventMyInfo(myInfoData: MyInfoData) {
        subjectMyInfo.onNext(myInfoData)
    }

    fun sendEventItemFrag(str: String) {
        subjectItemFrag.onNext(str)
    }

    fun sendEventPhoneAuth(str: String) {
        subjectPhoneAuth.onNext(str)
    }

    fun sendEventCoinCharge(str: String) {
        subjectCoinCharge.onNext(str)
    }

    fun sendEventIdealType(str: String) {
        subjectIdealType.onNext(str)
    }

    fun sendEventInquiry(str: String) {
        subjectInquiry.onNext(str)
    }

}
