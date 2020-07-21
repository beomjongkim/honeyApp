package com.dmonster.darling.honey.main.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.customview.ViewModelDialog
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.main.model.MainListModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.observers.DisposableObserver
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.util.GpsManager
import com.dmonster.darling.honey.util.retrofit.BaseItem
import kotlinx.android.synthetic.main.fragment_main.*


abstract class FilterVM(var id: String) : ViewModel() {
    //실질적인 검색이 이뤄지는 vm. 여러 조건들이 모여서 검색해야하므로.
    //areaVM같은 경우는 클릭 이벤트만 받아가고, 서버에 요청할 정보의 인자들만 전달해 주는 역할
    var searchMarry: String = ""
    var searchGender: String? = AppKeyValue.instance.keyYes
    var searchArea: String = ""
    var searchAge: String = ""
    var searchReligion: String = ""
    var searchIncome: String = ""
    var searchProperty: String = ""
    var searchBlood: String = ""
    var searchEducation: String = ""
    var searchDrink: String = ""
    var searchBaby: String = ""
    var searchCertyMarry: String = ""
    var searchCertyProfile: String = ""
    var searchLatitude: String = ""
    var searchLongitude: String = ""
    var sst: String = ""
    private var scrollOnce = true


    var dataList = MutableLiveData<ResultListItem<MainListData>>()
    var progressVisibility = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var dialog: ViewModelDialog? = null
    var isScroll = false
    var isRefreshing = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var isMarryFIltered = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isAreaFiltered = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isAgeGenderFIltered = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isEtcFiltered = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var filterMarryVM: FilterMarryVM? = null
    var filterAreaVM: FilterAreaVM? = null
    var filterAgeGenderVM: FilterAgeGenderVM? = null
    var filterEtcVM: FilterEtcVM? = null


