package com.dmonster.darling.honey.myinformation.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.customview.DialogSelect
import com.dmonster.darling.honey.databinding.FragmentMyInfoMemberBinding
import com.dmonster.darling.honey.dialog.SelectorMemberDialog
import com.dmonster.darling.honey.information.view.PhoneAuthActivity
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.myinformation.data.PictureData
import com.dmonster.darling.honey.myinformation.data.PictureMarryData
import com.dmonster.darling.honey.myinformation.view.adapter.MyInfoPictureAdapter
import com.dmonster.darling.honey.myinformation.presenter.MyInformationContract
import com.dmonster.darling.honey.myinformation.presenter.MyInformationPresenter
import com.dmonster.darling.honey.myinformation.viewmodel.MarriageCertVM
import com.dmonster.darling.honey.myinformation.viewmodel.RadioButtonVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.FlowLayout
import com.jakewharton.rxbinding2.view.RxView
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_my_info_member.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MyProfileFragment : BaseFragment(), MyInformationContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: MyInformationContract.Presenter
    private lateinit var mAdapter: MyInfoPictureAdapter

//    private lateinit var marryArrayId: Array<Int>
//    private lateinit var textColorArrayId: Array<Int>


    private var viewLayoutManager: RecyclerView.LayoutManager? = null
//    private lateinit var textColorArraySelect: Array<ImageView?>

    //    private var marryArray: ArrayList<RadioButton>? = null
    private var parentsArray: ArrayList<CheckBox>? = null
    private var marryPlanArray: ArrayList<CheckBox>? = null
    private var divorceArray: ArrayList<CheckBox>? = null
    private var drinkArray: ArrayList<CheckBox>? = null
    private var drinkNumberArray: ArrayList<CheckBox>? = null
    private var smokeArray: ArrayList<CheckBox>? = null
    private var bloodArray: ArrayList<CheckBox>? = null
    private var routeArray: ArrayList<CheckBox>? = null
    //    private var textColorArray: ArrayList<ImageView>? = null
    private var imageUriArray: ArrayList<Uri>? = null
    private var marryCertUriArray: ArrayList<Uri>? = null

    private var characterArray: ArrayList<String>? = null
    private var hobbyArray: ArrayList<String>? = null
    private var myStyleArray: ArrayList<String>? = null
    private var dateStyleArray: ArrayList<String>? = null

    private var isModifyMode = true
    private var sibling01Click = false
    private var sibling02Click = false
    private var childrenClick = false

    private var area01Position: Int = 0
    private var area02Position: Int = 0
    private var siblingMalePosition: Int = 0
    private var siblingFemalePosition: Int = 0
    private var siblingNumberPosition: Int = 0
    private var childrenMalePosition: Int = 0
    private var childrenFemalePosition: Int = 0


    private var textColor: String? = "0"
    private var birth: String? = null
    private var id: String? = null
    private var mbNo: String? = null
    private var gender: String? = null
    private var phoneAuth: Boolean = false
    private lateinit var binding: FragmentMyInfoMemberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_info_member, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setViewModel()
        setListener()
        setEventBusListener()
        setModifyMode()
    }

    private fun setViewModel() {
        binding.certifyVM = MarriageCertVM().also {
            fragmentManager?.let { it1 -> it.setFragmentManager(it1) }
        }
        binding.marriageRadioVM = RadioButtonVM()


        binding.lifecycleOwner = this
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        imageUriArray = ArrayList()
        marryCertUriArray = ArrayList()
        characterArray = ArrayList()
        hobbyArray = ArrayList()
        myStyleArray = ArrayList()
        dateStyleArray = ArrayList()

        activity?.let {
            val px =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
                    .toInt()
            val itemHeight =
                (Utility.instance.getWidth(it) - (rv_frag_my_info_member_list.paddingLeft + rv_frag_my_info_member_list.paddingRight) - px * 6 - px * 10) / 3
            mAdapter = MyInfoPictureAdapter(itemHeight)
            viewLayoutManager = GridLayoutManager(context, 3) as RecyclerView.LayoutManager?
        }

        rv_frag_my_info_member_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1

                }

            })

            isNestedScrollingEnabled = false
        }
        mPresenter = MyInformationPresenter()
        mPresenter.attachView(this)

        ll_frag_my_info_member_progress.visibility = View.VISIBLE
        context?.let {
            id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
            mbNo = Utility.instance.UserData().getUserMb()
            gender = Utility.instance.UserData().getUserGender()
            mPresenter.getMyInformation(it, id, mbNo)
        }

        textColor?.toInt()?.let { setPress(AppKeyValue.instance.infoSelectTextColor, it) }
    }

    private fun setListener() {

//        /*  가입 경로   */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_route)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val routeArray: Array<String?> =
                            arrayOf(
                                getString(R.string.key_information_route01),
                                getString(R.string.key_information_route02),
                                getString(R.string.key_information_route03),
                                getString(R.string.key_information_route04)
                            )
                        val routeDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "가입 경로",
                            routeArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_route.text = routeArray[position]
                                }
                            })
                        routeDialog.setCanceledOnTouchOutside(false)
                        routeDialog.show()
                    }
                }
            })
//
//        /*    결혼계획    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_marry_plan)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val marryPlanArray: Array<String?> = arrayOf(
                            getString(R.string.information_member_marry_plan_select01),
                            getString(R.string.information_member_marry_plan_select02),
                            getString(R.string.information_member_marry_plan_select03),
                            getString(R.string.information_member_marry_plan_select04)
                        )
                        val marryPlanDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "결혼 계획은 있나요?",
                            marryPlanArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_marry_plan.text =
                                        marryPlanArray[position]
                                }
                            })
                        marryPlanDialog.setCanceledOnTouchOutside(false)
                        marryPlanDialog.show()
                    }
                }
            })
//
//        /*    이혼절차    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_divorce)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val divorceArray: Array<String?> = arrayOf(
                            getString(R.string.information_member_divorce_select01),
                            getString(R.string.information_member_divorce_select02),
                            getString(R.string.information_member_divorce_select03),
                            getString(R.string.information_member_divorce_select04)
                        )
                        val divorceDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            getString(R.string.information_member_divorce),
                            divorceArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_divorce.text =
                                        divorceArray[position]
                                }
                            })
                        divorceDialog.setCanceledOnTouchOutside(false)
                        divorceDialog.show()
                    }
                }
            })
