package com.dmonster.darling.honey.util.retrofit

import com.dmonster.darling.honey.agreement.data.AgreementData
import com.dmonster.darling.honey.agreement.data.UseData
import com.dmonster.darling.honey.alarm.data.AlarmData
import com.dmonster.darling.honey.block.data.BlockData
import com.dmonster.darling.honey.information.data.MyInfoData
import com.dmonster.darling.honey.information.data.PhoneAuthData
import com.dmonster.darling.honey.inquiry.data.InquiryData
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.point.data.*
import com.dmonster.darling.honey.block_friends.data.ContactData
import com.dmonster.darling.honey.join.data.JoinData
import com.dmonster.darling.honey.login.data.FindIDPWData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.magazine.data.MagazineData
import com.dmonster.darling.honey.main.data.BannerData
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.main.data.NaviData
import com.dmonster.darling.honey.main.data.NoticePopupData
import com.dmonster.darling.honey.myactivity.data.MemberData
import com.dmonster.darling.honey.myactivity.data.ProfileData
import com.dmonster.darling.honey.myactivity.data.SpouseAreaData
import com.dmonster.darling.honey.myactivity.data.TalkListData
import com.dmonster.darling.honey.myinformation.data.MyInformationData
import com.dmonster.darling.honey.myinformation.data.RecommendData
import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.option.data.SimpleMyInfoData
import com.dmonster.darling.honey.profile.data.ProfileDetailData
import com.dmonster.darling.honey.question.data.QuestionData
import com.dmonster.darling.honey.servicecenter.data.AppInfoData
import com.dmonster.darling.honey.talk.data.TalkData
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitService {

    /*    인트로 로그인    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestIntroLogin(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_password") password: String?,
        @Field("instanceid") instanceId: String?,
        @Field("login_type") type: String?
    ): Observable<ResultItem<IntroLoginData>>

    /*    로그인    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestLogin(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_password") password: String?,
        @Field("instanceid") instanceId: String?,
        @Field("login_type") type: String?
    ): Observable<ResultItem<LoginData>>

    /*    아이디/비밀번호 찾기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestFindIDPW(
        @Field("method") method: String?,
        @Field("type") type: String?,
        @Field("mb_hp") phone: String?,
        @Field("mb_id") email: String?
    ): Observable<ResultItem<FindIDPWData>>

    /*    아이디 사용가능 유무    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestJoinId(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<String>>

    /*    회원가입    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestJoin(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_id_addr") email: String?,
        @Field("mb_password") password: String?,
        @Field("mb_password_re") passwordCheck: String?,
        @Field("mb_nick") talkId: String?,
        @Field("mb_birth") age: String?,
        @Field("mb_addr1") area01: String?,
        @Field("mb_addr2") area02: String?,
        @Field("mb_sex") gender: String?,
        @Field("mb_id_recommend") recommendation: String?,
        @Field("mb_hp") phone: String?
    ): Observable<ResultItem<JoinData>>

    /*   등록된 연락처 검색    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun checkContact(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("contacts") contacts: String?
    ): Observable<ResultListItem<ContactData>>

    /*   등록된 페이스북 연락처 검색    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun checkFacebookFriends(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("fb_ids") contacts: String?
    ): Observable<ResultListItem<ContactData>>


    /*    회원 초기 작업 시 사람들 차단    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun blockIds(
        @Field("method") method: String?,
        @Field("ids") contacts: String?,
        @Field("mb_id") mb_id: String?
    ): Observable<BaseItem>

    /*    소셜 회원가입    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestSocialJoin(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_id_addr") email: String?,
        @Field("mb_nick") talkId: String?,
        @Field("mb_birth") age: String?,
        @Field("mb_addr1") area01: String?,
        @Field("mb_addr2") area02: String?,
        @Field("mb_sex") gender: String?,
        @Field("mb_id_recommend") recommendation: String?,
        @Field("mb_hp") phone: String?,
        @Field("login_type") type: String?,
        @Field("mb_sns") social: String?
    ): Observable<ResultItem<JoinData>>

    /*    이용약관    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestAgreement(
        @Field("method") method: String?,
        @Field("co_id") types: String?
    ): Observable<ResultItem<AgreementData>>

    /*    메인목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMainList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?,
        @Field("mb_addr1") area: String?,
        @Field("mb_sex") gender: String?,
        @Field("mb_age") age: String?,
        @Field("mb_profile_state") profileState: String?
    ): Observable<ResultListItem<MainListData>>

    /*    메인목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestFilterList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?,
        @Field("mb_addr1") area: String?,
        @Field("mb_lat") latitude : String?,
        @Field("mb_lng") longitude : String?,
        @Field("sst") sst : String?,
        @Field("mb_sex") gender: String?,
        @Field("mb_age") age: String?,
        @Field("mb_char") marry : String?,
        @Field("mb_religion") religion: String?,
        @Field("mb_income") income: String?,
        @Field("mb_property") property: String?,
        @Field("mb_blood") blood: String?,
        @Field("mb_edu_level") edu_level: String?,
        @Field("mb_drink") drink: String?,
        @Field("mb_baby") baby: String?,
        @Field("bf_certify_mb_img") certify_profile: String?,
        @Field("bf_certify_mb_marry_img") certify_marry: String?,
        @Field("mb_profile_state") profileState: String?
    ): Observable<ResultListItem<MainListData>>

    /*배너 광고 정보*/
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBannerInfo(@Field("method") method: String?): Observable<ResultListItem<BannerData>>

    /*    나의톡 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyTalkList(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("limit_count") limitCnt: String?
    ): Observable<ResultListItem<TalkListData>>

    /*    러브카드 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestLoveList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<MemberData>>

    /*    나의톡 삭제    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyTalkDelete(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("idx") idx: String?
    ): Observable<BaseItem>

    /*    신규회원 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestNewMemberList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<MemberData>>

    /*    희망배우자지역    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestSpouseArea(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?
    ): Observable<ResultItem<SpouseAreaData>>

    /*    프로필 열람 기록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestVisitLog(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") type: String?
    ): Observable<ResultListItem<ProfileData>>

    /*    프로필열람 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestProfileList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?,
        @Field("type") type: String?
    ): Observable<ResultListItem<ProfileData>>

    /*    프로필열람 목록 삭제    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestProfileListDelete(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("type") type: String?,
        @Field("idx") idx: String?
    ): Observable<BaseItem>

    /*    내정보    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyInformation(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?
    ): Observable<ResultItem<MyInformationData>>

    /*    내정보 입력/변경    */
    @Multipart
    @POST("json/proc_json.php")
    fun requestMyInformationEdit(
        @Part("method") method: RequestBody?,
        @Part("mb_id") id: RequestBody?,
        @Part("mb_addr1") area01: RequestBody?,
        @Part("mb_addr2") area02: RequestBody?,
        @Part("mb_char") type: RequestBody?,
        @Part("mb_name_0") firstName: RequestBody?,
        @Part("mb_name_1") lastName: RequestBody?,
        @Part("mb_name_chk") nameCheck: RequestBody?,
        @Part("mb_birth_year") age: RequestBody?,
        @Part("mb_brother_m") siblingMale: RequestBody?,
        @Part("mb_brother_f") siblingFemale: RequestBody?,
        @Part("mb_brother_n") siblingNumber: RequestBody?,
        @Part("mb_baby") children: RequestBody?,
        @Part("mb_addr3") hometown01: RequestBody?,
        @Part("mb_addr4") hometown02: RequestBody?,
        @Part("mb_jobs") job: RequestBody?,
        @Part("mb_income") income: RequestBody?,
        @Part("mb_property") fortune: RequestBody?,
        @Part("mb_edu_level") education: RequestBody?,
        @Part("mb_car") car: RequestBody?,
        @Part("mb_religion") religion: RequestBody?,
        @Part("mb_parents") parents: RequestBody?,
        @Part("mb_plan_marry") marryPlan: RequestBody?,
        @Part("mb_divorce") divorce: RequestBody?,
        @Part("mb_divorce_age") divorceYear: RequestBody?,
        @Part("mb_tall") height: RequestBody?,
        @Part("mb_weight") weight: RequestBody?,
        @Part("mb_drink") drinking: RequestBody?,
        @Part("mb_drink_num") drinkingNumber: RequestBody?,
        @Part("mb_cigarette") smoking: RequestBody?,
        @Part("mb_blood") blood: RequestBody?,
        @Part("mb_character") character: RequestBody?,
        @Part("mb_hobby") hobby: RequestBody?,
        @Part("mb_style") myStyle: RequestBody?,
        @Part("mb_styledate") dateStyle: RequestBody?,
        @Part("mb_profile") introduce: RequestBody?,
        @Part("mb_profile_color") textColor: RequestBody?,
        @Part("mb_family") family: RequestBody?,
        @Part("mb_joinroute") route: RequestBody?,
        @Part("mb_hp") phone: RequestBody?,
        @Part("del_mb_img") deleteImage: RequestBody?,
        @Part("del_mb_marry_img") deleteMarryImage: RequestBody?,
        @Part profileImage: Array<MultipartBody.Part?>,
        @Part marryCertImage: Array<MultipartBody.Part?>
    ): Observable<ResultItem<MyInformationData>>

    /*    추천인 현황    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestRecommend(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?
    ): Observable<ResultListItem<RecommendData>>

    /*    이상형선택    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestSelectIdealType(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_hope_birth") age: String?,
        @Field("mb_hope_addr") area: String?,
        @Field("mb_hope_tall") height: String?,
        @Field("mb_hope_weight") weight: String?,
        @Field("mb_hope_style") style: String?,
        @Field("mb_hope_income") income: String?,
        @Field("mb_hope_level") education: String?,
        @Field("mb_hope_religion") religion: String?,
        @Field("mb_hope_blood") blood: String?
    ): Observable<BaseItem>

    /*    현재보유코인    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestPossessionCoin(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<CoinData>>

    /*    내 아이템 조회    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestItemList(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<ItemData>>

    /*    아이템 사용내역    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestItemUseList(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<ItemUseData>>

    /*    아이템구매    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBuyItem(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("it_id") itemId: String?,
        @Field("it_coin") coin: String?,
        @Field("it_qty") count: String?
    ): Observable<ResultItem<BaseItem>>

    /*    코인충전    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestCoinCharge(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("od_case") case: String?,
        @Field("coin") coin: String?,
        @Field("od_receipt_id") orderId: String?,
        @Field("od_receipt_price") productId: String?
    ): Observable<ResultItem<BaseItem>>

    /*    내아이템 확인    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyItemCheck(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("it_id") itemId: String?
    ): Observable<ResultItem<String>>

    /*    내아이템 사용    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestItemUse(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("it_id") itemId: String?,
        @Field("other_mb_id") mbNo: String?
    ): Observable<ResultItem<String>>

    /*    마켓 상품목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMarket(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<MarketListData>>

    /*    마켓 상품구매, 선물    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMarketBuy(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("it_id") productId: String?,
        @Field("it_qty") count: String?,
        @Field("it_info") type: String?,
        @Field("receiver_id") otherId: String?
    ): Observable<ResultItem<MarketBuyData>>

    /*    프로필 상세보기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestProfile(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?
    ): Observable<ResultItem<ProfileDetailData>>

    /*    호감표현하기, 관심표현하기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestGood(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("receive_id") receiveId: String?,
        /*@Field("msg") content: String?,*/
        @Field("idx") cardIndex: String?
    ): Observable<ResultItem<BaseItem>>

    /*    신고하기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestNotify(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?,
        @Field("type") type: String?
    ): Observable<ResultItem<BaseItem>>

    /*    톡하기 여부    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestTalkCheck(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("receive_id") otherId: String?
    ): Observable<ResultItem<ArrayList<String>>>

    /*    톡하기 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestTalkList(
        @Field("method") method: String?,
        @Field("item_count") startCnt: String?,
        @Field("limit_count") limitCnt: String?,
        @Field("mb_id") id: String?,
        @Field("idx") roomNo: String?
    ): Observable<ResultListItem<TalkData>>

    /*    톡하기    */
    @Multipart
    @POST("json/proc_json.php")
    fun requestTalk(
        @Part("method") method: RequestBody?,
        @Part("mb_id") id: RequestBody?,
        @Part("receive_id") otherId: RequestBody?,
        @Part("msg") message: RequestBody?,
        @Part talkImage: MultipartBody.Part?
    ): Observable<ResultItem<TalkData>>

    /*    즐겨찾기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBookmark(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?,
        @Field("w") type: String?
    ): Observable<ResultListItem<BaseItem>>

    /*    자동갱신    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestAutoList(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<BaseItem>

    /*    앱정보    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestAppInfo(@Field("method") method: String?): Observable<AppInfoData>


    /*    공지사항    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestNotice(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("bo_table") table: String?
    ): Observable<ResultListItem<NoticeData>>

    /*    기본정보    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyInfo(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?
    ): Observable<ResultItem<MyInfoData>>

    /*    기본정보 수정하기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestMyInfoEdit(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_nick") talkId: String?,
        @Field("mb_birth_year") age: String?,
        @Field("mb_char") type: String?,
        @Field("mb_addr1") area01: String?,
        @Field("mb_addr2") area02: String?,
        @Field("mb_password_old") password: String?,
        @Field("mb_password") newPassword: String?,
        @Field("mb_password_re") newPasswordCheck: String?,
        @Field("mb_hp") phone: String?
    ): Observable<ResultItem<MyInfoData>>

    /*    핸드폰번호 문자인증    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestPhoneAuth(
        @Field("method") method: String?,
        @Field("mb_hp") phone: String?
    ): Observable<ResultItem<PhoneAuthData>>

    /*    핸드폰번호 문자인증확인    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestPhoneAuthCheck(
        @Field("method") method: String?,
        @Field("mb_hp") phone: String?,
        @Field("certno") authNo: String?
    ): Observable<ResultItem<PhoneAuthData>>

    /*    알림설정 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestAlarm(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<AlarmData>>

    /*    알림설정하기    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestAlarmSetting(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_push_yn") all: String?,
        @Field("mb_push_msg") newTalk: String?,
        @Field("mb_push_hogam") good: String?,
        @Field("mb_push_mb") newMember: String?,
        @Field("mb_push_loveCard") loveCard: String?,
        @Field("mb_push_notice") notice: String?
    ): Observable<ResultItem<AlarmData>>

    /*    문의하기    */
    @Multipart
    @POST("json/proc_json.php")
    fun requestInquiry(
        @Part("method") method: RequestBody?,
        @Part("mb_id") id: RequestBody?,
        @Part("ca_name") password: RequestBody?,
        @Part("wr_hp") passwordCheck: RequestBody?,
        @Part("wr_content") name: RequestBody?,
        @Part profileImage: MultipartBody.Part?
    ): Observable<ResultItem<BaseItem>>

    /*    문의내역    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBreakdown(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<InquiryData>>

    /*    자주묻는 질문    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestQuestion(
        @Field("method") method: String?,
        @Field("last_idx") startCnt: String?,
        @Field("limit_count") limitCnt: String?
    ): Observable<ResultListItem<QuestionData>>

    /*    이용방법    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestUse(@Field("method") method: String?): Observable<ResultListItem<UseData>>

    /*    차단회원 목록    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBlockList(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("limit_count") limitCnt: String?
    ): Observable<ResultListItem<BlockData>>

    /*    회원차단    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBlock(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?,
        @Field("type") type: String?
    ): Observable<BaseItem>

    /*    회원차단해제    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestBlockClear(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?,
        @Field("type") type: String?
    ): Observable<BaseItem>

    /*    휴면설정    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestDormant(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("type") type: String?
    ): Observable<ResultItem<BaseItem>>

    /*    회원탈퇴    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestWithdrawal(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_password") password: String?,
        @Field("leave_reason") reason: String?
    ): Observable<ResultItem<BaseItem>>

    /*    회원탈퇴    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun getNaviCount(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<NaviData>>

    /*   옵션- 내정보    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestSimpleMyInformation(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("mb_no") mbNo: String?
    ): Observable<ResultItem<SimpleMyInfoData>>

    /*    메인 팝업    */
    @FormUrlEncoded
    @POST("json/proc_json.php")
    fun requestNotice(
        @Field("method") method: String?
    ): Observable<ResultItem<NoticePopupData>>



    /*    이용권 유무 확인    */
    @FormUrlEncoded
    @POST("api/base.php")
    fun checkOwnFreepass(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<CheckFreePassData>>

    /*    이용권 구매 내역 가져오기    */
    @FormUrlEncoded
    @POST("api/base.php")
    fun readPointLog(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultListItem<PointLogData>>

    /*    이용권 구매 내역 가져오기    */
    @FormUrlEncoded
    @POST("api/base.php")
    fun readPoint(
        @Field("method") method: String?,
        @Field("mb_id") id: String?
    ): Observable<ResultItem<PointData>>


    /*   아이템 구매    */
    @FormUrlEncoded
    @POST("api/base.php")
    fun buyItem(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("it_id") it_id: Int?
    ): Observable<ResultItem<String>>
    /*   포인트 충전    */
    @FormUrlEncoded
    @POST("api/base.php")
    fun rechargePoint(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("point") point: Int?
    ): Observable<ResultItem<String>>

    @FormUrlEncoded
    @POST("api/base.php")
    fun reservePayment(
        @Field("method") method: String?,
        @Field("mb_id") id: String?,
        @Field("name") name: String?,
        @Field("price") price: Int?


        ): Observable<ResultItem<String>>

    @FormUrlEncoded
    @POST("api/base.php")
    fun readMagazine(
        @Field("method") method: String?
    ): Observable<ResultListItem<MagazineData>>


}
