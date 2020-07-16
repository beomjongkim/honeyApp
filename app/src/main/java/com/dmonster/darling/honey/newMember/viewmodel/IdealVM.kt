package com.dmonster.darling.honey.newMember.viewmodel

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.common.viewmodel.CustomSpinnerVM
import com.dmonster.darling.honey.common.viewmodel.SpinnerRangeVM
import com.dmonster.darling.honey.newMember.view.NewMemeberSearchFragment
import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.myinformation.model.IdealTypeModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class IdealVM(
    var fragmentManager: FragmentManager,
    var id: String?,
    var ageVM: CustomSpinnerVM,
    var areaVM: CustomSpinnerVM,
    var incomeVM: CustomSpinnerVM,
    var eduVM: CustomSpinnerVM,
    var heigthVM: SpinnerRangeVM,
    var styleVM: CustomSpinnerVM,
    var religionVM: CustomSpinnerVM,
    var bloodVM: CustomSpinnerVM
) : ViewModel() {

    var isTabChanged = MutableLiveData<Boolean>().also {
        it.value = false
    }


    init {
        val newFragment = NewMemeberSearchFragment()
        val transaction = fragmentManager.beginTransaction()
        newFragment.let { transaction.replace(R.id.fl_frag_ideal, it) }
        transaction.commit()
        getIdeal()
    }

    fun onClickSearch() {
        isTabChanged.value = false
    }

    fun onClickOption() {
        isTabChanged.value = true
    }

    fun getIdeal() {

        val mModel = IdealTypeModel()
        val subscription = CompositeDisposable()

        val subscriber = object : DisposableObserver<ResultItem<MyInformationData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
//                mView.setIdealTypeFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInformationData>) {
                item.let { it ->
                    if (it.isSuccess) {
                        it.item?.let {
                            ageVM.text.value = it.mbHopeBirth
                            areaVM.text.value = it.mbHopeAddr
                            val tallArr = it.mbHopeTall?.split("~")
                            tallArr?.let { it1 ->
                                if (it1.size > 1) {
                                    heigthVM.textMin.value = tallArr[0]
                                    heigthVM.textMax.value = tallArr[1]
                                }
                            }
//                            val sucWeight = it.mbHopeWeight
                            styleVM.text.value = it.mbHopeStyle
                            incomeVM.text.value = it.mbHopeIncome
                            eduVM.text.value = it.mbHopeLevel
                            religionVM.text.value = it.mbHopeReligion
                            bloodVM.text.value = it.mbHopeBlood

//                            mView.setIdealTypeComplete(sucAge, sucArea, sucHeight, sucWeight, sucStyle, sucIncome, sucEducation, sucReligion, sucBlood)
                        }
                    } else {
//                        mView.setIdealTypeFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyIdealType(id, Utility.instance.UserData().getUserMb(), subscriber)
        subscription.add(subscriber)
    }

    fun initIdeal(view: View) {
        ageVM.text.value = ""
        ageVM.text.value = ""
        areaVM.text.value = ""
        incomeVM.text.value = ""
        eduVM.text.value = ""
        heigthVM.textMax.value = ""
        heigthVM.textMin.value = ""
        styleVM.text.value = ""
        religionVM.text.value = ""
        bloodVM.text.value = ""
    }

    fun saveIdeal(view: View) {
        val mModel = IdealTypeModel()
        val subscription = CompositeDisposable()

        val subscriber = object : DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
//                mView.setIdealTypeEditFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if (it.isSuccess) {
                        Utility.instance.showToast(view.context, "수정 되었습니다.")
                    } else {
                        Utility.instance.showToast(view.context, "인터넷 환경을 점검해주세요.")
//                        mView.setIdealTypeEditFailed(it.message)
                    }
                }
            }
        }
        var heigth = ""
        if (heigthVM.textMin.value != "" && heigthVM.textMax.value != "") {
            heigth = listOf(heigthVM.textMin.value, " ~ ", heigthVM.textMax.value).joinToString("")
        }
        mModel.requestIdealTypeEdit(
            id,
            ageVM.text.value,
            areaVM.text.value,
            heigth,
            "",
            styleVM.text.value,
            incomeVM.text.value,
            eduVM.text.value,
            religionVM.text.value,
            bloodVM.text.value,
            subscriber
        )
        subscription.add(subscriber)
    }


}