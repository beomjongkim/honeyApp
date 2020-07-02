package com.dmonster.darling.honey.util

class ServerApi {

    companion object {
        val instance: ServerApi
            get() = Singleton.instance
    }

    private object Singleton {
        val instance = ServerApi()
    }

    val baseUrl = "https://admin.jjagiya.com/"
//    val baseUrl = "https://15.164.26.214"

    val loginMethod = "proc_login_member"    // 로그인
    val findIDPWMethod = "proc_send_pw_email"    // 아이디/비밀번호 찾기
    val joinIdMethod = "proc_check_mb_email"    // 아이디 사용가능 유무
    val joinMethod = "proc_add_member"    // 회원가입

    val blockContact = "proc_edit_block_member_contact"//연락처 지인 차단
    val blockFacebook = "proc_edit_block_member_facebook"//페이스북 지인 차단

    val agreementMethod = "proc_list_content"    // 이용약관
    val mainListMethod = "proc_list_member"    // 메인목록
    val myTalkListMethod = "proc_list_msg"    // 나의톡 목록
    val loveListMethod = "proc_list_member_opposite"    // 러브카드 목록
    val myTalkDeleteMethod = "proc_delete_room"    // 나의톡 삭제
    val newMemberListMethod = "proc_list_member_new"    // 신규회원 목록
    val profileListMethod = "proc_list_visit_member"    // 프로필열람 목록
    val profileVisitLogMethod = "proc_list_visit_log"
    val profileListDeleteMethod = "proc_delete_visit_member"    // 프로필열람 목록 삭제
    val myInformationMethod = "proc_detail_member_get"    // 내정보
    val myInformationEditMethod = "proc_edit_member_detail"    // 내정보 입력/수정
    val simpleMyInformationMethod = "proc_simple_member"    // 내정보
    val recommendMethod = "proc_list_member_friend"    // 추천인 현황 목록
    val selectIdealTypeMethod = "proc_edit_member_hope"    // 이상형 선택
    val possessionCoinMethod = "proc_check_count_coin"    // 현재보유코인
    val itemListMethod = "proc_list_member_item"    // 내 아이템 조회
    val itemUseMethod = "proc_list_coin_use"    // 아이템 사용내역
    val buyItemMethod = "proc_add_member_item"    // 아이템 구매
    val coinChargeMethod = "proc_add_coin"    // 코인충전
    val myItemCheckMethod = "proc_check_member_item"    // 내아이템 확인
    val myItemUseMethod = "proc_use_member_item"    // 내아이템 사용
    val marketListMethod = "proc_list_product"    // 마켓 상품목록
    val marketBuyMethod = "proc_add_member_product"    // 마켓 상품구매, 선물
    val profileDetailMethod = "proc_detail_member"    // 프로필 상세보기
    val goodMethod = "proc_add_msg_hogam"    // 호감표현하기, 관심표현하기
    val notifyMethod = "proc_add_singo"    // 신고하기
    val talkCheckMethod = "proc_check_msg_flag"    // 톡하기 여부
    val talkListMethod = "proc_detail_msg"    // 톡하기 목록
    val talkMethod = "proc_add_msg"    // 톡하기
    val bookmarkMethod = "proc_add_wish_member"    // 즐겨찾기
    val autoListMethod = "proc_edit_member_list_up"    // 자동갱신
    val appInfoMethod = "proc_list_site"    // 앱정보
    val noticeMethod = "proc_list_board"    // 공지사항
    val myInfoMethod = "proc_detail_member_basic"    // 기본정보
    val myInfoEditMethod = "proc_edit_member"    // 기본정보 수정
    val phoneAuthMethod = "proc_send_certno_new"    // 핸드폰번호 문자인증
    val phoneAuthCheckMethod = "proc_send_certno_chk_new"    // 핸드폰번호 문자인증확인
    val alarmMethod = "proc_check_count_push"    // 알림설정 목록
    val alarmSettingMethod = "proc_edit_member_status"    // 알림설정하기
    val inquiryMethod = "proc_add_help"    // 문의하기
    val breakdownMethod = "proc_list_help"    // 문의내역
    val questionMethod = "proc_list_faq"    // 자주묻는 질문
    val useMethod = "proc_list_content1"    // 이용방법
    val blockListMethod = "proc_list_block_member"    // 차단회원 목록
    val blockMethod = "proc_edit_block_member"    // 회원차단
    val blockClearMethod = "proc_edit_block_member"    // 차단해제
    val dormantMethod = "proc_edit_member_sleep"    // 휴면설정
    val withdrawalMethod = "proc_leave_member"    // 회원탈퇴
    val bannerInfoMethod = "proc_list_banner" //배너 정보 가져오기
    val naviCountAlarmMethod = "proc_navi_get"
    val mainNoticeMethod = "proc_get_notice_popup"
    val checkOwnFreepass =  "user/check_own_freepass"// 이용권 소지 여부 확인
    val readPointLog = "point/read_log" //포인트 이용 내역 가져오기
    val readPoint = "point/read" //포인트 이용 내역 가져오기
    val reservePayment = "user/reserve_payment"
    val buyItem = "user/buy_item"//이용권 구매하기.
    val readMagazine = "magazine/read"//매거진 내역 불러오기
    val rechargePoint = "user/recharge_point"//포인트 충전하기
}
