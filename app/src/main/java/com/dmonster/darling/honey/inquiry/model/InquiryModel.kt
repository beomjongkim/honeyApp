package com.dmonster.darling.honey.inquiry.model

import android.net.Uri
import com.dmonster.darling.honey.inquiry.data.InquiryData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class InquiryModel {

    /*    문의하기    */
    fun requestInquire(id: String?, type: String?, image: Uri?, phone: String?, content: String?, subscriber: DisposableObserver<ResultItem<BaseItem>>) {
        val requestMethod = RequestBody.create(MediaType.parse("text/plain"), ServerApi.instance.inquiryMethod)
        val requestId = id?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestType = type?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        var requestImage: MultipartBody.Part? = null
        image?.path?.let {
            val file = File(it)
            val fileName = file.name
            requestImage = MultipartBody.Part.createFormData("wr_file", fileName, RequestBody.create(MediaType.parse("multipart/form-data"), file))
        }
        val requestPhone = phone?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestContent = content?.let{ RequestBody.create(MediaType.parse("text/plain"), it) }

        RetrofitProtocol().retrofit.requestInquiry(requestMethod, requestId, requestType, requestPhone, requestContent, requestImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    문의내역 목록    */
    fun requestInquiryList(id: String?, subscriber: DisposableObserver<ResultListItem<InquiryData>>) {
        RetrofitProtocol().retrofit.requestBreakdown(ServerApi.instance.breakdownMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
