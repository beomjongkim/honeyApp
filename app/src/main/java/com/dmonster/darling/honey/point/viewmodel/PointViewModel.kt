package com.dmonster.darling.honey.point.viewmodel

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.android.billingclient.api.*
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ReservePaymentPopup
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.data.PointData
import com.dmonster.darling.honey.point.data.PointLogData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.point.model.PointModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import io.reactivex.observers.DisposableObserver

class PointViewModel(
    var id: String?,
    lifecycle: Lifecycle,
    var activity: Activity,
    var reservePaymentPopup: ReservePaymentPopup,
    var adapter: CustomAdapter
) : ViewModel(), LifecycleObserver, PurchasesUpdatedListener {
    private var billingClient: BillingClient
    var productId = "freepass_month"

    //    var productId = "android.test.purchased"
    var tag = "PointViewModel"
    var inappType = 0

    var skuDetailList = ArrayList<SkuDetails>()
    var itemModel = ItemModel()
    var pointModel = PointModel()

    var rewardVM = activity.let { RewardVM(it) }

    var toggle = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var user_nick = Utility.instance.UserData().getUserNick()

    var text_available = MutableLiveData<String>().also {
        it.value = ""
    }

    var text_notice = MutableLiveData<String>().also {
        it.value = user_nick + "님의 포인트 구매, 사용 내역"
    }

    var text_left_date = MutableLiveData<String>().also {
        it.value = ""
    }

    var text_point_info = MutableLiveData<String>().also {
        it.value = user_nick + "님의 포인트는 현재\n0 포인트입니다."
    }

    var price_won = MutableLiveData<Int>().also {
        it.value = 0
    }

    var isProgressing = MutableLiveData<Boolean>().also {
        it.value = true
    }

    var hasPass = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var chargePoint = MutableLiveData<String>().also {
        it.value = "0"
    }


    init {
        Log.d(tag, "init")
        lifecycle.addObserver(this)
        billingClient =
            BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {

                Log.d(tag, "onBillingSetupFinished")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(tag, "BillingResponseCode.OK")
                    // The BillingClient is ready. You can query purchases here.
                    querySkuDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(tag, "billingServiceDisconnected")

                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        }
        )
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("RewardVM", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("RewardVM", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                // User earned reward.
                activity.let { buyItem(1, it) }
                Log.d("RewardVM", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("RewardVM", "Ad failed to display.")
            }
        }
        if (user_nick.isNullOrEmpty()) {
            user_nick = "회원"
        }
    }

    fun querySkuDetails() {
        Log.d(tag, "querySkuDetails")
        val skuList = ArrayList<String>()
        skuList.add("point50")
        skuList.add("point100")
        skuList.add("point150")
        skuList.add("freepass_month")

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { result, skuDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {

                for (skuDetails in skuDetailsList) {
                    val sku = skuDetails.sku
                    val price = skuDetails.price
                    skuDetailList.add(skuDetails)
                }

            }
        }
    }

    fun doBillingFlow(skuDetails: SkuDetails) {
        Log.d(tag, "doBillingFlow")

        val flowParams: BillingFlowParams =
            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams)
    }


    fun onClickBuyView() {
        toggle.value = false
    }

    fun onClickBuyLogView() {
        toggle.value = true
    }

    private fun checkPass() {
        isProgressing.value = true
        text_available.value = user_nick + "님은 현재 이용 중인 이용권이 없습니다."
        val subscriber = object : DisposableObserver<ResultItem<CheckFreePassData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_available.value = user_nick + "님은 현재 이용 중인 이용권이 없습니다."
                hasPass.value = false
                isProgressing.value = false
            }

            override fun onNext(item: ResultItem<CheckFreePassData>) {
                item.let { resultItem ->
                    if (resultItem.isSuccess) {
                        hasPass.value = true
                        text_available.value = ""
                        val day = resultItem.item?.minutes_left?.toInt()?.div(1440)
                        day?.let {
                            if(day >0){
                                text_left_date.value ="월간 이용권 사용중 | "+
                                        resultItem.item.due_date?.split(" ")!![0]+"까지 사용 가능 | 잔여일 : " + day + "일"
                            }else{
                                text_left_date.value ="1시간 이용권 사용중 | "+
                                        resultItem.item.due_date?.split(" ")!![1]+"까지 사용 가능 | 잔여시간 : " + resultItem.item.minutes_left?.toInt() + "분"
                            }
                        }


                    } else {
                        hasPass.value = false
                        text_available.value = user_nick + "님은 현재 이용 중인 이용권이 없습니다."
                    }
                }
            }
        }
        itemModel.check_own_freepass(id, subscriber)
    }

    private fun getCurrentPoint() {
        isProgressing.value = true
        val subscriber = object : DisposableObserver<ResultItem<PointData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                text_point_info.value = user_nick + "님의 포인트는 현재\n0 포인트입니다."
                isProgressing.value = false
            }

            override fun onNext(point: ResultItem<PointData>) {
                point.let {
                    if (it.isSuccess) {
                        it.item?.let {
                            text_point_info.value =
                                user_nick + "님의 포인트는 현재\n" + it.point + " 포인트입니다."
                        }
                    }

                }
            }
        }
        pointModel.read_point(id, subscriber)
    }

    private fun getPointLog() {
        isProgressing.value = true
        adapter.dataList.clear()
        val subscriber = object : DisposableObserver<ResultListItem<PointLogData>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                val pointLogData = PointLogData()
                pointLogData.description = "구매 내역이 없습니다."
                adapter.dataList.add(RecyclerItemData(0, pointLogData, BR.pointLogData))
            }

            override fun onNext(point: ResultListItem<PointLogData>) {
                point.let {
                    if (it.isSuccess) {
                        it.items?.let {

                            text_notice.value = user_nick + "님의 포인트 구매, 사용 내역"
                            for (item in it) {
                                item.date?.let {
                                    item.date_split = it.split(" ")[0]
                                    item.time_split = it.split(" ")[1].substring(0, 5)
                                }
                                if (item.useOrCharge == 1) {
                                    item.point = "-" + item.point + " 포인트"
                                } else {
                                    item.point = item.point + " 포인트"
                                }

                                adapter.dataList.add(RecyclerItemData(0, item, BR.pointLogData))

                            }
                            adapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
        pointModel.get_log_point(id, subscriber)
    }

    fun onClickBuyMonth(v: View) {
        buyItem(2, v.context)
    }

    fun onClickBuyHour() {
        if (hasPass.value == false) {
            if (rewardVM.rewardedAd.isLoaded) {
                rewardVM.rewardedAd.show(activity, rewardVM.adCallback)
            } else {
                Utility.instance.showToast(activity, "광고를 불러오는 중입니다.")
            }
        } else {
            Utility.instance.showToast(activity, "이용권 만료 후 광고 시청이 가능합니다.")
        }
    }

    fun onClickCharge(v: View) {
        showPaymentMethod()
//        chargePoint.value?.let {
//            showReservePopup(it.toInt() * 110, v.context)
//        }
//        reservePaymentPopup.show()
    }

    fun buyItem(itemCode: Int, context: Context) {//결제 후 아이템 구매 기록 남기기
        Log.d(tag, "buyItem")
        isProgressing.value = true
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.value = false
                onResume()
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                Utility.instance.showToast(context, "아이템 구매 과정 중 오류가 발생하였습니다.")
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if (it.isSuccess) {
                        Utility.instance.showToast(context, "성공적으로 이용권을 구매하였습니다.")
                        if (itemCode == 1 or 2)
                            hasPass.value = true
                    } else {
                        Utility.instance.showToast(context, "보유 포인트가 모자랍니다.")
                        if (itemCode == 2) {
                            inappType = 3
                            price_won.value = 5500
                            showPaymentMethod()
                        }
                    }
                }
                isProgressing.value = false
            }
        }
        itemModel.buyItem(id, itemCode, subscriber)
    }

    fun buy_inApp(context: Context, it_id: Int) {
        Log.d(tag, "buy_inApp")

        isProgressing.value = true
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.value = false
                onResume()
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if (it.isSuccess) {
                        Log.d(tag, "rechargePoint success")
//                        Utility.instance.showToast(context, "성공적으로 포인트를 구매하였습니다.")
                        hasPass.value = true
                        buyItem(2, context)
                    } else {
                        Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
                    }
                }
                isProgressing.value = false
            }
        }
        var point = 0
        if (it_id == 2) {
            point = 50
        }
        itemModel.rechargePoint(id, point, subscriber)
    }

    private fun rechargePoint(context: Context, type: Int) {

        isProgressing.value = true
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.value = false
                onResume()
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if (it.isSuccess) {
                        Log.d(tag, "rechargePoint success")
                        Utility.instance.showToast(context, "성공적으로 포인트를 구매하였습니다.")

                    } else {
                        Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
                    }
                }
                isProgressing.value = false
            }
        }
        var point = when (type) {
            0 -> 50
            1 -> 100
            2 -> 150
            else -> 0
        }

        itemModel.rechargePoint(id, point, subscriber)
    }


    private fun reservePayment(name: String, price: Int, receiptType : String?, receiptInfo : String?,context: Context) {
        isProgressing.value = true
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.value = false
            }

            override fun onError(e: Throwable) {
                isProgressing.value = false
                Utility.instance.showToast(context, "무통장 입금 정보 전송에 실패했습니다.")
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if (it.isSuccess) {
                        Utility.instance.showTwoButtonAlert(context,
                            "입금정보",
                            "아래 정보로 무통장 입금을 진행해주세요.\n입금자명 : " + name + "\n금액 : " + price + "원\n" + "기업은행 | 98602084704015 | 주식회사 상현",
                            "닫기",
                            "계좌번호복사",
                            DialogInterface.OnClickListener { p0, p1 ->
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip: ClipData = ClipData.newPlainText(
                                    "simple text",
                                    "입금자명 : " + name + "\n금액 : " + price + "원\n" + "기업은행 | 98602084704015 | 주식회사 상현"
                                )
                                clipboard.primaryClip = clip
                                Utility.instance.showToast(context, "복사되었습니다.")
                            }
                        )

                        Utility.instance.showAlert(
                            context,
                            "입금정보",
                            "아래 정보로 무통장 입금을 진행해주세요.\n입금자명 : " + name + "\n금액 : " + price + "원\n" + "기업은행 | 98602084704015 | 주식회사 상현",
                            object : CustomDialogInterface {
                                override fun onConfirm(v: View) {
                                    val clipboard =
                                        v.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip: ClipData = ClipData.newPlainText(
                                        "simple text",
                                        "입금자명 : " + name + "\n금액 : " + price + "원\n" + "기업은행 | 98602084704015 | 주식회사 상현"
                                    )
                                    clipboard.primaryClip = clip
                                    Utility.instance.showToast(v.context, "복사되었습니다.")
                                }

                                override fun onCancel(v: View) {
                                }
                            }
                        )
                    } else {
                        Utility.instance.showToast(context, "무통장 입금 정보 전송에 실패했습니다.")
                    }
                }
                isProgressing.value = false
            }
        }
        pointModel.reserve_payment(id, name, price,receiptType, receiptInfo , subscriber)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isProgressing.value = true
        checkPass()
        getCurrentPoint()
        getPointLog()
    }

    private fun showReservePopup(price: Int, context: Context) {
        reservePaymentPopup.reservePaymentPopupVM.let {
            it.price = price
            it.won.value = it.price.toString() + "원"
            it.twoBtnSwitch = object : CustomDialogInterface {
                override fun onConfirm(v: View) {
                    it.name.value?.let { it1 -> reservePayment(it1, it.price,it.receipt_type.value,it.receipt_info.value, context) }
                    reservePaymentPopup.dismiss()
                }

                override fun onCancel(v: View) {
                    reservePaymentPopup.dismiss()
                }

            }
        }
        reservePaymentPopup.show()
    }

    private fun afterPurchase(purchase: Purchase) {
        Log.d(tag, "afterPurchase")
        val purchaseToken = purchase.purchaseToken
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient.consumeAsync(consumeParams, object : ConsumeResponseListener {
            override fun onConsumeResponse(result: BillingResult, outToken: String) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(tag, "consumeResponse")
                    // Handle the success of the consume operation.
                    // For example, increase the number of coins inside the user's basket.
                    when (purchase.sku) {
                        "freepass_month" -> buy_inApp(activity, 2)
                        "point50" -> rechargePoint(activity, 0)
                        "point100" -> rechargePoint(activity, 1)
                        "point150" -> rechargePoint(activity, 2)
                    }
                }
            }
        })

    }

    private fun showPaymentMethod() {
        var popup = CustomPopup(
            activity,
            "결제 수단",
            "결제방식을 선택해주세요",
            R.drawable.ic_talk_vivid,
            object : CustomDialogInterface {
                override fun onConfirm(v: View) {
                    var skuDetail: SkuDetails
                    when (inappType) {
                        0 -> skuDetail = skuDetailList[3]
                        1 -> skuDetail = skuDetailList[1]
                        2 -> skuDetail = skuDetailList[2]
                        else -> skuDetail = skuDetailList[0]
                    }
                    Utility.instance.showToast(v.context, skuDetail.sku)
                    doBillingFlow(skuDetail)
                }

                override fun onCancel(v: View) {
                    showReservePopup(price_won.value!!, activity)
                }

            })
        popup.popupVM.negativeText.value = "무통장입금"
        popup.popupVM.positiveText.value = "인앱결제"
        popup.show()
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(tag, "onPurchasesUpdated")

            for (purchase in purchases) {
                afterPurchase(purchase)
            }
        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(tag, "USER_CANCELED")

            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            Log.e(tag, result.debugMessage)
            // Handle any other error codes.
        }
    }
}