    var scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    .plus(1)
            val itemTotalCount = recyclerView.adapter?.itemCount
            if (lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt()) && scrollOnce) {
                isScroll = true
                search(itemTotalCount.toString())
                scrollOnce = false
            }
        }
    }

    var refreshListener = SwipeRefreshLayout.OnRefreshListener {
        search()
//        initFilter()
        isRefreshing.value = false
    }

    init {
        initFilter()
    }

    fun initFilter() {
        searchGender = AppKeyValue.instance.keyYes
        searchArea = ""
        sst = ""
        searchAge = ""
        searchReligion = ""
        searchLatitude = ""
        searchLongitude = ""
        searchIncome = ""
        searchProperty = ""
        searchBlood = ""
        searchEducation = ""
        searchDrink = ""
        searchBaby = ""
        searchCertyMarry = ""
        searchCertyProfile = ""
        isScroll = false
        search()


        isMarryFIltered.value = false

        isAreaFiltered.value = false

        isAgeGenderFIltered.value = false

        isEtcFiltered.value = false
    }

    fun onClickFilterMarriage(view: View) {
        val context = view.context
        context.let {
            val dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes // 휴면여부 확인
            val profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes // 프로필작성 여부 확인.
            if (filterMarryVM == null) {
                filterMarryVM = object : FilterMarryVM() {
                    override fun onClickBack() {
                        dialog?.hide()
                    }

                    override fun afterClickInit() {
                        isMarryFIltered.value = false
                        searchMarry = ""
                        dialog?.hide()
                    }

                    override fun onClickSearch() {
                        isScroll = false
                        searchMarry = getMarry()
                        search()
                        dialog?.hide()
                        isMarryFIltered.value = getMarry() != ""
                    }
                }
            }
            when {
                dormantState -> dormantDialog()
                profileState -> {
                    val marryDialog =
                        ViewModelDialog(
                            it,
                            filterMarryVM!!,
                            BR.marryVM,
                            R.layout.layout_filter_marry
                        )
                    dialog = marryDialog
                    marryDialog.show()
//                    setDialogView(marryDialog, it, true)
                }
                else -> profileDialog()
            }
        }
    }

    fun onClickFilterArea(view: View) {
        val context = view.context
        context.let {
            val dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes // 휴면여부 확인
            val profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes // 프로필작성 여부 확인.
            if (filterAreaVM == null) {

                GpsManager.instance.permissionRequest(context)
                filterAreaVM = object : FilterAreaVM(
                    context.resources.getStringArray(R.array.main_area_array),
                    CustomAdapter(R.layout.layout_item_checkbox)
                ) {
                    override fun onClickBack() {
                        dialog?.hide()
                    }

                    override fun afterClickInit() {
                        isAreaFiltered.value = false
                        searchArea = ""
                        sst = ""
                        searchLatitude = ""
                        searchLongitude = ""
                        dialog?.hide()
                    }

                    override fun onClickSearch() {
                        searchArea = this.getSeletedArea()
                        searchLatitude = this.latitude
                        searchLongitude = this.longitude
                        if (searchLongitude.isNotBlank() && searchLatitude.isNotBlank()) {
                            sst = "addr"
                        } else {
                            sst = ""
                        }
                        isScroll = false
                        search()
                        dialog?.hide()
                        isAreaFiltered.value =
                            this.getSeletedArea().isNotBlank() || searchLatitude.isNotBlank() || searchLongitude.isNotBlank()

                    }
                }
            }
            when {
                dormantState -> dormantDialog()
                profileState -> {
                    val areaDialog = ViewModelDialog(
                        it,
                        filterAreaVM!!,
                        BR.areaVM,
                        R.layout.layout_filter_area
                    )
                    dialog = areaDialog
                    areaDialog.show()
//                    setDialogView(areaDialog, it,true)
                }
                else -> profileDialog()
            }
        }
    }

    fun onClickFilterAgeGender(view: View) {
        val context = view.context
        context.let {
            val dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes // 휴면여부 확인
            val profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes // 프로필작성 여부 확인.
            if (filterAgeGenderVM == null) {
                filterAgeGenderVM = object : FilterAgeGenderVM(
                    context.resources.getStringArray(R.array.main_age_array),
                    CustomAdapter(R.layout.layout_item_checkbox)
                ) {
                    override fun onClickBack() {
                        dialog?.hide()
                    }

                    override fun afterClickInit() {
                        isAgeGenderFIltered.value = false
                        searchGender = AppKeyValue.instance.keyYes
                        searchAge = ""
                        dialog?.hide()
                    }

                    override fun onClickSearch() {
                        searchGender = this.getSelectedGender()
                        searchAge = this.getSelectedAge()
                        isScroll = false
                        search()
                        dialog?.hide()
                        if (this.getSelectedAge() != "" || this.getSelectedGender() != "1") {
                            isAgeGenderFIltered.value = true
                        } else
                            isAgeGenderFIltered.value = false
                    }
                }
            }
            when {
                dormantState -> dormantDialog()
                profileState -> {
                    val ageGenderDialog =
                        ViewModelDialog(
                            it,
                            filterAgeGenderVM!!,
                            BR.ageGenderVM,
                            R.layout.layout_filter_age_gender
                        )
                    dialog = ageGenderDialog
                    ageGenderDialog.show()
//                    setDialogView(ageGenderDialog, it,true)
                }
                else -> profileDialog()
            }
        }
    }

    fun onClickFilterEtc(view: View) {
        val context = view.context
        context.let {
            val dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes // 휴면여부 확인
            val profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes // 프로필작성 여부 확인.
            if (filterEtcVM == null) {
                filterEtcVM = object : FilterEtcVM(view.context.resources) {
                    override fun onClickBack() {
                        dialog?.hide()
                    }

                    override fun onClickSearch() {
                        searchReligion = this.religionVM.choosenItem.value!!
                        searchIncome = this.incomeVM.choosenItem.value!!
                        searchProperty = this.fortuneVM.choosenItem.value!!
                        searchBlood = this.blTypeVM.choosenItem.value!!
                        searchEducation = this.educationVM.choosenItem.value!!
                        searchDrink = this.drinkVM.choosenItem.value!!
                        searchBaby = this.babyVM.choosenItem.value!!
                        if (this.marry_pic.value!!) {
                            searchCertyMarry = AppKeyValue.instance.keyYes
                        } else {
                            searchCertyMarry = ""
                        }
                        if (this.profile_pic.value!!) {
                            searchCertyProfile = AppKeyValue.instance.keyYes
                        } else {
                            searchCertyProfile = ""
                        }
                        search()
                        if (this.religionVM.choosenItem.value != "" || this.incomeVM.choosenItem.value != "" || this.fortuneVM.choosenItem.value != "" || this.blTypeVM.choosenItem.value != "" || this.educationVM.choosenItem.value != "" || this.drinkVM.choosenItem.value != "" || this.marry_pic.value!! || this.babyVM.choosenItem.value != "" || this.profile_pic.value!!) {
                            isEtcFiltered.value = true
                        } else
                            isEtcFiltered.value = false
                        dialog?.hide()
                    }

                    override fun afterClickInit() {
                        searchReligion = ""
                        searchIncome = ""
                        searchProperty = ""
                        searchBlood = ""
                        searchEducation = ""
                        searchDrink = ""
                        searchBaby = ""
                        searchCertyMarry = ""
                        searchCertyProfile = ""
                        isEtcFiltered.value = false
                        dialog?.hide()
                    }

                }
            }
            when {
                dormantState -> dormantDialog()
                profileState -> {
                    val etcDialog =
                        ViewModelDialog(
                            it,
                            filterEtcVM!!,
                            BR.etcVM,
                            R.layout.layout_filter_etc
                        )
                    dialog = etcDialog
                    etcDialog.show()
//                    setDialogView(etcDialog, it,true)
                }
                else -> profileDialog()
            }
        }
    }

    fun onClickProfileUp(view: View) {//프로필 올리기 했을 경우

        val dormantState =
            Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes // 휴면여부 확인
        val profileState =
            Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes // 프로필작성 여부 확인.


        val mModel = MainListModel()
        val subscriber = object : DisposableObserver<BaseItem>() {
            override fun onComplete() {
            }

            override fun onError(e: Throwable) {

                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if (it.isSuccess) {
                        initFilter()
                    } else {
                    }
                }
            }
        }

        when {
            dormantState -> dormantDialog()
            profileState -> {
                progressVisibility.value = true
                mModel.requestRefreshList(id, subscriber)

            }
            else -> profileDialog()
        }
    }

    abstract fun profileDialog()
    abstract fun dormantDialog()


    fun search(startCnt: String? = null) {
        progressVisibility.value = true
        val mModel = MainListModel()
        var mStartCnt = AppKeyValue.instance.listStartCnt
        startCnt?.let {
            mStartCnt = startCnt
        }
        val subscriber = object : DisposableObserver<ResultListItem<MainListData>>() {
            override fun onNext(t: ResultListItem<MainListData>) {
                dataList.value = t
            }

            override fun onComplete() {
                progressVisibility.value = false
                isRefreshing.value = false
                scrollOnce = true
            }

            override fun onError(e: Throwable) {
                Log.e("FilterVM", e.toString())
            }


        }
        mModel.requestFilterList(
            mStartCnt,
            AppKeyValue.instance.listLimitCnt,
            id,
            searchArea,
            searchLatitude,
            searchLongitude,
            sst,
            searchGender,
            searchAge,
            searchMarry,
            searchReligion,
            searchIncome,
            searchProperty,
            searchBlood,
            searchEducation,
            searchDrink,
            searchBaby,
            searchCertyProfile,
            searchCertyMarry,
            subscriber
        )
    }
}