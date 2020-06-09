package com.dmonster.darling.honey.block.model

import com.dmonster.darling.honey.block.data.BlockData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class BlockModel {

    /*    차단회원 목록    */
    fun requestBlockList(id: String?, limitCnt: String?, subscriber: DisposableObserver<ResultListItem<BlockData>>) {
        RetrofitProtocol().retrofit.requestBlockList(ServerApi.instance.blockListMethod, id, limitCnt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    차단해제    */
    fun requestBlockClear(id: String?, mbNo: String?, type: String?, subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.requestBlockClear(ServerApi.instance.blockClearMethod, id, mbNo, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

}
