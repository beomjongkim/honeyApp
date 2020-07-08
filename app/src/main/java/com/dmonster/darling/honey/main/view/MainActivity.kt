package com.dmonster.darling.honey.main.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.databinding.ActivityMainBinding
import com.dmonster.darling.honey.dialog.LoveDialog
import com.dmonster.darling.honey.newMember.view.NewMemberFragment
import com.dmonster.darling.honey.information.view.MyInfoActivity
import com.dmonster.darling.honey.point.view.ItemMainFragment
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.login.view.LoginEmailActivity
import com.dmonster.darling.honey.main.data.NoticePopupData
import com.dmonster.darling.honey.main.model.MainModel
import com.dmonster.darling.honey.main.viewmodel.NavigationVM
import com.dmonster.darling.honey.myactivity.view.MyActMainFragment
import com.dmonster.darling.honey.myinformation.view.MyProfileActivity
import com.dmonster.darling.honey.notice.view.NoticeActivity
import com.dmonster.darling.honey.option.view.OptionFragment
import com.dmonster.darling.honey.point.view.PointFragment
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.github.clans.fab.FloatingActionButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(){

    private lateinit var disposeBag: CompositeDisposable

    private var newFragment: androidx.fragment.app.Fragment? = null

    private val fragMain = 0
    private val fragMarket = 1
    private val fragIdeal = 2
    private val fragMailBox = 3
    private val fragMagazine = 4
    private val fragOption = 5

    private var arrMenus: ArrayList<FloatingActionButton>? = null
    private var arrMenusPress: Int = 3
    private var profileState: Boolean = true
    private var dormantState: Boolean = false
//    private var billingProcessor: BillingProcessor? = null
    private var coin: String? = null
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setViewModel()
        init()
        setListener()
        setEventBusListener()
    }

    private fun setViewModel() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mb_id  = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        binding.naviVM = NavigationVM(
            supportFragmentManager,
            mb_id,
            lifecycle
        )
        binding.bannerVM = BannerVM(mb_id,lifecycle,this)
        binding.lifecycleOwner = this
        binding.naviVM?.fragmentReplace(fragMain)
    }

    private fun init() {
        disposeBag = CompositeDisposable()
//        billingProcessor = BillingProcessor(this, AppKeyValue.instance.inAppKey, this)
//        billingProcessor?.initialize()


        val notificationType = intent.getStringExtra(AppKeyValue.instance.pushNotificationType)


        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes

        val roomNo = intent.getStringExtra(AppKeyValue.instance.pushRoomNo)
        val mbNo = intent.getStringExtra(AppKeyValue.instance.pushMbNo)
        val otherId = intent.getStringExtra(AppKeyValue.instance.pushOtherId)
        val otherTalkId = intent.getStringExtra(AppKeyValue.instance.pushOtherTalkId)

        if (!TextUtils.isEmpty(notificationType)) {
            val saveId = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
            val savePassword = Utility.instance.getPref(this, AppKeyValue.instance.savePrefPassword)
            val saveType = Utility.instance.getPref(this, AppKeyValue.instance.savePrefType)

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM_token", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log
                    Log.d("FCM_token", "FCM_token : " + token)
                    if (TextUtils.isEmpty(saveId)) {
                        val intent = Intent(this, LoginEmailActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (!TextUtils.isEmpty(saveId) && !TextUtils.isEmpty(savePassword) || saveType == AppKeyValue.instance.keyTypeSocial) {
                        setLogin(
                            saveId,
                            savePassword,
                            token,
                            saveType,
                            notificationType,
                            roomNo,
                            mbNo,
                            otherId,
                            otherTalkId
                        )
                    }
                })


        }

        val intentPW = intent.getBooleanExtra(AppKeyValue.instance.findPW, false)
        if (intentPW) {
            if (profileState) {
                Utility.instance.showTwoButtonAlert(
                    this,
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.msg_find_pw),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            val intent = Intent(this, MyInfoActivity::class.java)
                            startActivity(intent)
                        }
                    })
            }
        }


        if(intent.getBooleanExtra(AppKeyValue.instance.goToMarket,false)){
            binding.naviVM?.fragmentReplace(fragMarket)
        }
        if(intent.getBooleanExtra(AppKeyValue.instance.goToMailBox, false)){
            binding.naviVM?.fragmentReplace(fragMailBox)
        }
        if(intent.getBooleanExtra(AppKeyValue.instance.goToMagazine, false)){
            binding.naviVM?.fragmentReplace(fragMagazine)
        }
    }


    private fun setListener() {
    }


    private fun setEventBusListener() {
        EventBus.subjectProfile.subscribe {
            if (it == AppKeyValue.instance.eventBusProfile) {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }
        }.addTo(disposeBag)

        EventBus.subjectProductId.subscribe {
            when (it) {
                AppKeyValue.instance.inAppItem01 -> coin = AppKeyValue.instance.inAppItemCoin01
                AppKeyValue.instance.inAppItem02 -> coin = AppKeyValue.instance.inAppItemCoin02
                AppKeyValue.instance.inAppItem03 -> coin = AppKeyValue.instance.inAppItemCoin03
                AppKeyValue.instance.inAppItem04 -> coin = AppKeyValue.instance.inAppItemCoin04
                AppKeyValue.instance.inAppItem05 -> coin = AppKeyValue.instance.inAppItemCoin05
                AppKeyValue.instance.inAppItem06 -> coin = AppKeyValue.instance.inAppItemCoin06
            }

//            billingProcessor?.let { it1 ->
//                if (it1.isPurchased(it)) {
//                    it1.consumePurchase(it)
//                }
//                it1.purchase(this, it)
//            }
        }.addTo(disposeBag)

        EventBus.subjectMainFrag.subscribe {
            if (it == AppKeyValue.instance.eventBusMainFrag) {
                arrMenusPress = 3
                setPress(arrMenusPress)
                binding.naviVM?.fragmentReplace(fragMain)
            }
        }.addTo(disposeBag)

        EventBus.subjectCoinCharge.subscribe {
            if (it == AppKeyValue.instance.eventBusCoinCharge) {
                if (newFragment !is ItemMainFragment) {
                    Handler().postDelayed({
                        arrMenusPress = 2
                        setPress(arrMenusPress)

                        val bundle = Bundle()
                        bundle.putString(
                            AppKeyValue.instance.itemBundleCoin,
                            AppKeyValue.instance.itemBundleCoin
                        )
                        fragmentBundleReplace(fragMarket, bundle)
                        binding.naviVM?.selectView(fragMarket)

                    }, 100)
                }
            }
        }.addTo(disposeBag)

        EventBus.subjectIdealType.subscribe {
            if (it == AppKeyValue.instance.eventBusIdealType) {
                arrMenusPress = 1
                setPress(arrMenusPress)

                val bundle = Bundle()
                bundle.putString(
                    AppKeyValue.instance.idealTypeBundle,
                    AppKeyValue.instance.idealTypeBundle
                )
                fragmentBundleReplace(fragIdeal, bundle)
                binding.naviVM?.fragmentNumber?.value = fragIdeal
            }
        }.addTo(disposeBag)
    }

    private fun setPress(position: Int) {
        arrMenus?.let { it ->
            it.map {
                it.isSelected = false
            }

            if (position < 3) {
                it[position].isSelected = true
            }
        }
    }



    private fun fragmentBundleReplace(reqNewFragmentIndex: Int, bundle: Bundle?) {
        newFragment = getFragment(reqNewFragmentIndex)

        val transaction = supportFragmentManager.beginTransaction()
        newFragment?.let { transaction.replace(R.id.fl_act_main_content_layout, it) }
        transaction.commit()
        newFragment?.arguments = bundle

        Utility.instance.hideSoftKeyboard(this)
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when (idx) {
            fragMain -> newFragment = MainFragment()

            fragMarket -> newFragment = PointFragment()

            fragIdeal -> newFragment = NewMemberFragment()

            fragMailBox -> newFragment = MyActMainFragment()

            fragOption -> newFragment = OptionFragment()
        }

        return newFragment
    }

    private fun setLogin(
        id: String?,
        password: String?,
        instanceId: String?,
        type: String?,
        notificationType: String?,
        roomNo: String?,
        mbNo: String?,
        otherId: String?,
        otherTalkId: String?
    ) {
        val subscriber = object : DisposableObserver<ResultItem<LoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<LoginData>) {
                item.let { it ->
                    if (it.isSuccess) {
                        it.item?.let { it1 ->
                            Utility.instance.UserData().apply {
                                setUserMb(it1.mbNo)
                                setUserRecommend(it1.mbSn)
                                setUserGender(it1.mbSex)
                                setUserDormant(it1.mbSleep)
                                setUserProfile(it1.mbProfileState)
                            }

                            profileState = it1.mbProfileState == AppKeyValue.instance.keyYes
                            if (profileState) {
                                when (notificationType) {
                                    resources.getString(R.string.push_notification_type_message), resources.getString(
                                        R.string.push_notification_type_good
                                    ) -> {
                                        binding.naviVM?.fragmentReplace(fragMailBox)
                                        val intent =
                                            Intent(this@MainActivity, TalkActivity::class.java)
                                        intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                                        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                                        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                                        intent.putExtra(
                                            AppKeyValue.instance.talkOtherTalkId,
                                            otherTalkId
                                        )
                                        startActivity(intent)
                                    }

                                    resources.getString(R.string.push_notification_type_new_member) -> {
                                        arrMenusPress = 0
                                        setPress(arrMenusPress)

                                        val bundle = Bundle()
                                        bundle.putString(
                                            AppKeyValue.instance.pushNewMember,
                                            AppKeyValue.instance.pushNewMember
                                        )
                                        fragmentBundleReplace(fragIdeal, bundle)
                                        binding.naviVM?.fragmentNumber?.value = fragIdeal
                                    }

                                    resources.getString(R.string.push_notification_type_love) -> {
                                        val loveDialog = LoveDialog()
                                        loveDialog.setLoveMbNo(mbNo)
                                        loveDialog.show(
                                            supportFragmentManager,
                                            AppKeyValue.instance.tagLoveDlg
                                        )
                                    }

                                    resources.getString(R.string.push_notification_type_notice) -> {
                                        val intent =
                                            Intent(this@MainActivity, NoticeActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            } else {
//                                fab_act_main_logout.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestLogin(
            ServerApi.instance.loginMethod,
            id,
            password,
            instanceId,
            type
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

//    override fun onBillingInitialized() {
//        // BillingProcessor가 초기화되고, 구매 준비가 되면 호출된다.
//        // 이 부분에서 구매할 아이템들을 리스트로 구성해서 보여주는 코드를 구현하면된다.
//    }
//
//    override fun onPurchaseHistoryRestored() {
//        // 깃헙 설명에보면 Called when purchase history was restored and the contactItemList of all owned PRODUCT ID's was loaded from Google Play
//        // 라고 되어있는데 언제 쓸런지는 잘 모르겠다.
//    }
//
//    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
//        // 메소드이름이 다 말해주듯...특정 제품 ID를 가진 아이템의 구매 성공시 호출된다.
//        // 여기에서는 보상을 지급하거나, comsume을 할 필요성이있는 아이템이라면 comsume하는 작업을 하면된다.
//
//        val purchaseInfoData = PurchaseInfoData()
//        purchaseInfoData.coin = coin
//        purchaseInfoData.orderId = details?.purchaseInfo?.purchaseData?.orderId
//        purchaseInfoData.productId = details?.purchaseInfo?.purchaseData?.productId
//
//        EventBus.sendEventChargeCoin(purchaseInfoData)
//    }
//
//    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        // 구매시 어떤 오류가 발생했을 때 호출된다.
//        // 구매자가 구매과정에서 그냥 취소해도 발생되는데, 이때의 errorCode는  Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 라고한다.
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        billingProcessor?.handleActivityResult(requestCode, resultCode, data)?.let {
//            if (!it) {
//                super.onActivityResult(requestCode, resultCode, data)
//            }
//        }
    }

    override fun onBackPressed() {
        when {
//            fam_act_main_more.isOpened -> {
////                fab_act_main_more.isSelected = false
//                fam_act_main_more.close(true)
//                iv_act_main_shadow.visibility = View.GONE
//
//                setPress(arrMenusPress)
//            }

            newFragment !is MainFragment -> {
                arrMenusPress = 3
                setPress(arrMenusPress)
                binding.naviVM?.fragmentReplace(fragMain)
            }

            else -> Utility.instance.showTwoButtonAlert(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.msg_app_finish),
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        finish()
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        val seeDayLater = "seeDayLater"
        val thatDay = Utility.instance.getPref(this, seeDayLater)

        if (Utility.instance.isDayLater(thatDay,1)||true){
            val df = SimpleDateFormat("yyMMdd", Locale.getDefault())
            val nowTime = System.currentTimeMillis()
            val today = Date(nowTime)
            Utility.instance.savePref(baseContext,seeDayLater,df.format(today))

            val subscriber = object : DisposableObserver<ResultItem<NoticePopupData>>() {
                override fun onNext(t: ResultItem<NoticePopupData>) {
                    t.item?.let {
                        Utility.instance.showNotice(this@MainActivity,it.nt_title!!,it.nt_subtitle!!,it.nt_subtitle2!!,it.nt_link!!)
                    }
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                }

            }
            MainModel().getNotice(subscriber)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

}
