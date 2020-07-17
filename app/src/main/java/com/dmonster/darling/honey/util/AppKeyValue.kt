package com.dmonster.darling.honey.util

class AppKeyValue {

    companion object {
        val instance: AppKeyValue
            get() = Singleton.instance
    }

    private object Singleton {
        val instance = AppKeyValue()
    }

    val inAppKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArNclx33YSa/HmTo0fS5UgxwWWcve8eR09iHIJXTFqMvpR2GNT2muspc4vC4SZIQWrmipdFcWSd7Ya1wdYb1xV/3Qz6juYj212neG3/npkhaEAn8OoPf5jiTIMudqir5FWhDFqLJIbeiynln9k3RdQEa9CdWG8cIcJI6ESlQrvvv5qq5z62MF9SzNtMausgXBLOSmX0HFV2IX8TuAlyxMO9AuQOZpl5TAlsOdZm9Lzgkz8Gmupuq9k4S1EQIMSkho0dOAjPz96voMD6SyqmQtrDsE184Xl+gMRW1/jOwS3MVoJwtau0AY1iUUhJVdLGxKkJBHoZqw0mvzkFpvpv7rRwIDAQAB"
    val inAppItem01 = "honey_coin_item01"
    val inAppItem02 = "honey_coin_item02"
    val inAppItem03 = "honey_coin_item03"
    val inAppItem04 = "honey_coin_item04"
    val inAppItem05 = "honey_coin_item05"
    val inAppItem06 = "honey_coin_item06"
    val inAppItemCoin01 = "10000"
    val inAppItemCoin02 = "30000"
    val inAppItemCoin03 = "50000"
    val inAppItemCoin04 = "100000"
    val inAppItemCoin05 = "200000"
    val inAppItemCoin06 = "300000"

    val keyYes = "1"
    val keyNo = "0"

    val keyMale = "M"
    val keyFemale = "F"

    val keyBloodA = "A"
    val keyBloodB = "B"
    val keyBloodAB = "AB"
    val keyBloodO = "O"

    val keyTypeBasic = ""
    val keyTypeSocial = "sns"

    val listStartCnt = "0"
    val listLimitCnt = "25"
    val talkLimitCnt = "10"

    val profileListOpen = "open"
    val profileListVisit = "visit"

    val blockBlock = "block"
    val blockUnblock = "unblock"

    val tagEmailCheckDlg = "tagEmailCheckDlg"
    val tagSelectorDlg = "tagSelectorDlg"
    val tagFindIDDlg = "tagFindIDDlg"
    val tagFindPWDlg = "tagFindPWDlg"
    val tagDormantDlg = "tagDormantDlg"
    val tagDormantClearDlg = "tagDormantClearDlg"
    val tagWithdrawalDlg = "tagWithdrawalDlg"
    val tagWithdrawalReDlg = "tagWithdrawalReDlg"
    val tagWithdrawalSelectDlg = "tagWithdrawalSelectDlg"
    val tagNotifyDlg = "tagNotifyDlg"
    val tagGoodDlg = "tagGoodDlg"
    val tagInterestDlg = "tagInterestDlg"
    val tagSocialJoinDlg = "tagSocialJoinDlg"
    val tagItemProfileDlg = "tagItemProfileDlg"
    val tagItemGoodDlg = "tagItemGoodDlg"
    val tagItemTalkDlg = "tagItemTalkDlg"
    val tagItemLoveDlg = "tagItemLoveDlg"
    val tagItemRefreshDlg = "tagItemRefreshDlg"
    val tagLoveDlg = "tagLoveDlg"

    val infoCheckMarry = "infoCheckMarry"
    val infoCheckParents = "infoCheckParents"
    val infoCheckMarryPlan = "infoCheckMarryPlan"
    val infoCheckDivorce = "infoCheckDivorce"
    val infoCheckDrink = "infoCheckDrink"
    val infoCheckDrinkNumber = "infoCheckDrinkNumber"
    val infoCheckSmoke = "infoCheckSmoke"
    val infoCheckBlood = "infoCheckBlood"
    val infoCheckRoute = "infoCheckRoute"
    val infoSelectTextColor = "infoSelectTextColor"
    val intentPhone = "intentPhone"

    val intentAgreement = "agreementTypes"
    val typesAgreement = "provision"
    val typesPersonal = "privacy"
    val typesUse = "guide"

    val searchTypeArea = "searchTypeArea"
    val searchTypeGender = "searchTypeGender"
    val searchTypeAge = "searchTypeAge"

