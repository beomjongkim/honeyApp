package com.dmonster.darling.honey.point.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.point.data.MarketListData

interface MarketListContract {

    interface View: BaseView {
        fun setCoinComplete(coin: String?)    // 현재 보유코인

        fun setCoinFailed(error: String?)    // 현재 보유코인 호출실패

        fun setMarketListComplete(isScroll: Boolean, data: ArrayList<MarketListData>)    // 마켓 상품목록

        fun setMarketListFailed(error: String?)    // 마켓 상품목록 호출실패

        fun setMarketItemBuyComplete()    // 마켓 상품구매

        fun setMarketItemBuyFailed(error: String?)    // 마켓 상품구매 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getCoin(id: String?)    // 현재 보유코인 불러오기

        fun getMarketList(isScroll: Boolean, id: String?)    // 마켓 상품목록 불러오기

        fun setMarketItemBuy(id: String?, productId: String?, count: String?, type: String?, otherId: String?)    // 마켓 상품구매
    }

}
