package com.dmonster.darling.honey.webview.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.*

class InappPurchaseViewModel(var activity: Activity,  var id: String?)  : ViewModel(), LifecycleObserver, PurchasesUpdatedListener {
    private var billingClient: BillingClient =
        BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build()
    var tag = "InappPurchaseViewModel"

    var skuDetailList = ArrayList<SkuDetails>()

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        TODO("Not yet implemented")
    }

    init {
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
    }

    fun querySkuDetails() {
        Log.d(tag, "querySkuDetails")
        val skuList = ArrayList<String>()
        skuList.add("point50")
        skuList.add("point100")
        skuList.add("point150")

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

}