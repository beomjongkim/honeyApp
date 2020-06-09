package com.dmonster.darling.honey.profile.presenter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myinformation.data.PictureMarryData
import com.dmonster.darling.honey.profile.data.ProfileDetailData
import com.dmonster.darling.honey.profile.data.ProfilePictureData
import com.dmonster.darling.honey.profile.model.GoodModel
import com.dmonster.darling.honey.profile.model.ProfileModel
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ProfilePresenter: ProfileContract.Presenter {

    private lateinit var mModel: ProfileModel
    private lateinit var mView: ProfileContract.View
    private lateinit var subscription: CompositeDisposable

    var sucId  : String? = null
    var sucGender : String? = null
    var sucTalkId : String? = null
    var sucArea  : String? = null
    var sucGenderStr  : String? = null
    var sucType : String? = null
    var sucName  : String? = null
    var sucAge  : String? = null
    var sucBirth : String? = null
    var sucCar  : String? = null
    var sucMyStyle  : String? = null
    var sucIntroduce  : String? = null
    var sucFamily : String? = null
    var sucProfilePicture  : ProfilePictureData? = null
    var sucMarryPicture  : PictureMarryData? = null
    var sucItemUse : String? = null
    var sucProfilePicCnt  : String? = null

    var sucIncome: String ? = null
    var sucSibling : String ? = null
    var sucHometown: String ? = null
    var sucJob : String ? = null
    var sucFortune : String ? = null
    var sucEducation: String ? = null
    var sucReligion: String ? = null
    var sucParents : String ? = null
    var sucMarryPlan : String ? = null
    var sucDivorce : String ? = null
    var sucDivorceYear : String ? = null
    var sucChildren: String ? = null
    var sucHeight: String ? = null
    var sucWeight : String ? = null
    var sucDrinking: String ? = null
    var sucSmoking: String ? = null
    var sucBlood : String ? = null
    var sucCharacter : String ? = null
    var sucHobby : String ? = null
    var sucDateStyle : String ? = null
      var sucRoute : String ? = null

    override fun attachView(view: ProfileContract.View) {
        this.mView = view
        this.mModel = ProfileModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    프로필 열람전 정보    */
    override fun getProfileSample(context: Context, id: String?, mbNo: String?) {
        val subscriber = object: DisposableObserver<ResultItem<ProfileDetailData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setProfileSampleFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<ProfileDetailData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                             sucId = it.mbId
                             sucGender = it.mbSex
                             sucTalkId = it.mbNick
                             sucArea = String.format(context.resources.getString(R.string.arrow), it.mbAddr1, it.mbAddr2)
                             sucGenderStr = it.mbSexKor
                             sucType = it.mbChar
                             sucName = it.mbName
                             sucAge = it.mbAge
                             sucBirth = it.mbBirth
                             sucCar = it.mbCar
                             sucMyStyle = it.mbStyle
                             sucIntroduce = it.mbProfile
                             sucFamily = it.mbFamily
                             sucProfilePicture = it.mbImg
                             sucMarryPicture = it.mbMarryImg
                             sucItemUse = it.itemUseChkProfile
                             sucProfilePicCnt = it.mbImgCnt


                             sucIncome = it.mbIncome
                             sucSibling = it.mbBrother
                             sucHometown = String.format(context.resources.getString(R.string.arrow), it.mbAddr3, it.mbAddr4)
                             sucJob = it.mbJobs
                             sucFortune = it.mbProperty
                             sucEducation = it.mbEduLevel
                             sucReligion = it.mbReligion
                             sucParents = it.mbParents
                             sucMarryPlan = it.mbPlanMarry
                             sucDivorce = it.mbDivorce
                             sucDivorceYear = it.mbDivorceAge
                             sucChildren = it.mbBaby
                             sucHeight = it.mbTall
                             sucWeight = it.mbWeight
                             sucDrinking = if(it.mbDrink != context.resources.getString(R.string.key_information_drinking_not)) {
                                String.format(context.resources.getString(R.string.arrow), it.mbDrink, it.mbDrinkNum)
                            }
                            else {
                                it.mbDrink
                            }
                             sucSmoking = it.mbCigarette
                             sucBlood = it.mbBlood
                             sucCharacter = it.mbCharacter
                             sucHobby = it.mbHobby

                             sucDateStyle = it.mbStyledate
                             sucRoute = it.mbJoinroute


                            mView.setProfileSampleComplete(sucId, sucGender, sucTalkId, sucArea, sucGenderStr, sucType, sucName,
                                    sucAge, sucBirth, sucCar, sucMyStyle, sucIntroduce, sucFamily, sucProfilePicture, sucMarryPicture,sucProfilePicCnt,sucItemUse)
                        }
                    }
                    else {
                        mView.setProfileSampleFailed(it.message)
                    }
                }
            }
        }
        mModel.requestProfile(id, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    프로필 열람후 모든정보    */
    override fun getProfile(context: Context, id: String?, mbNo: String?) {
        mView.setProfileComplete(sucTalkId, sucArea, sucGenderStr, sucType, sucName, sucAge, sucBirth, sucIncome, sucSibling, sucHometown, sucJob,
            sucFortune, sucEducation, sucCar, sucReligion, sucParents, sucMarryPlan, sucDivorce, sucDivorceYear, sucChildren, sucHeight, sucWeight,
            sucDrinking, sucSmoking, sucBlood, sucCharacter, sucHobby, sucMyStyle, sucDateStyle, sucIntroduce, sucFamily, sucProfilePicture,sucMarryPicture, sucProfilePicCnt, sucRoute)
//        val subscriber = object: DisposableObserver<ResultItem<ProfileDetailData>>() {
//            override fun onComplete() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                mView.setProfileFailed(e.message)
//                e.printStackTrace()
//            }
//
//            override fun onNext(item: ResultItem<ProfileDetailData>) {
//                item.let { it ->
//                    if(it.isSuccess) {
//                        it.item?.let {
//                            val sucTalkId = it.mbNick
//                            val sucArea = String.format(context.resources.getString(R.string.arrow), it.mbAddr1, it.mbAddr2)
//                            val sucGenderStr = it.mbSexKor
//                            val sucType = it.mbChar
//                            val sucName = it.mbName
//                            val sucAge = it.mbAge
//                            val sucBirth = it.mbBirth
//                            val sucIncome = it.mbIncome
//                            val sucSibling = it.mbBrother
//                            val sucHometown = String.format(context.resources.getString(R.string.arrow), it.mbAddr3, it.mbAddr4)
//                            val sucJob = it.mbJobs
//                            val sucFortune = it.mbProperty
//                            val sucEducation = it.mbEduLevel
//                            val sucCar = it.mbCar
//                            val sucReligion = it.mbReligion
//                            val sucParents = it.mbParents
//                            val sucMarryPlan = it.mbPlanMarry
//                            val sucDivorce = it.mbDivorce
//                            val sucDivorceYear = it.mbDivorceAge
//                            val sucChildren = it.mbBaby
//                            val sucHeight = it.mbTall
//                            val sucWeight = it.mbWeight
//                            val sucDrinking = if(it.mbDrink != context.resources.getString(R.string.key_information_drinking_not)) {
//                                String.format(context.resources.getString(R.string.arrow), it.mbDrink, it.mbDrinkNum)
//                            }
//                            else {
//                                it.mbDrink
//                            }
//                            val sucSmoking = it.mbCigarette
//                            val sucBlood = it.mbBlood
//                            val sucCharacter = it.mbCharacter
//                            val sucHobby = it.mbHobby
//                            val sucMyStyle = it.mbStyle
//                            val sucDateStyle = it.mbStyledate
//                            val sucIntroduce = it.mbProfile
//                            val sucFamily = it.mbFamily
//                            val sucProfilePicture = it.mbImg
//                            val sucMarryPicture = it.mbMarryImg
//                            val sucProfilePicCnt = it.mbImgCnt
//                            val sucRoute = it.mbJoinroute
//
//                            mView.setProfileComplete(sucTalkId, sucArea, sucGenderStr, sucType, sucName, sucAge, sucBirth, sucIncome, sucSibling, sucHometown, sucJob,
//                                    sucFortune, sucEducation, sucCar, sucReligion, sucParents, sucMarryPlan, sucDivorce, sucDivorceYear, sucChildren, sucHeight, sucWeight,
//                                    sucDrinking, sucSmoking, sucBlood, sucCharacter, sucHobby, sucMyStyle, sucDateStyle, sucIntroduce, sucFamily, sucProfilePicture,sucMarryPicture, sucProfilePicCnt, sucRoute)
//                        }
//                    }
//                    else {
//                        mView.setProfileFailed(it.message)
//                    }
//                }
//            }
//        }
//        mModel.requestProfile(id, mbNo, subscriber)
//        subscription.add(subscriber)
    }

    /*    채팅방여부 확인    */
    override fun getTalkCheck(id: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<ArrayList<String>>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<ArrayList<String>>) {
                item.let { it ->
                    it.item?.let {
                        mView.setTalkCheck(it[0], it[1])
                    }
                }
            }
        }
        mModel.requestTalkCheck(id, otherId, subscriber)
        subscription.add(subscriber)
    }

    /*    톡하기    */
    override fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?) {
        val subscriber = object: DisposableObserver<ResultItem<TalkData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<TalkData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setTalkComplete()
                    }
                    else {
                        mView.setTalkFailed(it.message)
                    }
                }
            }
        }
        mModel.requestTalk(id, otherId, message, talkImage, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 보유 확인    */
    override fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.setItemUseComplete(itemId, it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 사용    */
    override fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if(it.isSuccess) {
                        when(itemId) {
                            AppKeyValue.instance.itemIdProfile -> getProfile(context, id, mbNo)
                            AppKeyValue.instance.itemIdTalk -> getTalk(id, otherId, "", null)
                        }
                    }
                    else {
                        mView.setItemUseFailed(it.message)
                    }
                }
            }
        }
        mModel.requestItemUse(id, itemId, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    차단하기    */
    override fun setBlock(id: String?, mbNo: String?, type: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setBlockFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setBlockComplete(it.message)
                    }
                    else {
                        mView.setBlockFailed(it.message)
                    }
                }
            }
        }
        mModel.requestBlock(id, mbNo, type, subscriber)
        subscription.add(subscriber)
    }

}
