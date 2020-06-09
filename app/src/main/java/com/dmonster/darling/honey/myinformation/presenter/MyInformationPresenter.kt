package com.dmonster.darling.honey.myinformation.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.myinformation.model.MyInformationModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MyInformationPresenter: MyInformationContract.Presenter {

    private lateinit var mModel: MyInformationModel
    private lateinit var mView: MyInformationContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: MyInformationContract.View) {
        this.mView = view
        this.mModel = MyInformationModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    내정보    */
    override fun getMyInformation(context: Context, id: String?, mbNo: String?) {
        val subscriber = object: DisposableObserver<ResultItem<MyInformationData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMyInformationFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInformationData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            val sucId = it.mbId
                            val sucTalkId = it.mbNick
                            val sucProfileState = it.mbProfileState
                            val sucArea = String.format(context.resources.getString(R.string.arrow), it.mbAddr1, it.mbAddr2)
                            val sucType = it.mbChar
                            val sucFirstName = it.mbName
                            val sucLastName = it.mbNameLast
                            val sucNameCheck = it.mbNameChk
                            val sucAge = it.mbAge
                            val sucBirth = it.mbBirth
                            val sucSiblingMale = it.mbBrotherM
                            val sucSiblingFemale = it.mbBrotherF
                            val sucSiblingNumber = it.mbBrotherN
                            val sucChildren = it.mbBaby
                            val sucHometown01 = it.mbAddr3
                            val sucHometown02 = it.mbAddr4
                            val sucJob = it.mbJobs
                            val sucIncome = it.mbIncome
                            val sucFortune = it.mbProperty
                            val sucEducation = it.mbEduLevel
                            val sucCar = it.mbCar
                            val sucReligion = it.mbReligion
                            val sucParents = it.mbParents
                            val sucMarryPlan = it.mbPlanMarry
                            val sucDivorce = it.mbDivorce
                            val sucDivorceYear = it.mbDivorceAge
                            val sucHeight = it.mbTall
                            val sucWeight = it.mbWeight
                            val sucDrinking = it.mbDrink
                            val sucDrinkingNumber = it.mbDrinkNum
                            val sucSmoking = it.mbCigarette
                            val sucBlood = it.mbBlood
                            val sucCharacter = it.mbCharacter
                            val sucHobby = it.mbHobby
                            val sucMyStyle = it.mbStyle
                            val sucDateStyle = it.mbStyledate
                            val sucIntroduce = it.mbProfile
                            val sucTextColor = it.mbProfileColor
                            val sucFamily = it.mbFamily
                            val sucRoute = it.mbJoinRoute
                            val sucPhone = it.mbHp
                            val sucProfilePicture = it.mbImg
                            val sucMarryPicture =it.mbMarryImg

                            mView.setMyInformationComplete(sucId, sucProfileState, sucTalkId, sucArea, sucType, sucFirstName, sucLastName, sucNameCheck, sucAge, sucBirth,
                                    sucSiblingMale, sucSiblingFemale, sucSiblingNumber, sucChildren, sucHometown01, sucHometown02, sucJob, sucIncome, sucFortune, sucEducation,
                                    sucCar, sucReligion, sucParents, sucMarryPlan, sucDivorce, sucDivorceYear, sucHeight, sucWeight, sucDrinking, sucDrinkingNumber, sucSmoking,
                                    sucBlood, sucCharacter, sucHobby, sucMyStyle, sucDateStyle, sucIntroduce, sucTextColor, sucFamily, sucRoute, sucPhone, sucProfilePicture,sucMarryPicture)
                        }
                    }
                    else {
                        mView.setMyInformationFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyInformation(id, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    내정보 수정    */
    override fun setMyInformationEdit(id: String?, area01: String?, area02: String?, type: String?, firstName: String?, lastName: String?, nameCheck: String?, age: String?,
                                      siblingMale: String?, siblingFemale: String?, siblingNumber: String?, children: String?, hometown01: String?, hometown02: String?,
                                      job: String?, income: String?, fortune: String?, education: String?, car: String?, religion: String?, parents: String?, marryPlan: String?,
                                      divorce: String?, divorceYear: String?, height: String?, weight: String?, drinking: String?, drinkingNumber: String?, smoking: String?,
                                      blood: String?, character: String?, hobby: String?, myStyle: String?, dateStyle: String?, introduce: String?, textColor: String?, family: String?,
                                      route: String?, phone: String?, deleteImage: String?, deleteMarryImage : String?,  profileImage: ArrayList<Uri>?, marryCertImage: ArrayList<Uri>?) {
        val subscriber = object: DisposableObserver<ResultItem<MyInformationData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMyInformationEditFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MyInformationData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        val profileState = it.item?.mbProfileState
//                        if(it.message.equals("최초 프로필 업데이트")) {
//                            val subscriber = object : DisposableObserver<ResultItem<BaseItem>>() {
//                                override fun onComplete() {
//
//                                }
//
//                                override fun onError(e: Throwable) {
//                                    e.printStackTrace()
//                                }
//
//                                override fun onNext(item: ResultItem<BaseItem>) {
//                                    item.let { it ->
//                                        mView.setMyInformationEditComplete(profileState)
//                                    }
//                                }
//                            }
//
//                            //Context 없이 resource에 있는 String 값을 불러오는 법을 몰라 직접 작성. 알게 되면 바꾸도록.
////                            ItemModel().requestCoinCharge(it.item?.mbId, "프로필 작성 완료 보상","10","","", subscriber)
//
//                        }else{
                            mView.setMyInformationEditComplete(profileState)
//                        }
                    }
                    else {
                        mView.setMyInformationEditFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyInformationEdit(id, area01, area02, type, firstName, lastName, nameCheck, age, siblingMale, siblingFemale, siblingNumber, children,
                hometown01, hometown02, job, income, fortune, education, car, religion, parents, marryPlan, divorce, divorceYear, height, weight, drinking, drinkingNumber,
                smoking, blood, character, hobby, myStyle, dateStyle, introduce, textColor, family, route, phone, deleteImage,deleteMarryImage, profileImage, marryCertImage, subscriber)
        subscription.add(subscriber)
    }

}
