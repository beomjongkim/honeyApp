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
import com.dmonster.darling.honey.js.JSHandler
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
    var activity: ComponentActivity,
    var webViewInterface: JSHandler.WebViewInterface?
) : ViewModel(), LifecycleObserver, PurchasesUpdatedListener {
    private var billingClient: BillingClient

    var tag = "InappPurchaseModel"

    lateinit var itemSkuDetail : SkuDetails

    var skuDetailList = ArrayList<SkuDetails>()
    var itemModel = ItemModel()


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
        // 프로필 열람
        skuList.add("profile_three")
        skuList.add("profile_five")
        skuList.add("profile_fifteen")
        skuList.add("profile_thirty")
        skuList.add("profile_fifty")
        skuList.add("profile_onehundred")

        //관심있어요
        skuList.add("wisi_three")
        skuList.add("wish_five")
        skuList.add("wish_fifteen")
        skuList.add("wish_thirty")
        skuList.add("wish_fifty")
        skuList.add("wish_onehundred")

        // 톡이용권
        skuList.add("talk_one")
        skuList.add("talk_five")
        skuList.add("talk_ten")
        skuList.add("talk_fifteen")
        skuList.add("talk_thirty")
        skuList.add("talk_sixty")

        //프로필점프업업
        skuList.add("jumpup_fifty")
        skuList.add("jumpup_onehundred")
        skuList.add("jumpup_twohundredfifty")
        skuList.add("jumpup_fivehundred")
        skuList.add("jumpup_onethousand")
        skuList.add("jumpup_onethousandfivehundred")


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

        itemSkuDetail = skuDetails
        Log.e("inAppPurchase","skuDetails : "+skuDetails)
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
                    webViewInterface?.afterPurchase()
                }
                isProgressing.postValue(false)

            }
        }


        itemModel.buyItem(id, item_id, subscriber)
    }

    private fun buyItemNew(context: Context , item_id : String){

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
                        Utility.instance.showToast(context, "성공적으로"+ itemSkuDetail.description +"상품을 구매하였습니다.")

                    } else {
                        Utility.instance.showToast(context, "구매 과정 중 오류가 발생하였습니다.")
                    }
                    webViewInterface?.afterPurchase()
                }
                isProgressing.postValue(false)

            }
        }

        if(item_id.contains("profile")){
            when (item_id) {
                "profile_three" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"3", subscriber)
                "profile_five" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"5", subscriber)
                "profile_fifteen" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"15", subscriber)
                "profile_thirty" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"30", subscriber)
                "profile_fifty" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"50", subscriber)
                "profile_onehundred" -> itemModel.addReadingPass(id, itemSkuDetail.description ,"100", subscriber)
                else -> {
                }
            }
        }else if(item_id.contains("wish")){
            when (item_id) {
                "wisi_three" -> itemModel.addWishPass(id, itemSkuDetail.description ,"3", subscriber)
                "wisi_five" -> itemModel.addWishPass(id, itemSkuDetail.description ,"5", subscriber)
                "wisi_fifteen" -> itemModel.addWishPass(id, itemSkuDetail.description ,"15", subscriber)
                "wisi_thirty" -> itemModel.addWishPass(id, itemSkuDetail.description ,"30", subscriber)
                "wisi_fifty" -> itemModel.addWishPass(id, itemSkuDetail.description ,"50", subscriber)
                "wish_onehundred" -> itemModel.addWishPass(id, itemSkuDetail.description ,"100", subscriber)
                else -> {
                }
            }
        }else if(item_id.contains("talk")){
            when (item_id) {
                "talk_one" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(1*24*60*60), subscriber)
                "talk_five" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(5*24*60*60), subscriber)
                "talk_ten" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(10*24*60*60), subscriber)
                "talk_fifteen" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(15*24*60*60), subscriber)
                "talk_thirty" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(30*24*60*60), subscriber)
                "talk_sixty" -> itemModel.addTalkPass(id, itemSkuDetail.description ,""+(60*24*60*60), subscriber)
                else -> {
                }
            }

        }else if(item_id.contains("jumpup")){
            when (item_id) {
                "jumpup_fifty" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"50", subscriber)
                "jumpup_onehundred" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"100", subscriber)
                "jumpup_twohundredfifty" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"250", subscriber)
                "jumpup_fivehundred" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"500", subscriber)
                "jumpup_onethousand" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"1000", subscriber)
                "jumpup_onethousandfivehundred" -> itemModel.addJumpupPass(id, itemSkuDetail.description ,"1500", subscriber)
                else -> {
                }
            }

        }

    }


    private fun afterPurchase(purchase: Purchase) {

        Log.e("inAppPurchase","afterPurchase")
        Log.e("inAppPurchase","sku : "+purchase.sku)
        Log.e("inAppPurchase","purchase : "+purchase)
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
                buyItemNew(activity,purchase.sku)
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
