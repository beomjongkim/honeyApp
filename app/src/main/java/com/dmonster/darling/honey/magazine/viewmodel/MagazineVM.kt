package com.dmonster.darling.honey.magazine.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM
import com.dmonster.darling.honey.magazine.data.MagazineData
import com.dmonster.darling.honey.magazine.model.MagazineModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MagazineVM(var recyclerAdapter: CustomAdapter) : ViewModel() {

    init {
        loadData()
    }

    private fun loadData() {
        recyclerAdapter.dataList.clear()
        var model = MagazineModel()
        val subscription = CompositeDisposable()
        var subscriber = object : DisposableObserver<ResultListItem<MagazineData>>() {
            override fun onComplete() {
            }

            override fun onNext(t: ResultListItem<MagazineData>) {
                if (t.isSuccess) {
                    t.items?.let {
                        for (data in it) {
                            recyclerAdapter.dataList.add(
                                RecyclerItemData(
                                    0,
                                    MagazineListItemVM(
                                        data.title,
                                        data.type,
                                        data.link,
                                        data.thumnail
                                    ),
                                    BR.magazineItemVM
                                )
                            )
                        }
                        recyclerAdapter.dataList.add(RecyclerItemData(0,MagazineListItemVM("","","",""), BR.magazineItemVM))
                        recyclerAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onError(e: Throwable) {
            }

        }
        model.readMagazine(subscriber)
        subscription.add(subscriber)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        loadData()
    }
}