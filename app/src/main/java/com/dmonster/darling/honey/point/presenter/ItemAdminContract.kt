package com.dmonster.darling.honey.point.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.point.data.ItemData
import com.dmonster.darling.honey.point.data.ItemUseData

interface ItemAdminContract {

    interface View: BaseView {
        fun setItemListComplete(data: ArrayList<ItemData>)    // 보유아이템 목록

        fun setItemListFailed(error: String?)    // 보유아이템 목록 호출실패

        fun setItemUseListComplete(data: ArrayList<ItemUseData>)    // 아이템 사용내역 목록

        fun setItemUseListFailed(error: String?)    // 아이템 사용내역 목록 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getItemList(id: String?)    // 보유 아이템 목록 불러오기

        fun getItemUseList(id: String?)    // 아이템 사용내역 불러오기
    }

}