//
        /*    연봉 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_income)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val incomeArray = it1.resources.getStringArray(R.array.member_income_array)
                        val incomeDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_income),
                            incomeArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_income.text = incomeArray[position]
                                }
                            })
                        incomeDialog.setCanceledOnTouchOutside(false)
                        incomeDialog.show()
                    }
                }
            })
//        /*    흡연여부    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_smoke)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val smokeArray: Array<String?> = arrayOf(
                            getString(R.string.key_information_smoking),
                            getString(R.string.key_information__prohibit_smoking),
                            getString(R.string.key_information_not_smoking)
                        )
                        val smokeDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "흡연 여부 선택",
                            smokeArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_smoke.text = smokeArray[position]
                                }
                            })
                        smokeDialog.setCanceledOnTouchOutside(false)
                        smokeDialog.show()
                    }
                }
            })
        /*    혈액형 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_blood)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val bloodArray: Array<String?> = arrayOf(
                            getString(R.string.information_member_blood_a),
                            getString(R.string.information_member_blood_b),
                            getString(R.string.information_member_blood_ab),
                            getString(R.string.information_member_blood_o)
                        )
                        val bloodDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "혈액형 선택",
                            bloodArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_blood.text = bloodArray[position]
                                }
                            })
                        bloodDialog.setCanceledOnTouchOutside(false)
                        bloodDialog.show()
                    }
                }
            })

//        /*    부모님 생사 여부  */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_parents)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val parentsArray: Array<String?> =
                            arrayOf(
                                getString(R.string.key_information_parents_select01),
                                getString(R.string.key_information_parents_select02),
                                getString(R.string.key_information_parents_select03),
                                getString(R.string.key_information_parents_select04)
                            )
                        val parentsDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "부모님은 살아 계신가요?",
                            parentsArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_parents.text = parentsArray[position]
                                }
                            })
                        parentsDialog.setCanceledOnTouchOutside(false)
                        parentsDialog.show()
                    }
                }
            })


        /*    글자색상 선택    */
