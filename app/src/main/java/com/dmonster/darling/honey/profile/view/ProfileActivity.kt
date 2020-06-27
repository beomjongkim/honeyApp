package com.dmonster.darling.honey.profile.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.databinding.ActivityProfileBinding
import com.dmonster.darling.honey.dialog.*
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.myinformation.data.PictureMarryData
import com.dmonster.darling.honey.myinformation.viewmodel.MarriageCertVM
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.profile.data.ProfilePictureData
import com.dmonster.darling.honey.profile.presenter.ProfileContract
import com.dmonster.darling.honey.profile.presenter.ProfilePresenter
import com.dmonster.darling.honey.profile.view.adapter.ProfilePictureAdapter
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.common.FlowLayout
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.internal.Util
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileActivity : BaseActivity(), ProfileContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ProfileContract.Presenter
    private lateinit var mAdapter: ProfilePictureAdapter

    private lateinit var textArray: Array<TextView?>
    private lateinit var layoutArrayId: Array<Int>
    private lateinit var layoutArray: Array<LinearLayout?>
    private lateinit var rewardVM : RewardVM

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var mbNo: String? = null
    private var gender: String? = null
    private var otherId: String? = null
    private var otherGender: String? = null
    private var otherProfileImage: String? = null
    private var otherType: String? = null
    private var otherArea: String? = null
    private var otherAge: String? = null
    private var roomNo: String? = null
    private var express: String? = null

    private var itemUseCheck: Boolean = false
    private var talkCheck: Boolean = false
    private lateinit var binding: ActivityProfileBinding

    private var customPopup: CustomPopup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
        setViewModel()
        init()
        setListener()
        setEventBusListener()
    }

    private fun setViewModel() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.certifyVM = MarriageCertVM().also {
            it.setFragmentManager(supportFragmentManager)
        }
        binding.lifecycleOwner = this
    }

    private fun init() {
        ctb_act_profile_toolbar.setTitle(resources.getString(R.string.profile_title))
        disposeBag = CompositeDisposable()

        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
                .toInt()
        val itemHeight =
            (Utility.instance.getWidth(this) - (rv_act_profile_picture_list.paddingLeft + rv_act_profile_picture_list.paddingRight) - px * 6 - px * 10) / 3
        mAdapter = ProfilePictureAdapter(itemHeight)
        viewLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        rv_act_profile_picture_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter
        }

        textArray = arrayOf(
            tv_act_profile_name,
            tv_act_profile_age,
            tv_act_profile_income,
            tv_act_profile_sibling,
            tv_act_profile_hometown,
            tv_act_profile_job,
            tv_act_profile_fortune,
            tv_act_profile_education,
            tv_act_profile_car,
            tv_act_profile_religion,
            tv_act_profile_parents,
            tv_act_profile_marry_plan,
            tv_act_profile_divorce,
            tv_act_profile_divorce_year,
            tv_act_profile_children,
            tv_act_profile_height,
            tv_act_profile_weight,
            tv_act_profile_drinking,
            tv_act_profile_smoking,
            tv_act_profile_blood,
            tv_act_profile_route
        )

        layoutArray = arrayOf(
            ll_act_profile_sibling,
            ll_act_profile_hometown,
            ll_act_profile_job,
            ll_act_profile_income,
            ll_act_profile_fortune,
            ll_act_profile_education,
            ll_act_profile_religion,
            ll_act_profile_parents,
            ll_act_profile_marry_plan,
            ll_act_profile_divorce_layout,
            ll_act_profile_divorce_year_layout,
            ll_act_profile_children,
            ll_act_profile_height,
            ll_act_profile_weight,
            ll_act_profile_drinking,
            ll_act_profile_smoking,
            ll_act_profile_blood,
            ll_act_profile_character,
            ll_act_profile_hobby,
            ll_act_profile_car,
            ll_act_profile_datestyle,
            ll_act_profile_mystyle,
            ll_act_profile_route
        )

        layoutArrayId = arrayOf(
            R.id.ll_act_profile_sibling,
            R.id.ll_act_profile_hometown,
            R.id.ll_act_profile_job,
            R.id.ll_act_profile_income,
            R.id.ll_act_profile_fortune,
            R.id.ll_act_profile_education,
            R.id.ll_act_profile_religion,
            R.id.ll_act_profile_parents,
            R.id.ll_act_profile_marry_plan,
            R.id.ll_act_profile_divorce,
            R.id.ll_act_profile_divorce_year,
            R.id.ll_act_profile_children,
            R.id.ll_act_profile_height,
            R.id.ll_act_profile_weight,
            R.id.ll_act_profile_drinking,
            R.id.ll_act_profile_smoking,
            R.id.ll_act_profile_blood,
            R.id.ll_act_profile_character,
            R.id.ll_act_profile_hobby,
            R.id.ll_act_profile_car,
            R.id.ll_act_profile_datestyle,
            R.id.ll_act_profile_mystyle,
            R.id.ll_act_profile_route
        )

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mbNo = intent.getStringExtra(AppKeyValue.instance.profileMbNo)
        gender = Utility.instance.UserData().getUserGender()

        express = resources.getString(R.string.profile_interest)
        tv_act_profile_express.text = express


        mPresenter = ProfilePresenter()
        mPresenter.attachView(this)

        ll_act_profile_progress.visibility = View.VISIBLE
        mPresenter.getProfileSample(this, id, mbNo)

        rewardVM = RewardVM(this)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("ProfileActivity", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("ProfileActivity", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                ll_act_profile_progress.visibility = View.VISIBLE
                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                        ll_act_profile_progress.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        ll_act_profile_progress.visibility = View.GONE
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                Utility.instance.showToast(this@ProfileActivity, "성공적으로 이용권을 구매하였습니다.")
                            }
                        }
                        ll_act_profile_progress.visibility = View.GONE
                    }
                }
                ItemModel().buyItem(id, 1, subscriber)
                Log.d("ProfileActivity", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("ProfileActivity", "Ad failed to display.")
            }
        }
    }

    private fun setListener() {
        layoutArray = arrayOfNulls(23)
        for (i in layoutArray.indices) {
            layoutArray[i] = findViewById<LinearLayout>(layoutArrayId[i])

            layoutArray[i]?.let { it ->
                RxView.clicks(it)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        if (id == otherId) {
                            Utility.instance.showAlert(
                                this,
                                resources.getString(R.string.app_name),
                                resources.getString(R.string.msg_profile_error_own),
                                DialogInterface.OnClickListener { dialog, which -> })
                        } else {
                            ll_act_profile_progress.visibility = View.VISIBLE
//                            if (itemUseCheck || (gender == "F" && gender != otherGender)) {
                           if (gender == otherGender) {
                                ll_act_profile_progress.visibility = View.GONE
                                Utility.instance.showAlert(
                                    this,
                                    resources.getString(R.string.app_name),
                                    resources.getString(R.string.msg_profile_error_good),
                                    DialogInterface.OnClickListener { dialog, which -> })
                            } else {
//                                mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdProfile)
                                mPresenter.checkPass(id,AppKeyValue.instance.itemIdProfile)
                            }
                        }
                    }
            }?.let { disposeBag.add(it) }
        }


