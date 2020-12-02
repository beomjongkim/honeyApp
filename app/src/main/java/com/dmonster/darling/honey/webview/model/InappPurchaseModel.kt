package com.dmonster.darling.honey.webview.model

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
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
class InappPurchaseModel(
    var id: String?,
    var activity: ComponentActivity
) : ViewModel(), LifecycleObserver, PurchasesUpdatedListener {
    private var billingClient: BillingClient
    var productId = "freepass_month"

    //    var productId = "android.test.purchased"
    var tag = "PointViewModel"
    var inappType = 0

    var skuDetailList = ArrayList<SkuDetails>()
    var itemModel = ItemModel()
    var user_nick = Utility.instance.UserData().getUserNick()


    var isProgressing = MutableLiveData<Boolean>().also {
        it.postValue(true)
    }

    var hasPass = MutableLiveData<Boolean>().also {
        it.postValue(false)
    }


    init {
        Log.d(tag, "init")
        activity.lifecycle.addObserver(this)
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
        })

    }

    fun querySkuDetails() {
        Log.d(tag, "querySkuDetails")
        val skuList = ArrayList<String>()
        skuList.add("point50")
        skuList.add("point100")
        skuList.add("point150")
        skuList.add("freepass_month")
        skuList.add("freepass_year")


        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { result, skuDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                for (skuDetails in skuDetailsList) {
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




    private fun rechargePoint(context: Context, type: Int) {

        isProgressing.postValue(true)
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.postValue(false)
            }

            override fun onError(e: Throwable) {
                isProgressing.postValue(false)
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
                isProgressing.postValue(false)
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

    private fun buyItem(context: Context, item_id: Int){
        isProgressing.postValue(true)
        val subscriber = object : DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {
                isProgressing.postValue(false)
            }

            override fun onError(e: Throwable) {
                isProgressing.postValue(false)
                Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if (it.isSuccess) {
                        Log.d(tag, "item purchase success")
                        Utility.instance.showToast(context, "성공적으로 상품을 구매하였습니다.")

                    } else {
                        Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
                    }
                }
                isProgressing.postValue(false)
            }
        }


        itemModel.buyItem(id, item_id, subscriber)
    }


    private fun afterPurchase(purchase: Purchase) {
        Log.d(tag, "afterPurchase")
        val purchaseToken = purchase.purchaseToken
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient.consumeAsync(
            consumeParams
        ) { result, outToken ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(tag, "consumeResponse")
                // Handle the success of the consume operation.
                // For example, increase the number of coins inside the user's basket.
                when (purchase.sku) {
                    "point50" -> rechargePoint(activity, 0)
                    "point100" -> rechargePoint(activity, 1)
                    "point150" -> rechargePoint(activity, 2)
                    "freepass_month"->buyItem(activity,2)
                    "freepass_year"->buyItem(activity,6)
                    "freepass_twodays"->buyItem(activity,7)
                }
            }
        }

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
        } else if (purchases != null) {
            Log.e(tag, result.debugMessage)
            for (purchase in purchases) {
                val purchaseToken = purchase.purchaseToken
                val consumeParams =
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(purchaseToken)
                        .build()
                billingClient.consumeAsync(
                    consumeParams
                ) { result, outToken ->
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d(tag, "consumeResponse")
                        // Handle the success of the consume operation.
                        // For example, increase the number of coins inside the user's basket.
                    }
                }
            }
            // Handle any other error codes.
        } else {
            Log.e(tag, "ResponseCode is " + result.responseCode)
            Log.e(tag, result.debugMessage)
        }
    }
}
