package com.dmonster.darling.honey.item.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.item.data.ItemUseData

interface ItemUseContract {

    interface View: BaseView {
        fun setItemUseListComplete(isScroll: Boolean, data: ArrayList<ItemUseData>)    // 아이템 사용내역

        fun setItemUseListFailed(error: String?)    // 아이템 사용내역 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getItemUseList(isScroll: Boolean, id: String?)    // 아이템 사용내역 불러오기
    }

}
