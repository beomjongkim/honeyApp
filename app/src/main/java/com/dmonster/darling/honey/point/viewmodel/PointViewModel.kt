package com.dmonster.darling.honey.point.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.customview.ReservePaymentPopup
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.data.PointData
import com.dmonster.darling.honey.point.data.PointLogData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import io.reactivex.observers.DisposableObserver

class PointViewModel(var id: String?, lifecycle: Lifecycle, var activity: Activity, var reservePaymentPopup: ReservePaymentPopup, var adapter: CustomAdapter) : ViewModel() , LifecycleObserver {
    var model : ItemModel

    var rewardVM = activity.let { RewardVM(it) }

    var toggle = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var user_nick = Utility.instance.UserData().getUserNick()

    var text_available = MutableLiveData<String>()

    var text_notice = MutableLiveData<String>().also{
            it.value = "회원님의 포인트 구매, 사용 내역"
    }

    var text_left_date = MutableLiveData<String>().also{
        it.value = ""
    }

    var text_point_info = MutableLiveData<String>().also{
        it.value = "회원님의 포인트는 현재\n0 포인트입니다."
    }

    var isProgressing = MutableLiveData<Boolean>().also {
        it.value = true
    }

    var hasPass = MutableLiveData<Boolean>().also {
        it.value = false
    }

    init {

        lifecycle.addObserver(this)
        model = ItemModel()
        rewardVM.adCallback = object: RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                activity.let { Utility.instance.showToast(it,"Ad opened.") }
            }
            override fun onRewardedAdClosed() {
                // Ad closed.
                activity.let { Utility.instance.showToast(it,"Ad closed.") }
            }
            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                // User earned reward.
                if(hasPass.value!!){
                    activity.let { buyItem(1, it) }
                }
                Log.d("RewardVM","User earned reward.")
            }
            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                activity.let { Utility.instance.showToast(it,"Ad failed to display.") }
            }
        }

    }

    fun onClickBuyView(){
        toggle.value = false
    }

    fun onClickBuyLogView(){
        toggle.value = true
    }

    private fun checkPass(){
        isProgressing.value = true
        if(user_nick.isNullOrBlank()){
            text_available.value = "회원님은 현재 이용권 사용"
        }else{
            text_available.value = user_nick+"님은 현재 이용권 사용"
        }
        val subscriber = object: DisposableObserver<ResultItem<CheckFreePassData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_available.value = text_available.value +" 만료되었습니다."
                isProgressing.value = false
            }

            override fun onNext(item:ResultItem<CheckFreePassData>) {
                item.let { it ->
                    if(it.isSuccess){
                        hasPass.value = true
                        text_available.value = text_available.value +" 중 입니다."
                        val day  = it.item?.minutes_left?.toInt()?.div(1440)
                        val dueDate = Utility.instance.transformDateTime(it.item?.due_date!!)
                        text_left_date.value = dueDate[0].toString()+"년 "+dueDate[1].toString() +"월 "+ dueDate[2].toString()+"일 까지 사용 가능합니다.\n잔여일 : " + day +"일"
                    }else{
                        text_available.value = text_available.value +" 만료되었습니다."
                    }
                }
            }
        }
        model.check_own_freepass(id,subscriber)
    }
    private fun getCurrentPoint(){
        isProgressing.value = true
        val subscriber = object: DisposableObserver<ResultItem<PointData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_point_info.value ="회원님의 포인트는 현재\n0 포인트입니다."
                isProgressing.value = false
            }

            override fun onNext(point:ResultItem<PointData>) {
                point.let {
                    if(it.isSuccess){
                        it.item?.let{
                            text_point_info.value =  Utility.instance.UserData().getUserNick() +"님의 포인트는 현재\n"+ it.point +" 포인트입니다."
                        }
                    }

                }
            }
        }
        model.read_point(id,subscriber)
    }
    private fun getPointLog(){
        isProgressing.value = true
        adapter.dataList.clear()
        val subscriber = object: DisposableObserver<ResultListItem<PointLogData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_available.value =""
                isProgressing.value = false
            }

            override fun onNext(point:ResultListItem<PointLogData>) {
                point.let {
                    if(it.isSuccess){
                        it.items?.let{
                            text_notice.value =  Utility.instance.UserData().getUserNick() +"님의 포인트 구매, 사용 내역"
                            for (item in it){
                                item.date?.let {
                                    item.date_split = it.split(" ")[0]
                                    item.time_split = it.split(" ")[1].substring(0,5)
                                }
                                if(item.useOrCharge == 1){
                                    item.point = "-"+item.point + " 포인트"
                                }else{
                                    item.point = item.point + " 포인트"
                                }

                                adapter.dataList.add(RecyclerItemData(0,item,BR.pointLogData))

                            }
                            adapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
        model.get_log_point(id,subscriber)
    }

    fun onClickBuyMonth(v: View){
        buyItem(2, v.context)
    }

    fun onClickBuyHour(){
        if(rewardVM.rewardedAd.isLoaded&&(hasPass.value == false)){
            rewardVM.rewardedAd.show(activity, rewardVM.adCallback)
        }else{
            Utility.instance.showToast(activity,"이용권 만료 후 광고 시청이 가능합니다.")
        }
    }

    fun buyItem(itemCode : Int, context : Context){//결제 후 아이템 구매 기록 남기기
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                Utility.instance.showToast(context,"아이템 구매 과정 중 오류가 발생하였습니다.")
            }

            override fun onNext(item:ResultItem<String>) {
                item.let { it ->
                    if(it.isSuccess){
                        Utility.instance.showToast(context,"성공적으로 이용권을 구매하였습니다.")
                    }else{
                        Utility.instance.showToast(context,"보유 포인트가 모자랍니다.")
                        if(itemCode==2){
                            reservePaymentPopup.reservePaymentPopupVM.price.value=5000
                        }
                        reservePaymentPopup.show()
                    }
                }
            }
        }
        model.buyItem(id, itemCode, subscriber)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isProgressing.value = true
        checkPass()
        getCurrentPoint()
        getPointLog()
    }
}
