package com.dmonster.darling.honey.item.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.customview.RegisterPaymentPopup
import com.dmonster.darling.honey.item.data.CheckFreePassData
import com.dmonster.darling.honey.item.data.PointLogData
import com.dmonster.darling.honey.item.model.ItemModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.observers.DisposableObserver

class ItemViewModel(var id: String?,var lifecycle: Lifecycle, var lifecycleOwner: LifecycleOwner, var adapter: CustomAdapter) : ViewModel() , LifecycleObserver {
    var model : ItemModel
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

    init {
        lifecycle.addObserver(this)
        model = ItemModel()
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
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_point_info.value ="회원님의 포인트는 현재\n0 포인트입니다."
                isProgressing.value = false
            }

            override fun onNext(point:ResultItem<BaseItem>) {
                point.let {
                    if(it.isSuccess){
                        it.item?.let{
                            text_point_info.value =  Utility.instance.UserData().getUserNick() +"님의 포인트는 현재\n"+ it +" 포인트입니다."
                        }
                    }

                }
            }
        }
        model.read_point(id,subscriber)
    }
    private fun getPointLog(){
        isProgressing.value = true
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
        val registerPaymentPopup = RegisterPaymentPopup(v.context, 5000,lifecycleOwner )
        registerPaymentPopup.show()
    }

    fun onClickBuyHour(){

    }

    fun buyItem(itemCode : Int){//결제 후 아이템 구매 기록 남기기
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
            }

            override fun onNext(item:ResultItem<BaseItem>) {
                item.let { it ->
                   Log.d("itemViewModel",item.result.toString())
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
