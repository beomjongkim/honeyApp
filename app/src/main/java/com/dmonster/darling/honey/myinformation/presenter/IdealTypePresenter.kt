package com.dmonster.darling.honey.myinformation.presenter

import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.myinformation.model.IdealTypeModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class IdealTypePresenter: IdealTypeContract.Presenter {

    private lateinit var mModel: IdealTypeModel
    private lateinit var mView: IdealTypeContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: IdealTypeContract.View) {
        this.mView = view
        this.mModel = IdealTypeModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    이상형 정보    */
    override fun getIdealType(id: String?, mbNo: String?) {
        val subscriber = object: DisposableObserver<ResultItem<MyInformationData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setIdealTypeFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInformationData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            val sucAge = it.mbHopeBirth
                            val sucArea = it.mbHopeAddr
                            val sucHeight = it.mbHopeTall
                            val sucWeight = it.mbHopeWeight
                            val sucStyle = it.mbHopeStyle
                            val sucIncome = it.mbHopeIncome
                            val sucEducation = it.mbHopeLevel
                            val sucReligion = it.mbHopeReligion
                            val sucBlood = it.mbHopeBlood

                            mView.setIdealTypeComplete(sucAge, sucArea, sucHeight, sucWeight, sucStyle, sucIncome, sucEducation, sucReligion, sucBlood)
                        }
                    }
                    else {
                        mView.setIdealTypeFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyIdealType(id, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    이상형 수정    */
    override fun setSelectIdealType(id: String?, age: String?, area: String?, height: String?, weight: String?, style: String?,
                                    income: String?, education: String?, religion: String?, blood: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setIdealTypeEditFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setIdealTypeEditComplete()
                    }
                    else {
                        mView.setIdealTypeEditFailed(it.message)
                    }
                }
            }
        }
        mModel.requestIdealTypeEdit(id, age, area, height, weight, style, income, education, religion, blood, subscriber)
        subscription.add(subscriber)
    }

}