    val memberTypeAge = "memberTypeAge"
    val memberTypeSibling = "memberTypeSibling"
    val memberTypeHometown = "memberTypeHometown"
    val memberTypeIncome = "memberTypeIncome"
    val memberTypeFortune = "memberTypeFortune"
    val memberTypeEducation = "memberTypeEducation"
    val memberTypeCar = "memberTypeCar"
    val memberTypeReligion = "memberTypeReligion"
    val memberTypeChildren = "memberTypeChildren"
    val memberTypeCharacter = "memberTypeCharacter"
    val memberTypeHobby = "memberTypeHobby"
    val memberTypeLimit = "memberTypeLimit"
    val memberTypeNotLimit = "memberTypeNotLimit"
    val memberTypeAgeLimit = "memberTypeAgeLimit"
    val memberTypeIncomeLimit = "memberTypeIncomeLimit"
    val memberTypeEducationLimit = "memberTypeEducationLimit"
    val memberTypeReligionLimit = "memberTypeReligionLimit"
    val memberTypeBloodLimit = "memberTypeBloodLimit"
    val memberTypeMyStyleLimit = "memberTypeMyStyleLimit"

    val memberSelectOne = "memberSelectOne"
    val memberSelectTwo = "memberSelectTwo"
    val memberSelectThree = "memberSelectThree"

    val itemBundleCoin = "itemBundleCoin"
    val idealTypeBundle = "idealTypeBundle"

    val itemTypeAdmin = "itemTypeAdmin"
    val itemTypeMore = "itemTypeMore"

    val itemIdCharge = "0"
    val itemIdTalk = "1"
    val itemIdProfile = "2"
    val itemIdGood = "3"
    val itemIdRecommend = "4"
    val itemIdLove = "11"
    val itemIdRefresh = "12"
    val itemIdPicture = "13"

    val itemCoinProfile = "3000:5000:14000:27000"
    val itemCoinGood = "3000:4500:8000:15000"
    val itemCoinTalk = "5900:19200:36500:45900:79000"
    val itemCoinLove = "20000:30000"
    val itemCoinRefresh = "6000:20000:50000:70000"

    val eventBusProfile = "eventBusProfile"
    val eventBusCoin = "eventBusCoin"
    val eventBusMainFrag = "eventBusMainFrag"
    val eventBusTalkDelete = "eventBusTalkDelete"
    val eventBusItemFrag = "eventBusItemFrag"
    val eventBusItemFragActivity = "eventBusItemFragActivity"
    val eventBusPhoneAuth = "eventBusPhoneAuth"
    val eventBusCoinCharge = "eventBusCoinCharge"
    val eventBusIdealType = "eventBusIdealType"
    val eventBusInquiry = "eventBusInquiry"

    val profileMbNo = "profileMbNo"
    val profileDetailOtherId = "profileDetailOtherId"
    val profileDetailTalkId = "profileDetailTalkId"
    val profileDetailOtherArea = "profileDetailOtherArea"
    val profileDetailOtherAge = "profileDetailOtherAge"
    val profileDetailImagePosition = "profileDetailImagePosition"
    val profileDetailImage = "profileDetailImage"
    val profileDetailItemCheck = "profileDetailItemCheck"

    val notifyType01 = "liar"
    val notifyType02 = "r18"
    val notifyType03 = "adver"
    val notifyType04 = "etc"

    val talkCheckTime = "0000-00-00 00:00:00"
    val talkRoomNo = "talkRoomNo"
    val talkMbNo = "talkMbNo"
    val talkOtherId = "talkOtherId"
    val talkOtherTalkId = "talkOtherTalkId"
    val talkImage = "talkImage"
    val marketOtherId = "marketOtherId"

    val talkTitleName = "talkTitleName"
    val talkTitleArea = "talkTitleArea"
    val talkTitleAge = "talkTitleAge"

    val goodOtherId = "goodOtherId"
    val goodOtherProfileImage = "goodOtherProfileImage"
    val goodOtherTalkId = "goodOtherTalkId"
    val goodOtherType = "goodOtherType"

    val savePrefID = "prefID"
    val savePrefNick = "prefNick"
    val savePrefPassword = "prefPassword"
    val savePrefType = "savePrefType"
    val savePrefMbNumber = "prefMbNumber"
    val savePrefGender = "prefGender"
    val savePrefProfile = "prefProfile"    // 프로필등록여부
    val savePrefDormant = "prefDormant"    // 휴면여부
    val savePrefRecommend = "prefRecommend"    // 추천인 회원번호
    val alarm_event_agreed = "alarm_event_agreed"

    val notificationChannelId = "notificationChannelId"
    val notificationChannelName = "notificationChannelName"
    val notificationChannelDescription = "notificationChannelDescription"

    val pushNotificationType = "pushNotificationType"
    val pushRoomNo = "pushRoomNo"
    val pushMbNo = "pushMbNo"
    val pushOtherId = "pushOtherId"
    val pushOtherTalkId = "pushOtherTalkId"
    val pushNewMember = "pushNewMember"

    val phoneAuthPhone = "phoneAuthPhone"
    val requestPhoneAuth = 1000

    val profileItem = "profileItem"
    val requestProfileItem = 2000

    val findID = "findID"
    val findPW = "findPW"
    val requestFindIDPW = 3000
    val goToMarket = "goToMarket"
    val goToMagazine = "goToMagazine"
    val goToMailBox ="goToMailBox"
    val goToOption ="goToOption"

    val noMoreSeePromotion = "noMoreSeePromotion"

    val phoneCert = "phoneCert"
    val hasFreePass = "hasFreePass"

}
