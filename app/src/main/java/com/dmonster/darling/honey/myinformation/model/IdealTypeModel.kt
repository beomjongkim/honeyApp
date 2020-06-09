package com.dmonster.darling.honey.myinformation.model

import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class IdealTypeModel {

    /*    내 이상형정보    */
    fun requestMyIdealType(id: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<MyInformationData>>) {
        RetrofitProtocol().retrofit.requestMyInformation(ServerApi.instance.myInformationMethod, id, mbNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    이상형 선택    */
    fun requestIdealTypeEdit(id: String?, age: String?, area: String?, height: String?, weight: String?, style: String?, income: String?,
                             education: String?, religion: String?, blood: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestSelectIdealType(ServerApi.instance.selectIdealTypeMethod, id, age, area, height, weight, style, income, education, religion, blood)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