//        textColorArraySelect = arrayOfNulls(11)
//        for (i in textColorArraySelect.indices) {
//            textColorArraySelect[i] = activity?.findViewById<ImageView>(textColorArrayId[i])
//
//            textColorArraySelect[i]?.let { it ->
//                RxView.clicks(it)
//                    .throttleFirst(1, TimeUnit.SECONDS)
//                    .subscribe {
//                        setPress(AppKeyValue.instance.infoSelectTextColor, i)
//                    }
//            }?.let { disposeBag.add(it) }
//        }

        /*    지역 선택    */
        disposeBag.add(RxView.clicks(tv_frag_my_info_member_area)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val area01Array = it1.resources.getStringArray(R.array.member_area_array)
                        val area01Dialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectThree,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_area),
                            area01Array,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    area01Position = position
                                    val area = area01Array[position]
                                    var area02Array: Array<String?> = emptyArray()
                                    when (area) {
                                        area01Array[0] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_seoul_array)
                                        area01Array[1] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_gyeonggi_array)
                                        area01Array[2] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_guangwon_array)
                                        area01Array[3] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_gyeongnam_array)
                                        area01Array[4] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_gyeongbuk_array)
                                        area01Array[5] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_gwangju_array)
                                        area01Array[6] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_daegu_array)
                                        area01Array[7] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_daejeon_array)
                                        area01Array[8] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_busan_array)
                                        area01Array[9] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_sejong_array)
                                        area01Array[10] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_ulsan_array)
                                        area01Array[11] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_incheon_array)
                                        area01Array[12] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_jeonnam_array)
                                        area01Array[13] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_jeonbuk_array)
                                        area01Array[14] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_jeju_array)
                                        area01Array[15] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_chungnam_array)
                                        area01Array[16] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_chungbuk_array)
                                        area01Array[17] -> area02Array =
                                            it1.resources.getStringArray(R.array.member_area_foreign_array)
                                    }

                                    val area02Dialog = SelectorMemberDialog(
                                        it1,
                                        AppKeyValue.instance.memberSelectThree,
                                        AppKeyValue.instance.memberTypeNotLimit,
                                        it1.resources.getString(R.string.information_member_dlg_area),
                                        area02Array,
                                        object : DialogSelect {
                                            override fun onSelect(position: Int, o: Any) {
                                                area02Position = position
                                                tv_frag_my_info_member_area.text = String.format(
                                                    it1.resources.getString(R.string.arrow),
                                                    area01Array[area01Position],
                                                    area02Array[position]
                                                )
                                            }
                                        })
                                    area02Dialog.setCanceledOnTouchOutside(false)
                                    area02Dialog.show()
                                }
                            })
                        area01Dialog.setCanceledOnTouchOutside(false)
                        area01Dialog.show()
                    }
                }
            })

        /*    나이 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_age)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    val ageArray = Utility.instance.getFormer(it1)
                    val ageDialog = SelectorMemberDialog(
                        it1,
                        AppKeyValue.instance.memberSelectThree,
                        AppKeyValue.instance.memberTypeNotLimit,
                        it1.resources.getString(R.string.information_member_dlg_age),
                        ageArray,
                        object : DialogSelect {
                            override fun onSelect(position: Int, o: Any) {
                                tv_frag_my_info_member_age.text = ageArray[position]
                            }
                        })
                    ageDialog.setCanceledOnTouchOutside(false)
                    ageDialog.show()
                }
            })

        /*    형제/자매 선택01    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_sibling_male)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    siblingDialog(tv_frag_my_info_member_sibling_male, 1)
                }
            })

        /*    형제/자매 선택02    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_sibling_female)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    if (sibling01Click) {
                        siblingDialog(tv_frag_my_info_member_sibling_female, 2)
                    }
                }
            })

        /*    형제/자매 선택03    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_sibling_number)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    if (sibling02Click) {
                        siblingDialog(tv_frag_my_info_member_sibling_number, 3)
                    }
                }
            })

        /*    자녀 선택01    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_children_male)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val maleArray =
                            it1.resources.getStringArray(R.array.member_sibling_male_array)
                        val maleDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectThree,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_children),
                            maleArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_children_male.text = maleArray[position]
                                    childrenClick = true
                                    childrenMalePosition = position
                                }
                            })
                        maleDialog.setCanceledOnTouchOutside(false)
                        maleDialog.show()
                    }
                }
            })

        /*    자녀 선택02    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_children_female)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        if (childrenClick) {
                            val femaleArray =
                                it1.resources.getStringArray(R.array.member_sibling_female_array)
                            val femaleDialog = SelectorMemberDialog(
                                it1,
                                AppKeyValue.instance.memberSelectThree,
                                AppKeyValue.instance.memberTypeNotLimit,
                                it1.resources.getString(R.string.information_member_dlg_children),
                                femaleArray,
                                object : DialogSelect {
                                    override fun onSelect(position: Int, o: Any) {
                                        tv_frag_my_info_member_children_female.text =
                                            femaleArray[position]
                                        childrenFemalePosition = position
                                    }
                                })
                            femaleDialog.setCanceledOnTouchOutside(false)
                            femaleDialog.show()
                        }
                    }
                }
            })

        /*    고향 선택01    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_hometown01)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val hometownArray = it1.resources.getStringArray(R.array.member_area_array)
                        val hometownDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectThree,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_hometown),
                            hometownArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_hometown01.text = hometownArray[position]
                                    tv_frag_my_info_member_hometown02.text = null

                                    val hometown = tv_frag_my_info_member_hometown01.text
                                    val hometownArray01 =
                                        it1.resources.getStringArray(R.array.member_area_array)
                                    var hometownArray02: Array<String?> = emptyArray()
                                    when (hometown) {
                                        hometownArray01[0] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_seoul_array)
                                        hometownArray01[1] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_gyeonggi_array)
                                        hometownArray01[2] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_guangwon_array)
                                        hometownArray01[3] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_gyeongnam_array)
                                        hometownArray01[4] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_gyeongbuk_array)
                                        hometownArray01[5] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_gwangju_array)
                                        hometownArray01[6] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_daegu_array)
                                        hometownArray01[7] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_daejeon_array)
                                        hometownArray01[8] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_busan_array)
                                        hometownArray01[9] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_sejong_array)
                                        hometownArray01[10] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_ulsan_array)
                                        hometownArray01[11] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_incheon_array)
                                        hometownArray01[12] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_jeonnam_array)
                                        hometownArray01[13] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_jeonbuk_array)
                                        hometownArray01[14] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_jeju_array)
                                        hometownArray01[15] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_chungnam_array)
                                        hometownArray01[16] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_chungbuk_array)
                                        hometownArray01[17] -> hometownArray02 =
                                            it1.resources.getStringArray(R.array.member_area_foreign_array)
                                    }

                                    val hometownDialog = SelectorMemberDialog(
                                        it1,
                                        AppKeyValue.instance.memberSelectThree,
                                        AppKeyValue.instance.memberTypeNotLimit,
                                        it1.resources.getString(R.string.information_member_dlg_hometown),
                                        hometownArray02,
                                        object : DialogSelect {
                                            override fun onSelect(position: Int, o: Any) {
                                                tv_frag_my_info_member_hometown02.text =
                                                    hometownArray02[position]
                                            }
                                        })
                                    hometownDialog.setCanceledOnTouchOutside(false)
                                    hometownDialog.show()
                                }
                            })
                        hometownDialog.setCanceledOnTouchOutside(false)
                        hometownDialog.show()
                    }
                }
            })

        /*    고향 선택02    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_hometown02)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val hometown = tv_frag_my_info_member_hometown01.text
                        if (!TextUtils.isEmpty(hometown)) {
                            val hometownArray01 =
                                it1.resources.getStringArray(R.array.member_area_array)
                            var hometownArray02: Array<String?> = emptyArray()
                            when (hometown) {
                                hometownArray01[0] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_seoul_array)
                                hometownArray01[1] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_gyeonggi_array)
                                hometownArray01[2] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_guangwon_array)
                                hometownArray01[3] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_gyeongnam_array)
                                hometownArray01[4] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_gyeongbuk_array)
                                hometownArray01[5] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_gwangju_array)
                                hometownArray01[6] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_daegu_array)
                                hometownArray01[7] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_daejeon_array)
                                hometownArray01[8] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_busan_array)
                                hometownArray01[9] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_sejong_array)
                                hometownArray01[10] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_ulsan_array)
                                hometownArray01[11] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_incheon_array)
                                hometownArray01[12] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_jeonnam_array)
                                hometownArray01[13] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_jeonbuk_array)
                                hometownArray01[14] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_jeju_array)
                                hometownArray01[15] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_chungnam_array)
                                hometownArray01[16] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_chungbuk_array)
                                hometownArray01[17] -> hometownArray02 =
                                    it1.resources.getStringArray(R.array.member_area_foreign_array)
                            }

                            val hometownDialog = SelectorMemberDialog(
                                it1,
                                AppKeyValue.instance.memberSelectThree,
                                AppKeyValue.instance.memberTypeNotLimit,
                                it1.resources.getString(R.string.information_member_dlg_hometown),
                                hometownArray02,
                                object : DialogSelect {
                                    override fun onSelect(position: Int, o: Any) {
                                        tv_frag_my_info_member_hometown02.text =
                                            hometownArray02[position]
                                    }
                                })
                            hometownDialog.setCanceledOnTouchOutside(false)
                            hometownDialog.show()
                        }
                    }
                }
            })

        /*    음주 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_drink)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val drinkArray: Array<String?> = arrayOf(
                            getString(R.string.information_member_drinking_not),
                            getString(R.string.information_member_drinking01),
                            getString(R.string.information_member_drinking02),
                            getString(R.string.information_member_drinking03)
                        )
                        val drinkDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "음주 빈도 선택",
                            drinkArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_drink.text = drinkArray[position]
                                    if (position == 0) {
                                        tv_frag_my_info_member_drink_number_title.visibility =
                                            View.GONE
                                        rl_frag_my_info_member_drink_number.visibility = View.GONE
                                        tv_frag_my_info_member_drink_number.text = ""
                                    } else {
                                        tv_frag_my_info_member_drink_number_title.visibility =
                                            View.VISIBLE
                                        rl_frag_my_info_member_drink_number.visibility =
                                            View.VISIBLE
                                    }
                                }
                            })
                        drinkDialog.setCanceledOnTouchOutside(false)
                        drinkDialog.show()
                    }
                }
            })
        /*    주량 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_drink_number)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val drinkNumberArray: Array<String?> = arrayOf(
                            getString(R.string.information_member_drinking_detail01),
                            getString(R.string.information_member_drinking_detail02),
                            getString(R.string.information_member_drinking_detail03),
                            getString(R.string.information_member_drinking_detail04)
                        )
                        val drinkNumberDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeNotLimit,
                            "주량 선택",
                            drinkNumberArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_drink_number.text =
                                        drinkNumberArray[position]
                                    if (position == 0) {
                                        tv_frag_my_info_member_drink_number_title.visibility =
                                            View.GONE
                                        rl_frag_my_info_member_drink_number.visibility = View.GONE
                                        tv_frag_my_info_member_drink_number.text = ""
                                    } else {
                                        tv_frag_my_info_member_drink_number_title.visibility =
                                            View.VISIBLE
                                        rl_frag_my_info_member_drink_number.visibility =
                                            View.VISIBLE
                                    }
                                }
                            })
                        drinkNumberDialog.setCanceledOnTouchOutside(false)
                        drinkNumberDialog.show()
                    }
                }
            })
        /*    직업 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_job)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val jobArray = it1.resources.getStringArray(R.array.member_job_array)
                        val jobDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_job),
                            jobArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_job.text = jobArray[position]
                                }
                            })
                        jobDialog.setCanceledOnTouchOutside(false)
                        jobDialog.show()
                    }
                }
            })

        /*    보유재산 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_fortune)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val fortuneArray =
                            it1.resources.getStringArray(R.array.member_fortune_array)
                        val fortuneDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_fortune),
                            fortuneArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_fortune.text = fortuneArray[position]
                                }
                            })
                        fortuneDialog.setCanceledOnTouchOutside(false)
                        fortuneDialog.show()
                    }
                }
            })

        /*    학력 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_education)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val educationArray =
                            it1.resources.getStringArray(R.array.member_education_array)
                        val educationDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_education),
                            educationArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_education.text = educationArray[position]
                                }
                            })
                        educationDialog.setCanceledOnTouchOutside(false)
                        educationDialog.show()
                    }
                }
            })

        /*    차량 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_car)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val carArray = it1.resources.getStringArray(R.array.member_car_array)
                        val carDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectThree,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_car),
                            carArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_car.text = carArray[position]
                                }
                            })
                        carDialog.setCanceledOnTouchOutside(false)
                        carDialog.show()
                    }
                }
            })

        /*    종교 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_religion)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val religionArray =
                            it1.resources.getStringArray(R.array.member_religion_array)
                        val religionDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectThree,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_religion),
                            religionArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_religion.text = religionArray[position]
                                }
                            })
                        religionDialog.setCanceledOnTouchOutside(false)
                        religionDialog.show()
                    }
                }
            })

        /*    성격 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_character)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val characterArray =
                            it1.resources.getStringArray(R.array.member_character_array)
                        val characterDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeLimit,
                            it1.resources.getString(R.string.information_member_dlg_character),
                            characterArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    this@MyProfileFragment.characterArray?.clear()
                                    val characterStr =
                                        o.toString().replace("[", "").replace("]", "")
                                    val characterSelectArray =
                                        characterStr.split(", ".toRegex())
                                            .dropLastWhile { it2 -> it2.isEmpty() }
                                            .toTypedArray()
                                    keyWordSetLayout(
                                        it1,
                                        fl_frag_my_info_member_character,
                                        characterSelectArray,
                                        this@MyProfileFragment.characterArray
                                    )
                                }
                            })
                        characterDialog.setCanceledOnTouchOutside(false)
                        characterDialog.show()
                    }
                }
            })

//        /*    취미 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_hobby)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val hobbyArray = it1.resources.getStringArray(R.array.member_hobby_array)
                        val hobbyDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeLimit,
                            it1.resources.getString(R.string.information_member_dlg_hobby),
                            hobbyArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    this@MyProfileFragment.hobbyArray?.clear()
                                    val hobbyStr = o.toString().replace("[", "").replace("]", "")
//                                    val hobbySelectArray =
//                                        hobbyStr.split(", ".toRegex())
//                                            .dropLastWhile { it2 -> it2.isEmpty() }
//                                            .toTypedArray()
                                    tv_frag_my_info_member_hobby.text = hobbyStr
                                }
                            })
                        hobbyDialog.setCanceledOnTouchOutside(false)
                        hobbyDialog.show()
                    }
                }
            })

        /*    나의 스타일 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_mystyle)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val myStyleArray: Array<String?> = if (gender == "F") {
                            it1.resources.getStringArray(R.array.member_mystyle_female_array)
                        } else {
                            it1.resources.getStringArray(R.array.member_mystyle_male_array)
                        }
                        val myStyleDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeMyStyleLimit,
                            it1.resources.getString(R.string.information_member_dlg_mystyle),
                            myStyleArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    this@MyProfileFragment.myStyleArray?.clear()
                                    val myStyleStr = o.toString().replace("[", "").replace("]", "")
                                    val myStyleSelectArray =
                                        myStyleStr.split(", ".toRegex())
                                            .dropLastWhile { it2 -> it2.isEmpty() }
                                            .toTypedArray()
                                    keyWordSetLayout(
                                        it1,
                                        fl_frag_my_info_member_mystyle,
                                        myStyleSelectArray,
                                        this@MyProfileFragment.myStyleArray
                                    )
                                }
                            })
                        myStyleDialog.setCanceledOnTouchOutside(false)
                        myStyleDialog.show()
                    }
                }
            })

        /*    데이트 스타일 선택    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_datestyle)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val dateStyleArray =
                            it1.resources.getStringArray(R.array.member_datestyle_array)
                        val dateStyleDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectOne,
                            AppKeyValue.instance.memberTypeLimit,
                            it1.resources.getString(R.string.information_member_dlg_datestyle),
                            dateStyleArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    this@MyProfileFragment.dateStyleArray?.clear()
                                    val dateStyleStr =
                                        o.toString().replace("[", "").replace("]", "")
                                    val dateStyleSelectArray =
                                        dateStyleStr.split(", ".toRegex())
                                            .dropLastWhile { it2 -> it2.isEmpty() }
                                            .toTypedArray()
                                    keyWordSetLayout(
                                        it1,
                                        fl_frag_my_info_member_datestyle,
                                        dateStyleSelectArray,
                                        this@MyProfileFragment.dateStyleArray
                                    )
                                }
                            })
                        dateStyleDialog.setCanceledOnTouchOutside(false)
                        dateStyleDialog.show()
                    }
                }
            })

        /*    이혼(사별)연도    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_divorce_year)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {
                        val divorceYearArray = Utility.instance.getDivorceYear(it1)
                        val divorceYearDialog = SelectorMemberDialog(
                            it1,
                            AppKeyValue.instance.memberSelectTwo,
                            AppKeyValue.instance.memberTypeNotLimit,
                            it1.resources.getString(R.string.information_member_dlg_divorce_year),
                            divorceYearArray,
                            object : DialogSelect {
                                override fun onSelect(position: Int, o: Any) {
                                    tv_frag_my_info_member_divorce_year.text = String.format(
                                        it1.resources.getString(R.string.information_member_divorce_year_content),
                                        divorceYearArray[position]
                                    )
                                }
                            })
                        divorceYearDialog.setCanceledOnTouchOutside(false)
                        divorceYearDialog.show()
                    }
                }
            })

        /*    사진추가    */
        disposeBag.add(RxView.clicks(rl_frag_my_info_member_add_picture)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    if (isModifyMode) {

                        val selectSize = mAdapter.itemCount
                        if (selectSize < 10) {
                            val tedBottomPicker = TedBottomPicker.Builder(it1)
                                .setImageProvider { imageView, imageUri ->
                                    Glide.with(this).load(imageUri)
                                        .apply(RequestOptions().centerCrop()).into(imageView)
                                }
                                .setOnMultiImageSelectedListener { uriList ->
                                    var haveGIF = false
                                    for (i in 0..uriList.size.minus(1)) {
                                        if (!uriList[i].toString().contains(".gif", true)) {
                                            mAdapter.addItem(uriList[i].toString())
                                            imageUriArray?.add(uriList[i])
                                        } else {
                                            haveGIF = true
                                        }
                                    }

//                                    if (mAdapter.itemList.size > 1) {
//                                        iv_frag_my_info_arrow_left.visibility = View.VISIBLE
//                                        iv_frag_my_info_arrow_right.visibility = View.VISIBLE
//                                    } else {
//                                        iv_frag_my_info_arrow_left.visibility = View.GONE
//                                        iv_frag_my_info_arrow_right.visibility = View.GONE
//                                    }
                                    context?.let { it2 ->
                                        if (!haveGIF)
                                            Utility.instance.showToast(
                                                it2,
                                                "삭제하라면 해당 사진을 클릭하세요."
                                            )
                                        else
                                            Utility.instance.showToast(
                                                it2,
                                                "GIF파일(움직이는 사진)은 등록할 수 없습니다."
                                            )
                                    }
                                }
                                .setPreviewMaxCount(1000)
                                .setCameraTile(R.drawable.camera_shooting)
                                .setCameraTileBackgroundResId(R.color.color_black)
                                .setGalleryTile(R.drawable.camera_album)
                                .setGalleryTileBackgroundResId(R.color.color_black)
                                .setSelectMaxCount(10 - selectSize)
                                .create()
                            tedBottomPicker?.show(it1.supportFragmentManager)
                        } else {
                            Utility.instance.showAlert(
                                it1,
                                it1.getString(R.string.app_name),
                                it1.getString(R.string.msg_error_picture),
                                DialogInterface.OnClickListener { dialog, which -> })
                        }
                    }
                }
            })

        /*    추천인 현황    */
        disposeBag.add(RxView.clicks(tv_frag_my_info_member_recommend)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(context, RecommendActivity::class.java)
                startActivity(intent)
            })

        /*    핸드폰 인증    */
        disposeBag.add(RxView.clicks(tv_frag_my_info_member_phone_auth)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                context?.let { it1 ->
                    val phone = et_frag_my_info_member_phone.text.toString()
                    if (TextUtils.isEmpty(phone)) {
                        Utility.instance.showToast(
                            it1,
                            it1.resources.getString(R.string.msg_error_information_member_phone)
                        )
                    } else {
                        val intent = Intent(it1, PhoneAuthActivity::class.java)
                        intent.putExtra(AppKeyValue.instance.intentPhone, phone)
                        startActivity(intent)
                    }
                }
            })



        /*    수정완료    */
        disposeBag.add(RxView.clicks(btn_frag_my_info_member_complete)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                context?.let { it1 ->
                    if (isModifyMode) {
                        val talkId = tv_frag_my_info_member_talk_id.text.toString()
                        val area = tv_frag_my_info_member_area.text.toString()
                        val areaArray =
                            area.split(" > ".toRegex()).dropLastWhile { it2 -> it2.isEmpty() }
                                .toTypedArray()
                        var area01 = ""
                        var area02 = ""
                        if (areaArray.size == 2) {
                            area01 = areaArray[0]
                            area02 = areaArray[1]
                        }
                        val type: String? = if (rb_frag_my_info_member_marry.isChecked) {
                            it1.resources.getString(R.string.information_member_marry)
                        } else {
                            it1.resources.getString(R.string.information_member_remarry)
                        }
                        val firstName = et_frag_my_info_member_first_name.text.toString()
                        val lastName = et_frag_my_info_member_last_name.text.toString()
                        val nameCheck: String? =
                            if (cb_frag_my_info_member_first_name_check.isChecked) {
                                AppKeyValue.instance.keyYes
                            } else {
                                AppKeyValue.instance.keyNo
                            }
                        //val age = tv_frag_my_info_member_age.text.toString().replace(it1.resources.getString(R.string.utility_date_former), "")
                        val siblingMale = tv_frag_my_info_member_sibling_male.text.toString()
                        val siblingFemale = tv_frag_my_info_member_sibling_female.text.toString()
                        val siblingNumber = tv_frag_my_info_member_sibling_number.text.toString()
                        val childrenMale = tv_frag_my_info_member_children_male.text.toString()
                        val childrenFemale = tv_frag_my_info_member_children_female.text.toString()
                        val children = String.format(
                            it1.resources.getString(R.string.comma_int01),
                            childrenMalePosition,
                            childrenFemalePosition
                        )
                        val hometown01 = tv_frag_my_info_member_hometown01.text.toString()
                        val hometown02 = tv_frag_my_info_member_hometown02.text.toString()
                        val job = tv_frag_my_info_member_job.text.toString()
                        val income = tv_frag_my_info_member_income.text.toString()
                        val fortune = tv_frag_my_info_member_fortune.text.toString()
                        val education = tv_frag_my_info_member_education.text.toString()
                        val car = tv_frag_my_info_member_car.text.toString()
                        val religion = tv_frag_my_info_member_religion.text.toString()

                        val parents = tv_frag_my_info_member_parents.text.toString()


                        val marryPlan = tv_frag_my_info_member_marry_plan.text.toString()

                        val divorce = tv_frag_my_info_member_divorce.text.toString()
                        var divorceYear = tv_frag_my_info_member_divorce_year.text.toString()
                        if (!TextUtils.isEmpty(divorceYear)) {
                            divorceYear = divorceYear.substring(0, 4)
                        }

                        val height = et_frag_my_info_member_height.text.toString()
                        val weight = et_frag_my_info_member_weight.text.toString()

                        val drinking = tv_frag_my_info_member_drink.text.toString()

                        val drinkingNumber = tv_frag_my_info_member_drink_number.text.toString()

                        val smoking = tv_frag_my_info_member_smoke.text.toString()

                        val blood = tv_frag_my_info_member_blood.text.toString()
                        val hobby = tv_frag_my_info_member_hobby.text.toString()

                        val character =
                            characterArray?.toString()?.replace("[", "")?.replace("]", "")
                                ?.replace(", ", ",")
                        val myStyle = myStyleArray?.toString()?.replace("[", "")?.replace("]", "")
                            ?.replace(", ", ",")
                        val dateStyle =
                            dateStyleArray?.toString()?.replace("[", "")?.replace("]", "")
                                ?.replace(", ", ",")

                        val introduce = et_frag_my_info_member_introduce.text.toString()
                        val family = et_frag_my_info_member_family.text.toString()

                        val route = tv_frag_my_info_member_route.text.toString()

                        val phone =
                            et_frag_my_info_member_phone.text.toString()/*.replace("-", "")*/

                        mAdapter.deleteArray?.sort()
                        val deleteImage =
                            mAdapter.deleteArray.toString().replace("[", "").replace("]", "")
                                .replace(", ", ",")
                        val imgUri = binding.certifyVM?.imgUri?.value
                        if (!imgUri.toString().contains("http"))
                            imgUri?.let { it2 -> marryCertUriArray?.add(it2) }
                        val deleteMarryImage = binding.certifyVM?.del_mb_marry_img

                        if (TextUtils.isEmpty(talkId)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_talk_id)
                            )
                        } else if (TextUtils.isEmpty(area)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_area)
                            )
                        } else if (TextUtils.isEmpty(firstName) /*|| TextUtils.isEmpty(lastName)*/) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_name)
                            )
                        } else if (TextUtils.isEmpty(siblingMale) || TextUtils.isEmpty(
                                siblingFemale
                            ) || TextUtils.isEmpty(
                                siblingNumber
                            )
                        ) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_sibling)
                            )
                        } else if (TextUtils.isEmpty(hometown01) || TextUtils.isEmpty(hometown02)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_hometown)
                            )
                        } else if (TextUtils.isEmpty(job)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_job)
                            )
                        } else if (TextUtils.isEmpty(income)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_income)
                            )
                        } else if (TextUtils.isEmpty(fortune)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_fortune)
                            )
                        } else if (TextUtils.isEmpty(education)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_education)
                            )
                        } else if (TextUtils.isEmpty(car)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_car)
                            )
                        } else if (TextUtils.isEmpty(religion)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_religion)
                            )
                        } else if (TextUtils.isEmpty(height)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_height)
                            )
                        } else if (TextUtils.isEmpty(weight)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_weight)
                            )
                        } else if (TextUtils.isEmpty(character)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_character)
                            )
                        } else if (TextUtils.isEmpty(hobby)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_hobby)
                            )
                        } else if (TextUtils.isEmpty(myStyle)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_mystyle)
                            )
                        } else if (TextUtils.isEmpty(dateStyle)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_datestyle)
                            )
                        } else if (TextUtils.isEmpty(introduce)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_introduce)
                            )
                        } else if (TextUtils.isEmpty(family)) {
                            Utility.instance.showToast(
                                it1,
                                it1.resources.getString(R.string.msg_error_information_member_family)
                            )
                        } else {
                            if (rb_frag_my_info_member_remarry.isChecked) {
                                if (TextUtils.isEmpty(divorceYear)) {
                                    Utility.instance.showToast(
                                        it1,
                                        it1.resources.getString(R.string.msg_error_information_member_divorceyear)
                                    )
                                    return@let
                                } else if (TextUtils.isEmpty(childrenMale) || TextUtils.isEmpty(
                                        childrenFemale
                                    )
                                ) {
                                    Utility.instance.showToast(
                                        it1,
                                        it1.resources.getString(R.string.msg_error_information_member_children)
                                    )
                                    return@let
                                }
                            }

                            if (rl_frag_my_info_member_phone.visibility == View.VISIBLE) {
                                if (TextUtils.isEmpty(phone)) {
                                    Utility.instance.showToast(
                                        it1,
                                        it1.resources.getString(R.string.msg_error_information_member_phone)
                                    )
                                } else if (!phoneAuth) {
                                    Utility.instance.showToast(
                                        it1,
                                        it1.resources.getString(R.string.msg_error_information_member_phone_auth)
                                    )
                                } else {
                                    Utility.instance.hideSoftKeyboard(activity)
                                    ll_frag_my_info_member_progress.visibility = View.VISIBLE
                                    mPresenter.setMyInformationEdit(
                                        id,
                                        area01,
                                        area02,
                                        type,
                                        firstName,
                                        lastName,
                                        nameCheck,
                                        birth,
                                        siblingMale,
                                        siblingFemale,
                                        siblingNumber,
                                        children,
                                        hometown01,
                                        hometown02,
                                        job,
                                        income,
                                        fortune,
                                        education,
                                        car,
                                        religion,
                                        parents,
                                        marryPlan,
                                        divorce,
                                        divorceYear,
                                        height,
                                        weight,
                                        drinking,
                                        drinkingNumber,
                                        smoking,
                                        blood,
                                        character,
                                        hobby,
                                        myStyle,
                                        dateStyle,
                                        introduce,
                                        textColor,
                                        family,
                                        route,
                                        phone,
                                        deleteImage,
                                        deleteMarryImage,
                                        imageUriArray,
                                        marryCertUriArray
                                    )
                                }
                            } else {
                                Utility.instance.hideSoftKeyboard(activity)
                                ll_frag_my_info_member_progress.visibility = View.VISIBLE
                                mPresenter.setMyInformationEdit(
                                    id,
                                    area01,
                                    area02,
                                    type,
                                    firstName,
                                    lastName,
                                    nameCheck,
                                    birth,
                                    siblingMale,
                                    siblingFemale,
                                    siblingNumber,
                                    children,
                                    hometown01,
                                    hometown02,
                                    job,
                                    income,
                                    fortune,
                                    education,
                                    car,
                                    religion,
                                    parents,
                                    marryPlan,
                                    divorce,
                                    divorceYear,
                                    height,
                                    weight,
                                    drinking,
                                    drinkingNumber,
                                    smoking,
                                    blood,
                                    character,
                                    hobby,
                                    myStyle,
                                    dateStyle,
                                    introduce,
                                    textColor,
                                    family,
                                    route,
                                    phone,
                                    deleteImage,
                                    deleteMarryImage,
                                    imageUriArray,
                                    marryCertUriArray
                                )
                            }
                        }

                        isModifyMode = true
                        setModifyMode()
                    }
                }
            })

        /*    이미지삭제    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun setEventBusListener() {
        EventBus.subjectMyInfo.subscribe {
            context?.let { it1 ->
                tv_frag_my_info_member_talk_id.text = it.mbNick
                tv_frag_my_info_member_area.text =
                    String.format(it1.resources.getString(R.string.arrow), it.mbAddr1, it.mbAddr2)
                if (it.mbChar == it1.resources.getString(R.string.information_member_marry)) {
                    setPress(AppKeyValue.instance.infoCheckMarry, 0)
                } else {
                    setPress(AppKeyValue.instance.infoCheckMarry, 1)
                }
                tv_frag_my_info_member_age.text =
                    String.format(it1.resources.getString(R.string.age_birth), it.mbAge, it.mbBirth)
                this.birth = it.mbBirth
                et_frag_my_info_member_phone.setText(it.mbHp)
            }
        }.addTo(disposeBag)

        EventBus.subjectPhoneAuth.subscribe {
            phoneAuth = true
            et_frag_my_info_member_phone.isEnabled = false
        }.addTo(disposeBag)
    }

    private fun setPress(setType: String, position: Int) {
        when (setType) {
            AppKeyValue.instance.infoCheckMarry -> {
                rb_frag_my_info_member_marry.isChecked = position == 0


            }

            AppKeyValue.instance.infoCheckParents -> {
                parentsArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckMarryPlan -> {
                marryPlanArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckDivorce -> {
                divorceArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckDrink -> {
                drinkArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true

                }
            }

            AppKeyValue.instance.infoCheckDrinkNumber -> {
                drinkNumberArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckSmoke -> {
                smokeArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckBlood -> {
                bloodArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

            AppKeyValue.instance.infoCheckRoute -> {
                routeArray?.let { it ->
                    it.map {
                        it.isChecked = false
                    }
                    it[position].isChecked = true
                }
            }

//            AppKeyValue.instance.infoSelectTextColor -> {
//                textColorArray?.let { it ->
//                    it.map {
//                        it.isSelected = false
//                    }
//                    it[position].isSelected = true
//                    val colorArray = arrayOf(
//                        R.color.color_profile_select01,
//                        R.color.color_profile_select02,
//                        R.color.color_profile_select03,
//                        R.color.color_profile_select04,
//                        R.color.color_profile_select05,
//                        R.color.color_profile_select06,
//                        R.color.color_profile_select07,
//                        R.color.color_profile_select08,
//                        R.color.color_profile_select09,
//                        R.color.color_profile_select10,
//                        R.color.color_profile_select11
//                    )
//
//                    textColor = position.toString()
//                }
//            }
        }
    }

    /*    키워드형식 레이아웃    */
    private fun keyWordSetLayout(
        context: Context,
        flowLayout: FlowLayout,
        array: Array<String>,
        arrayList: ArrayList<String>?
    ) {
        flowLayout.removeAllViews()
        for (i in array.indices) {
            val textLayoutParams =
                FlowLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            context.resources?.getDimension(R.dimen.default_margin_5dp)?.toInt()
                ?.let { textLayoutParams.horizontalSpacing = it }
            val textView = TextView(context)
            textView.apply {
                text = array[i]
                setBackgroundResource(R.drawable.bg_border_btn_warm_gray)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
                setTextColor(Color.argb(166, 65, 65, 65))
                context.resources?.getDimension(R.dimen.default_margin_10dp)?.toInt()
                    ?.let { setPadding(it, it / 2, it, it / 2) }
                gravity = Gravity.CENTER
                layoutParams = textLayoutParams
            }
            flowLayout.addView(textView)
            arrayList?.add(array[i])
        }
    }

    /*    형제/자매 선택 다이얼로그    */
    private fun siblingDialog(textView: TextView, sibling: Int) {
        val siblingDialog = activity?.let {
            val siblingArray: Array<String?> = when (sibling) {
                1 -> it.resources.getStringArray(R.array.member_sibling_male_array) as Array<String?>
                2 -> it.resources.getStringArray(R.array.member_sibling_female_array) as Array<String?>
                else -> it.resources.getStringArray(R.array.member_sibling_number_array) as Array<String?>
            }
            SelectorMemberDialog(
                it,
                AppKeyValue.instance.memberSelectThree,
                AppKeyValue.instance.memberTypeNotLimit,
                it.resources.getString(R.string.information_member_dlg_sibling),
                siblingArray,
                object : DialogSelect {
                    override fun onSelect(position: Int, o: Any) {
                        textView.text = siblingArray[position]
                        when (sibling) {
                            1 -> {
                                sibling01Click = true
                                siblingMalePosition = position
                            }

                            2 -> {
                                sibling02Click = true
                                siblingFemalePosition = position
                            }

                            3 -> {
                                siblingNumberPosition = position
                            }
                        }

                        val siblingMale = tv_frag_my_info_member_sibling_male.text
                        val siblingFemale = tv_frag_my_info_member_sibling_female.text
                        val siblingNumber = tv_frag_my_info_member_sibling_number.text
                        if (!TextUtils.isEmpty(siblingMale) && !TextUtils.isEmpty(siblingFemale) || !TextUtils.isEmpty(
                                siblingMale
                            ) && !TextUtils.isEmpty(siblingNumber) || !TextUtils.isEmpty(
                                siblingFemale
                            ) && !TextUtils.isEmpty(
                                siblingNumber
                            )
                        ) {
                            val siblingSum = siblingMalePosition.plus(siblingFemalePosition)
                            if ((gender == "M" && siblingMalePosition == 0) || (gender == "F" && siblingFemalePosition == 0) || siblingSum == 0 || siblingNumberPosition.plus(
                                    1
                                ) > siblingSum
                            ) {
                                Utility.instance.showAlert(
                                    it,
                                    it.getString(R.string.app_name),
                                    it.getString(R.string.msg_error_select),
                                    DialogInterface.OnClickListener { dialog, which ->
                                        textView.text = null
                                        when (sibling) {
                                            1 -> sibling01Click = false
                                            2 -> sibling02Click = false
                                        }
                                    })
                            }
                        }
                    }
                })
        }
        siblingDialog?.setCanceledOnTouchOutside(false)
        siblingDialog?.show()
    }

    private fun itemClickListener() = View.OnClickListener {
        context?.let { it1 ->
            if (isModifyMode) {
                val position = it.tag.toString().toInt()
                Utility.instance.showTwoButtonAlert(
                    it1,
                    it1.getString(R.string.app_name),
                    it1.getString(R.string.msg_app_select_delete),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            val size = mAdapter.dataImgNo.size
                            if (size > position) {
                                val mbImgNo = mAdapter.dataImgNo[position]
                                mAdapter.deleteArray?.add(mbImgNo)
                            }
                            mAdapter.removeItem(position)
                            imageUriArray?.let {
                                if (it.getOrNull(position) != null) {
                                    it.removeAt(position)
                                }
                            }
                            mAdapter.notifyDataSetChanged()
                        }
                    })
            }
        }
    }

    private fun setModifyMode() {
        binding.certifyVM?.isModifyMode?.value = isModifyMode

        if (isModifyMode) {

            rb_frag_my_info_member_marry.isEnabled = true
            rb_frag_my_info_member_remarry.isEnabled = true
            et_frag_my_info_member_first_name.isEnabled = true
            et_frag_my_info_member_height.isEnabled = true
            et_frag_my_info_member_weight.isEnabled = true
            et_frag_my_info_member_introduce.isEnabled = true
            et_frag_my_info_member_family.isEnabled = true

        }
    }

    /*    내정보    */
    override fun setMyInformationComplete(
        id: String?,
        profileState: String?,
        talkId: String?,
        area: String?,
        type: String?,
        firstName: String?,
        lastName: String?,
        nameCheck: String?,
        age: String?,
        birth: String?,
        siblingMale: String?,
        siblingFemale: String?,
        siblingNumber: String?,
        children: String?,
        hometown01: String?,
        hometown02: String?,
        job: String?,
        income: String?,
        fortune: String?,
        education: String?,
        car: String?,
        religion: String?,
        parents: String?,
        marryPlan: String?,
        divorce: String?,
        divorceYear: String?,
        height: String?,
        weight: String?,
        drinking: String?,
        drinkingNumber: String?,
        smoking: String?,
        blood: String?,
        character: String?,
        hobby: String?,
        myStyle: String?,
        dateStyle: String?,
        introduce: String?,
        textColor: String?,
        family: String?,
        route: String?,
        phone: String?,
        data: PictureData?,
        marryData: PictureMarryData?
    ) {
        context?.let {

            ll_frag_my_info_member_progress.visibility = View.GONE
            if (marryData?.mbImg!![0] != "") binding.certifyVM?.imgUri?.value =
                Uri.parse(marryData.mbImg!![0])
            binding.certifyVM?.pictureMarryData?.value = marryData

            tv_frag_my_info_member_talk_id.text = talkId
            val arrow = String.format(it.resources.getString(R.string.arrow), "", "")
            if (area != arrow) {
                tv_frag_my_info_member_area.text = area
            }
            this.birth = birth
            tv_frag_my_info_member_age.text =
                String.format(it.resources.getString(R.string.age_birth), age, birth)
            tv_frag_my_info_member_mark_content.text =
                String.format(
                    it.resources.getString(R.string.information_member_mark_content),
                    talkId
                )

            if (profileState == AppKeyValue.instance.keyYes) {
                btn_frag_my_info_member_complete?.apply {
                    setBackgroundResource(R.drawable.bg_pink_purple)
                    setTextColor(it.resources.getColor(R.color.color_white))
                    text = it.resources.getString(R.string.information_member_modify)
                }
                if (type == it.resources.getString(R.string.information_member_marry)) {
                    setPress(AppKeyValue.instance.infoCheckMarry, 0)

                } else {
                    setPress(AppKeyValue.instance.infoCheckMarry, 1)
                }

                et_frag_my_info_member_first_name.setText(firstName)
                /*et_frag_my_info_member_last_name.setText(lastName)*/

                if (nameCheck == AppKeyValue.instance.keyYes) {
                    cb_frag_my_info_member_first_name_check.isChecked = true
                }
                rl_frag_my_info_member_age.isClickable = false

                tv_frag_my_info_member_sibling_male.text = siblingMale
                tv_frag_my_info_member_sibling_female.text = siblingFemale
                tv_frag_my_info_member_sibling_number.text = siblingNumber

                val maleArray = it.resources.getStringArray(R.array.member_sibling_male_array)
                for (i in maleArray.indices) {
                    if (siblingMale == maleArray[i]) {
                        siblingMalePosition = i
                    }
                }

                val femaleArray = it.resources.getStringArray(R.array.member_sibling_female_array)
                for (i in maleArray.indices) {
                    if (siblingFemale == femaleArray[i]) {
                        siblingFemalePosition = i
                    }
                }

                val numberArray = it.resources.getStringArray(R.array.member_sibling_number_array)
                for (i in maleArray.indices) {
                    if (siblingNumber == numberArray[i]) {
                        siblingNumberPosition = i
                    }
                }

                val childrenArray =
                    children?.split(",".toRegex())?.dropLastWhile { it2 -> it2.isEmpty() }
                        ?.toTypedArray()
                val childrenMale = childrenArray?.get(0)
                val childrenFemale = childrenArray?.get(1)
                tv_frag_my_info_member_children_male.text =
                    String.format(it.resources.getString(R.string.children_male), childrenMale)
                tv_frag_my_info_member_children_female.text =
                    String.format(it.resources.getString(R.string.children_female), childrenFemale)
                tv_frag_my_info_member_hometown01.text = hometown01
                tv_frag_my_info_member_hometown02.text = hometown02
                tv_frag_my_info_member_job.text = job
                tv_frag_my_info_member_income.text = income
                tv_frag_my_info_member_fortune.text = fortune
                tv_frag_my_info_member_education.text = education
                tv_frag_my_info_member_car.text = car
                tv_frag_my_info_member_religion.text = religion
                tv_frag_my_info_member_parents.text = parents
                tv_frag_my_info_member_marry_plan.text = marryPlan
                tv_frag_my_info_member_divorce.text = divorce
                tv_frag_my_info_member_divorce_year.text = divorceYear
                tv_frag_my_info_member_drink.text = drinking
                drinkingNumber?.let { drinkNum ->
                    if (drinkNum == "") {
                        rl_frag_my_info_member_drink_number.visibility = View.GONE
                        tv_frag_my_info_member_drink_number_title.visibility = View.GONE
                    } else {
                        rl_frag_my_info_member_drink_number.visibility = View.VISIBLE
                        tv_frag_my_info_member_drink_number_title.visibility = View.VISIBLE
                        tv_frag_my_info_member_drink_number.text = drinkNum
                    }
                }

                tv_frag_my_info_member_smoke.text = smoking
                tv_frag_my_info_member_blood.text = blood
                tv_frag_my_info_member_hobby.text = hobby
                tv_frag_my_info_member_route.text = route

                et_frag_my_info_member_height.setText(height)
                et_frag_my_info_member_weight.setText(weight)


                val characterArray =
                    character?.split(",".toRegex())?.dropLastWhile { it1 -> it1.isEmpty() }
                        ?.toTypedArray()
                characterArray?.let { it1 ->
                    keyWordSetLayout(
                        it,
                        fl_frag_my_info_member_character,
                        it1,
                        this@MyProfileFragment.characterArray
                    )
                }


                val myStyleArray =
                    myStyle?.split(",".toRegex())?.dropLastWhile { it1 -> it1.isEmpty() }
                        ?.toTypedArray()
                myStyleArray?.let { it1 ->
                    keyWordSetLayout(
                        it,
                        fl_frag_my_info_member_mystyle,
                        it1,
                        this@MyProfileFragment.myStyleArray
                    )
                }

                val dateStyleArray =
                    dateStyle?.split(",".toRegex())?.dropLastWhile { it1 -> it1.isEmpty() }
                        ?.toTypedArray()
                dateStyleArray?.let { it1 ->
                    keyWordSetLayout(
                        it,
                        fl_frag_my_info_member_datestyle,
                        it1,
                        this@MyProfileFragment.dateStyleArray
                    )
                }

                et_frag_my_info_member_introduce.setText(introduce)
                if (!TextUtils.isEmpty(textColor)) {
                    textColor?.toInt()?.let { it1 ->
                        if (it1 < 11) {
                            setPress(AppKeyValue.instance.infoSelectTextColor, it1)
                        }
                    }
                }

                et_frag_my_info_member_family.setText(family)

                mAdapter.dataImg.clear()
                data?.mbImg?.let { it1 ->
                    for (i in 0..it1.size.minus(1)) {
                        if(!it1[i].contains("no_mb_img"))
                            mAdapter.addItem(it1[i])
                    }
                }

                mAdapter.dataImgNo.clear()
                data?.mbImgNo?.let { it1 ->
                    for (i in 0..it1.size.minus(1)) {
                        mAdapter.dataImgNo.add(it1[i])
                    }
                }

                mAdapter.notifyDataSetChanged()

                rl_frag_my_info_member_phone.visibility = View.GONE
                et_frag_my_info_member_phone.setText(phone)

                /*    기본정보 변경    */
                ll_frag_my_info_member_information.visibility = View.VISIBLE
//                disposeBag.add(RxView.clicks(ll_frag_my_info_member_information)
//                    .throttleFirst(1, TimeUnit.SECONDS)
//                    .subscribe { it1 ->
//                        val intent = Intent(it, MyInfoActivity::class.java)
//                        startActivity(intent)
//                    })


                isModifyMode = true
            }
            setModifyMode()
        }
    }

    /*    내정보 호출실패    */
    override fun setMyInformationFailed(error: String?) {
        ll_frag_my_info_member_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    내정보 수정    */
    override fun setMyInformationEditComplete(profileState: String?, first: Boolean) {
        ll_frag_my_info_member_progress.visibility = View.GONE
        btn_frag_my_info_member_complete?.apply {
            context?.let {
                setBackgroundResource(R.drawable.bg_pink_purple)
                setTextColor(it.resources.getColor(R.color.color_white))
                text = resources.getString(R.string.information_member_modify)
            }
        }

        setModifyMode()

        context?.let {
            val title = it.resources.getString(R.string.msg_information_member_modify_complete)

            Utility.instance.showTwoButtonAlert(
                it,
                it.resources.getString(R.string.app_name),
                title,
                it.resources.getString(R.string.dialog_enter),
                it.resources.getString(R.string.msg_information_member_home),
                DialogInterface.OnClickListener { dialog, which ->
                    Utility.instance.UserData().setUserProfile(profileState)
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        nsv_frag_my_info_member_view.scrollTo(0, 0)
                    } else {
                        //                        EventBus.sendEventMainFrag(AppKeyValue.instance.eventBusMainFrag)
                        startActivity(Intent(context, MainActivity::class.java))
                    }
                })
        }
    }


    /*    내정보 수정 호출실패    */
    override fun setMyInformationEditFailed(error: String?) {
        ll_frag_my_info_member_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
