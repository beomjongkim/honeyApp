package com.dmonster.darling.honey.myinformation.model

import android.net.Uri
import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MyInformationModel {

    /*    내정보    */
    fun requestMyInformation(id: String?, mbNo: String?, subscriber: DisposableObserver<ResultItem<MyInformationData>>) {
        RetrofitProtocol().retrofit.requestMyInformation(ServerApi.instance.myInformationMethod, id, mbNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    회원정보 입력/변경    */
    fun requestMyInformationEdit(id: String?, area01: String?, area02: String?, type: String?, firstName: String?, lastName: String?, nameCheck: String?, age: String?,
                                 siblingMale: String?, siblingFemale: String?, siblingNumber: String?, children: String?, hometown01: String?, hometown02: String?, job: String?,
                                 income: String?, fortune: String?, education: String?, car: String?, religion: String?, parents: String?, marryPlan: String?, divorce: String?,
                                 divorceYear: String?, height: String?, weight: String?, drinking: String?, drinkingNumber: String?, smoking: String?, blood: String?, character: String?,
                                 hobby: String?, myStyle: String?, dateStyle: String?, introduce: String?, textColor: String?, family: String?, route: String?, phone: String?, deleteImage: String?,deleteMarryImage: String?,
                                 profileImage: ArrayList<Uri>?, marryCertImage: ArrayList<Uri>?, subscriber: DisposableObserver<ResultItem<MyInformationData>>) {
        val requestMethod = RequestBody.create(MediaType.parse("text/plain"), ServerApi.instance.myInformationEditMethod)
        val requestId = id?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestArea01 = area01?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestArea02 = area02?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestType = type?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestFirstName = firstName?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestLastName = lastName?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestNameCheck = nameCheck?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestAge = age?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestSiblingMale = siblingMale?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestSiblingFemale = siblingFemale?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestSiblingNumber = siblingNumber?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestChildren = children?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestHometown01 = hometown01?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestHometown02 = hometown02?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestJob = job?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestIncome = income?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestFortune = fortune?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestEducation = education?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestCar = car?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestReligion = religion?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestParents = parents?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestMarryPlan = marryPlan?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDivorce = divorce?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDivorceYear = divorceYear?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestHeight = height?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestWeight = weight?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDrinking = drinking?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDrinkingNumber = drinkingNumber?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestSmoking = smoking?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestBlood = blood?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestCharacter = character?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestHobby = hobby?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestMyStyle = myStyle?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDateStyle = dateStyle?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestIntroduce = introduce?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestTextColor = textColor?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestFamily = family?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestRoute = route?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestPhone = phone?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDeleteImage = deleteImage?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestDeleteMarryImage = deleteMarryImage?.let { RequestBody.create(MediaType.parse("text/plain"), it) }

        RetrofitProtocol().retrofit.requestMyInformationEdit(requestMethod, requestId, requestArea01, requestArea02, requestType, requestFirstName, requestLastName, requestNameCheck,
                requestAge, requestSiblingMale, requestSiblingFemale, requestSiblingNumber, requestChildren, requestHometown01, requestHometown02, requestJob, requestIncome, requestFortune, requestEducation,
                requestCar, requestReligion, requestParents, requestMarryPlan, requestDivorce, requestDivorceYear, requestHeight, requestWeight, requestDrinking, requestDrinkingNumber, requestSmoking, requestBlood,
                requestCharacter, requestHobby, requestMyStyle, requestDateStyle, requestIntroduce, requestTextColor, requestFamily, requestRoute, requestPhone, requestDeleteImage, requestDeleteMarryImage, createParamFiles(profileImage), createParamCertFiles(marryCertImage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    private fun createParamFiles(profileImage: ArrayList<Uri>?): Array<MultipartBody.Part?> {
        var requestProfileImage: Array<MultipartBody.Part?>
        profileImage?.let {
            requestProfileImage = arrayOfNulls<MultipartBody.Part>(it.size)
            for(i in 0 until it.size) {
                it[i].path?.let { it1 ->
                    val file = File(it1)
                    val fileName = file.name
                    val requestBody = RequestBody.create(MediaType.parse("mb_img[]"), file)
                    requestProfileImage[i] = MultipartBody.Part.createFormData("mb_img[]", fileName, requestBody)
                }
            }
            return requestProfileImage
        }

        return emptyArray()
    }

    private fun createParamCertFiles(marryCertImage: ArrayList<Uri>?): Array<MultipartBody.Part?> {
        var requestProfileImage: Array<MultipartBody.Part?>
        marryCertImage?.let {
            requestProfileImage = arrayOfNulls<MultipartBody.Part>(it.size)
            for(i in 0 until it.size) {
                it[i].path?.let { it1 ->
                    val file = File(it1)
                    val fileName = file.name
                    val requestBody = RequestBody.create(MediaType.parse("mb_marry_img[]"), file)
                    requestProfileImage[i] = MultipartBody.Part.createFormData("mb_marry_img[]", fileName, requestBody)
                }
            }
            return requestProfileImage
        }

        return emptyArray()
    }

}