//        차단하기, 신고하기
        disposeBag.add(RxView.clicks(btn_act_profile_three_dot)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (ll_act_profile_block_report.tag == View.VISIBLE) {
                    ll_act_profile_block_report.tag = View.GONE
                    ll_act_profile_block_report.visibility = View.GONE
                } else {
                    ll_act_profile_block_report.tag = View.VISIBLE
                    ll_act_profile_block_report.visibility = View.VISIBLE
                }

            })
        /*    호감있어요, 관심있어요    */
        disposeBag.add(RxView.clicks(ll_act_profile_express)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val talkId = tv_act_profile_talk_id.text.toString()
                when {
                    id == otherId -> {
                        Utility.instance.showAlert(
                            this,
                            resources.getString(R.string.app_name),
                            resources.getString(R.string.msg_profile_error_own),
                            DialogInterface.OnClickListener { dialog, which -> })
                    }

                    gender == otherGender -> {
                        Utility.instance.showAlert(
                            this,
                            resources.getString(R.string.app_name),
                            resources.getString(R.string.msg_profile_error_good),
                            DialogInterface.OnClickListener { dialog, which -> })
                    }

                    else -> {
//                        if (gender == "F") {
//                            customPopup = CustomPopup(
//                                this,
//                                getString(R.string.interest_express),
//                                getString(R.string.popup_interest_title),
//                                R.drawable.main_navi_ideal_on,
//                                object : CustomDialogInterface {
//                                    override fun onCancel(v: View) {
//                                        customPopupDismiss()
//                                    }
//
//                                    override fun onConfirm(v: View) {
//                                        val intent = Intent(v.context, InterestActivity::class.java)
//                                        intent.putExtra(
//                                            AppKeyValue.instance.goodOtherId,
//                                            otherId
//                                        )
//                                        intent.putExtra(
//                                            AppKeyValue.instance.goodOtherProfileImage,
//                                            otherProfileImage
//                                        )
//                                        intent.putExtra(
//                                            AppKeyValue.instance.goodOtherTalkId,
//                                            talkId
//                                        )
//                                        intent.putExtra(
//                                            AppKeyValue.instance.goodOtherType,
//                                            otherType
//                                        )
//                                        startActivity(intent)
//                                        customPopupDismiss()
//                                    }
//                                })
//                            customPopup?.popupVM?.let { it1 ->
//                                it1.positiveText.value = "보내기"
//                                it1.negativeText.value = "취소"
//                            }
//                            customPopup?.show()
//
//                        } else {
                            mPresenter.checkPass(id, AppKeyValue.instance.itemIdGood)
//
//                        }
                    }
                }
            })

        /*    톡하기    */
        disposeBag.add(RxView.clicks(ll_act_profile_talk)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (id == otherId) {
                    Utility.instance.showAlert(
                        this,
                        resources.getString(R.string.app_name),
                        resources.getString(R.string.msg_profile_error_own),
                        DialogInterface.OnClickListener { dialog, which -> })
                } else if (gender == otherGender) {
                    Utility.instance.showAlert(
                        this,
                        resources.getString(R.string.app_name),
                        resources.getString(R.string.msg_profile_error_gender),
                        DialogInterface.OnClickListener { dialog, which -> })
                } else {
                    if (talkCheck) {
                        val talkId = tv_act_profile_talk_id.text.toString()
                        val intent = Intent(this, TalkActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                        intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
                        /*    상단 타이틀정보    */
                        intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
                        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                        startActivity(intent)
                    } else {
//                        if (gender == "F") {
//                            val talkId = tv_act_profile_talk_id.text.toString()
//                            val intent = Intent(this, TalkActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
//                            intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
//                            intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
//                            /*    상단 타이틀정보    */
//                            intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
//                            intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
//                            intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
//                            startActivity(intent)
//                        } else {
                            ll_act_profile_progress.visibility = View.VISIBLE
//                            mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
                            mPresenter.checkPass(id, AppKeyValue.instance.itemIdTalk)
//                        }
                    }
                }
            })

        /*    신고하기    */
        disposeBag.add(RxView.clicks(tv_act_profile_notify)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (id == otherId) {
                    Utility.instance.showAlert(
                        this,
                        resources.getString(R.string.app_name),
                        resources.getString(R.string.msg_profile_error_own),
                        DialogInterface.OnClickListener { dialog, which -> })
                } else {
                    val notifyDialog = NotifyDialog()
                    notifyDialog.setMbNumber(mbNo)
                    notifyDialog.show(supportFragmentManager, AppKeyValue.instance.tagNotifyDlg)
                }
            })

        /*    차단하기    */
        disposeBag.add(RxView.clicks(tv_act_profile_block)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (id == otherId) {
                    Utility.instance.showAlert(
                        this,
                        resources.getString(R.string.app_name),
                        resources.getString(R.string.msg_profile_error_own),
                        DialogInterface.OnClickListener { dialog, which -> })
                } else {
                    val talkId = tv_act_profile_talk_id.text.toString()
                    customPopup = CustomPopup(
                        this,
                        getString(R.string.popup_title_block),
                        String.format(resources.getString(R.string.msg_block), talkId),
                        R.drawable.ic_block,
                        object : CustomDialogInterface {
                            override fun onConfirm(v: View) {
                                ll_act_profile_progress.visibility = View.VISIBLE
                                mPresenter.setBlock(id, mbNo, AppKeyValue.instance.blockBlock)
                                customPopupDismiss()
                            }

                            override fun onCancel(v: View) {
                                customPopupDismiss()
                            }

                        })

                    customPopup?.show()
                }
            })

        /*    이미지 상세보기    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun setEventBusListener() {
        EventBus.subjectCoinCharge.subscribe {
            if (it == AppKeyValue.instance.eventBusCoinCharge) {
                finish()
            }
        }.addTo(disposeBag)
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val talkId = tv_act_profile_talk_id.text.toString()
        val dataImg = mAdapter.dataImg

        when {
            gender == otherGender -> {
                Utility.instance.showAlert(
                    this,
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.msg_profile_error_good),
                    DialogInterface.OnClickListener { dialog, which -> })
            }

//            itemUseCheck || (gender == "F" && gender != otherGender) || position == 0 -> {
           (gender != otherGender) || position == 0 -> {
                val intent = Intent(this, ImageDetailActivity::class.java)
                intent.putExtra(AppKeyValue.instance.profileDetailOtherId, otherId)
                intent.putExtra(AppKeyValue.instance.profileDetailTalkId, talkId)
                intent.putExtra(AppKeyValue.instance.profileDetailImagePosition, position)
                intent.putExtra(AppKeyValue.instance.profileDetailImage, dataImg)
//                if (gender != "F") {
                intent.putExtra(AppKeyValue.instance.profileDetailItemCheck, itemUseCheck)
//                }
                intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
                intent.putExtra(AppKeyValue.instance.profileDetailOtherArea, otherArea)
                intent.putExtra(AppKeyValue.instance.profileDetailOtherAge, otherAge)
                startActivity(intent)
            }

            else -> {
                ll_act_profile_progress.visibility = View.VISIBLE
//                mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdProfile)
                mPresenter.checkPass(id, AppKeyValue.instance.itemIdProfile)
            }
        }
    }

    /*    키워드형식 레이아웃    */
    private fun keyWordSetLayout(flowLayout: FlowLayout, array: Array<String>) {
        flowLayout.removeAllViews()
        for (i in array.indices) {
            val textLayoutParams = FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textLayoutParams.horizontalSpacing =
                resources.getDimension(R.dimen.default_margin_5dp).toInt()
            val textView = TextView(this)
            textView.apply {
                text = array[i]
                setBackgroundResource(R.drawable.bg_border_btn_warm_gray)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
                setTextColor(Color.argb(166, 65, 65, 65))
                resources.getDimension(R.dimen.default_margin_10dp).toInt()
                    .let { setPadding(it, it / 2, it, it / 2) }
                gravity = Gravity.CENTER
                layoutParams = textLayoutParams
            }
            flowLayout.addView(textView)
        }
    }

    /*    프로필 열람전 정보    */
    override fun setProfileSampleComplete(
        id: String?,
        gender: String?,
        talkId: String?,
        area: String?,
        genderStr: String?,
        type: String?,
        name: String?,
        age: String?,
        birth: String?,
        car: String?,
        myStyle: String?,
        introduce: String?,
        family: String?,
        data: ProfilePictureData?,
        marryData: PictureMarryData?,
        mbImgCnt: String?,
        itemUse: String?
    ) {

        if (marryData?.mbImg!![0] != "") binding.certifyVM?.imgUri?.value =
            Uri.parse(marryData.mbImgThumb!![0])
        binding.certifyVM?.pictureMarryData?.value = marryData
        otherId = id
        otherGender = gender
        otherProfileImage = data?.mbImgThumb?.get(0)
        otherType = type

        tv_act_profile_marry.text = type
        if (otherType == resources.getString(R.string.information_member_marry)) {
            tv_act_profile_marry.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_main_type_marry
                )
            )
            tv_act_profile_marry.background =
                ContextCompat.getDrawable(this, R.drawable.bg_border_round_marry)
        } else {
            tv_act_profile_marry.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_main_type_remarry
                )
            )
            tv_act_profile_marry.background =
                ContextCompat.getDrawable(this, R.drawable.bg_border_round_remarry)
        }

        val areaArray = area?.split(" > ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        val area01 = areaArray?.get(0)
        otherArea = area01
        val nowTime = System.currentTimeMillis()
        val date = Date(nowTime)
        val dateFormat = SimpleDateFormat(resources.getString(R.string.utility_date_year))
        val getYear = dateFormat.format(date)
        val getYearFormer = birth?.toInt()?.minus(1)?.let { getYear.toInt().minus(it) }
        otherAge = String.format(resources.getString(R.string.age), getYearFormer?.toString())

        ctb_act_profile_toolbar.setTitle(
            String.format(
                resources.getString(R.string.profile_title_talk),
                talkId
            )
        )
        tv_act_profile_talk_id.text = talkId
        tv_act_profile_area.text = area
        tv_act_profile_name.text = name
        tv_act_profile_age.text = String.format(resources.getString(R.string.age_birth), age, birth)
        tv_act_profile_car.text = car

        tv_act_profile_introduce.text = introduce
        tv_act_profile_introduce.text = introduce
        tv_act_profile_family.text = family

        mAdapter.dataImg.clear()
        mAdapter.dataThumb.clear()

        data?.mbImg?.let {
            for (i in 0..it.size.minus(1)) {
                mAdapter.dataImg.add(it[i])
            }
            Glide.with(applicationContext).load(it[0]).into(iv_act_profile_member_info)
        }

        tv_act_profile_member_pic_size.text = mbImgCnt

        data?.mbImgThumb?.let {
            for (i in 0..it.size.minus(1)) {
                mAdapter.dataThumb.add(it[i])
            }
        }
        mAdapter.notifyDataSetChanged()

//        itemUseCheck = AppKeyValue.instance.keyYes == itemUse
//        if (itemUseCheck) {
//            ll_act_profile_progress.visibility = View.VISIBLE
//            mPresenter.getProfile(this, this.id, mbNo)
//        }

        mPresenter.getTalkCheck(this.id, otherId)
    }

    /*    프로필 열람전 호출실패    */
    override fun setProfileSampleFailed(error: String?) {
        ll_act_profile_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
        finish()
    }


    /*    프로필 열람후 모든정보    */
    override fun setProfileComplete(
        talkId: String?,
        area: String?,
        genderStr: String?,
        type: String?,
        name: String?,
        age: String?,
        birth: String?,
        income: String?,
        sibling: String?,
        hometown: String?,
        job: String?,
        fortune: String?,
        education: String?,
        car: String?,
        religion: String?,
        parents: String?,
        marryPlan: String?,
        divorce: String?,
        divorceYear: String?,
        children: String?,
        height: String?,
        weight: String?,
        drinking: String?,
        smoking: String?,
        blood: String?,
        character: String?,
        hobby: String?,
        myStyle: String?,
        dateStyle: String?,
        introduce: String?,
        family: String?,
        data: ProfilePictureData?,
        marryData: PictureMarryData?,
        mbImgCnt: String?,
        route: String?
    ) {

        if (marryData?.mbImg!![0] != "") binding.certifyVM?.imgUri?.value =
            Uri.parse(marryData.mbImg!![0])
        binding.certifyVM?.pictureMarryData?.value = marryData

        tv_act_profile_talk_id.text = talkId
        tv_act_profile_area.text = area
        tv_act_profile_name.text = name
        tv_act_profile_age.text = String.format(resources.getString(R.string.age_birth), age, birth)
        tv_act_profile_income.text = income
        tv_act_profile_sibling.text = sibling
        tv_act_profile_hometown.text = hometown
        tv_act_profile_job.text = job
        tv_act_profile_fortune.text = fortune
        tv_act_profile_education.text = education
        tv_act_profile_car.text = car
        tv_act_profile_religion.text = religion
        tv_act_profile_parents.text = parents
        tv_act_profile_marry_plan.text = marryPlan
        tv_act_profile_divorce.text = divorce
        tv_act_profile_divorce_year.text = divorceYear

        if (!TextUtils.isEmpty(children)) {
            val childrenArray =
                children?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            when (childrenArray?.size) {
                1 -> {
                    val childrenMale = childrenArray[0]
                    tv_act_profile_children.text =
                        String.format(resources.getString(R.string.children_male), childrenMale)
                }

                2 -> {
                    val childrenMale = childrenArray[0]
                    val childrenFemale = childrenArray[1]
                    tv_act_profile_children.text = String.format(
                        resources.getString(R.string.children),
                        childrenMale,
                        childrenFemale
                    )
                }
            }
        }

        tv_act_profile_height.text = String.format(resources.getString(R.string.height), height)
        tv_act_profile_weight.text = String.format(resources.getString(R.string.weight), weight)
        tv_act_profile_drinking.text = drinking
        tv_act_profile_smoking.text = smoking
        tv_act_profile_blood.text = blood

        val characterArray =
            character?.split(",".toRegex())?.dropLastWhile { it2 -> it2.isEmpty() }?.toTypedArray()
        characterArray?.let { keyWordSetLayout(fl_act_profile_chatacter, it) }

        val hobbyArray =
            hobby?.split(",".toRegex())?.dropLastWhile { it2 -> it2.isEmpty() }?.toTypedArray()
        hobbyArray?.let { keyWordSetLayout(fl_act_profile_hobby, it) }

        val myStyleArray =
            myStyle?.split(",".toRegex())?.dropLastWhile { it2 -> it2.isEmpty() }?.toTypedArray()
        myStyleArray?.let { keyWordSetLayout(fl_act_profile_mystyle, it) }

        val dateStyleArray =
            dateStyle?.split(",".toRegex())?.dropLastWhile { it2 -> it2.isEmpty() }?.toTypedArray()
        dateStyleArray?.let { keyWordSetLayout(fl_act_profile_datestyle, it) }

        tv_act_profile_introduce.text = introduce
        tv_act_profile_family.text = family
        tv_act_profile_route.text = route

        mAdapter.dataImg.clear()

        mAdapter.dataThumb.clear()
        data?.mbImg?.let {
            for (i in 0..it.size.minus(1)) {
                mAdapter.dataImg.add(it[i])
            }
        }
        tv_act_profile_member_pic_size.text = mbImgCnt

        data?.mbImgThumb?.let {
            for (i in 0..it.size.minus(1)) {
                mAdapter.dataThumb.add(it[i])
            }
        }
        mAdapter.notifyDataSetChanged()

        for (i in 0..textArray.size.minus(1)) {
            textArray[i]?.visibility = View.VISIBLE
        }

        for (i in 0..layoutArray.size.minus(1)) {
            layoutArray[i]?.visibility = View.GONE
        }

        ll_act_profile_progress.visibility = View.GONE

        mAdapter.setBlurView(false)
    }

    /*    프로필 열람후 호출실패    */
    override fun setProfileFailed(error: String?) {
        ll_act_profile_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    채팅방여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        when (result) {
            "Y" -> {
                talkCheck = true
                this.roomNo = roomNo
            }
            "N" -> talkCheck = false
        }
        ll_act_profile_progress.visibility = View.GONE
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_act_profile_progress.visibility = View.GONE

        val talkId = tv_act_profile_talk_id.text.toString()
        val intent = Intent(this, TalkActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
        intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
        /*    상단 타이틀정보    */
        intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
        startActivity(intent)
    }

    /*    톡하기 호출실패    */
    override fun setTalkFailed(error: String?) {
        ll_act_profile_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    아이템 사용    */
    override fun setItemUseComplete(type: String?, result: String?) {
        var content: String = ""
        var itemId: String? = null
        var title: String = ""
        var imgId: Int = -1
        var talkId : String? = tv_act_profile_talk_id.text.toString()
        itemUseCheck =true
        when (result) {
            "Y" -> {
                when (type) {
                    AppKeyValue.instance.itemIdProfile -> {
                        title = "프로필 열람"
                        content = resources.getString(R.string.app_intorduce) +"\n\n"+ talkId + resources.getString(R.string.msg_profile_look_item_use)
                        itemId = AppKeyValue.instance.itemIdProfile
                    }

                    AppKeyValue.instance.itemIdTalk -> {
                        title = "톡하기"
                        content = talkId + resources.getString(R.string.msg_profile_talk_item_use)
                        itemId = AppKeyValue.instance.itemIdTalk
                        imgId = R.drawable.ic_talk_vivid

                    }

                    AppKeyValue.instance.itemIdGood -> {
                        title = "아이템 사용"
                        content = String.format(
                            resources.getString(R.string.msg_profile_love_item_use),
                            express
                        )
                        itemId = AppKeyValue.instance.itemIdGood
                    }
                }

                content.let {

                    customPopup = CustomPopup(
                        this,
                        title,
                        content,
                        imgId,
                        object : CustomDialogInterface {
                            override fun onCancel(v: View) {
                                customPopupDismiss()
                            }

                            override fun onConfirm(v: View) {
                                if (type == AppKeyValue.instance.itemIdGood) {
                                    val intent = Intent(this@ProfileActivity, GoodActivity::class.java)
                                    intent.putExtra(AppKeyValue.instance.goodOtherId, otherId)
                                    intent.putExtra(AppKeyValue.instance.goodOtherProfileImage, otherProfileImage)
                                    intent.putExtra(AppKeyValue.instance.goodOtherTalkId, talkId)
                                    intent.putExtra(AppKeyValue.instance.goodOtherType, otherType)
                                    startActivity(intent)

                                } else {
                                    ll_act_profile_progress.visibility = View.VISIBLE
                                    mPresenter.setItemUse(v.context, id, itemId, mbNo, otherId)
                                }
                                customPopupDismiss()
                            }
                        })

                    customPopup?.show()

                }
            }

            "N" -> {
                when (type) {
                    AppKeyValue.instance.itemIdProfile -> {
                        content = String.format(
                            resources.getString(R.string.msg_profile_item),
                            resources.getString(R.string.msg_profile_item_look)
                        )
                    }

                    AppKeyValue.instance.itemIdTalk -> {
                        content = String.format(
                            resources.getString(R.string.msg_profile_item),
                            resources.getString(R.string.msg_profile_item_talk)
                        )
                    }

                    AppKeyValue.instance.itemIdGood -> {
                        content =
                            String.format(resources.getString(R.string.msg_profile_item), express)
                    }
                }

                content.let {

                    Utility.instance.showTwoButtonAlert(
                        this,
                        resources.getString(R.string.app_name),
                        it,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                when (type) {
                                    AppKeyValue.instance.itemIdProfile -> {
                                        val profileDialog = ItemProfileDialog()
                                        profileDialog.show(
                                            supportFragmentManager,
                                            AppKeyValue.instance.tagItemProfileDlg
                                        )
                                    }

                                    AppKeyValue.instance.itemIdTalk -> {
                                        val talkDialog = ItemTalkDialog()
                                        talkDialog.show(
                                            supportFragmentManager,
                                            AppKeyValue.instance.tagItemTalkDlg
                                        )
                                    }

                                    AppKeyValue.instance.itemIdGood -> {
                                        val goodDialog = ItemGoodDialog()
                                        goodDialog.show(
                                            supportFragmentManager,
                                            AppKeyValue.instance.tagItemGoodDlg
                                        )
                                    }
                                }
                            }
                        })
                }
            }
        }

        ll_act_profile_progress.visibility = View.GONE
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_act_profile_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    차단하기    */
    override fun setBlockComplete(message: String?) {
        ll_act_profile_progress.visibility = View.GONE
        message?.let {
            Utility.instance.showAlert(
                this,
                resources.getString(R.string.app_name),
                it,
                DialogInterface.OnClickListener { dialog, which -> })
        }
    }

    /*    차단하기 호출실패    */
    override fun setBlockFailed(error: String?) {
        ll_act_profile_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun setPassNeed() {
        itemUseCheck = false
        ll_act_profile_progress.visibility = View.GONE

        val popup = CustomPopup(this, "이용권 구매", "이용권을 구매해서 아래 기능을 마음껏 이용해보세요!\n" +getString(R.string.msg_freepass_description), R.drawable.ic_talk_vivid, object: CustomDialogInterface{
            override fun onConfirm(v: View) {
                if (rewardVM.rewardedAd.isLoaded) {
                    rewardVM.rewardedAd.show(this@ProfileActivity, rewardVM.adCallback)
                }
            }

            override fun onCancel(v: View) {
                val intent = Intent(this@ProfileActivity,MainActivity::class.java)
                intent.putExtra(AppKeyValue.instance.goToMarket, true)
                startActivity(intent)
            }
        })
        popup.popupVM.negativeText.value = "1개월 이용권\n구매하기"
        popup.popupVM.positiveText.value = "광고 시청 후\n이용권 받기"
        popup.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
    }
}
