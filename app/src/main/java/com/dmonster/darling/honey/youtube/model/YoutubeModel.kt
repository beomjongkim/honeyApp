package com.dmonster.darling.honey.youtube.model

import android.net.Uri
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.dmonster.darling.honey.youtube.data.YoutubeData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class YoutubeModel {
    /*    즐겨찾기    */
    fun getYoutubePlayKey(subscriber: DisposableObserver<ResultItem<YoutubeData>>) {
        RetrofitProtocol().retrofit.readYoutube(ServerApi.instance.readYoutube)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
