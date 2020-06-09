package com.dmonster.darling.honey.block.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.block.data.BlockData

interface BlockContract {

    interface View: BaseView {
        fun setBlockList(isScroll: Boolean, data: List<BlockData>)    // 차단회원목록

        fun setBlockListFailed(error: String?)    // 차단회원목록 호출실패

        fun setBlockClearComplete(message: String?)    // 차단회원 차단해제

        fun setBlockClearFailed(error: String?)    // 차단회원 차단해제 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getBlockList(isScroll: Boolean, id: String?, limitCnt: String?)    // 차단회원목록 불러오기

        fun setBlockClear(id: String?, mbNo: String?, type: String?)    // 차단회원 차단해제
    }

}
