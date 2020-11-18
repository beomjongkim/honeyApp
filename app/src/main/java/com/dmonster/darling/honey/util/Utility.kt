package com.dmonster.darling.honey.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AlertDialog
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.login.view.LoginEmailActivity
import com.facebook.login.LoginManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class Utility private constructor() {

    companion object {
        val instance: Utility
            get() = SingleTone.instance
    }

    private object SingleTone {
        val instance = Utility()
    }

    private var userId : String? = null
    private var userMb: String? = null    // 회원번호
    private var userNick: String? = null // 회원닉네임
    private var userGender: String? = null    // 성별
    private var userProfile: String? = null    // 프로필 등록여부
    private var userDormant: String? = null    // 휴면여부
    private var userRecommend: String? = null    // 추천인 회원번호

    inner class UserData {
        fun setUserId(userId : String?){
            this@Utility.userId = userId
        }
        fun getUserId() : String?{
            return userId
        }
        fun setUserMb(userMb: String?) {
            this@Utility.userMb = userMb
        }

        fun getUserMb(): String? {
            return userMb
        }

        fun getUserNick(): String? {
            return userNick
        }

        fun setUserNick(userNick: String?) {
            this@Utility.userNick = userNick
        }

        fun setUserGender(userGender: String?) {
            this@Utility.userGender = userGender
        }

        fun getUserGender(): String? {
            return userGender
        }

        fun setUserProfile(userProfile: String?) {
            this@Utility.userProfile = userProfile
        }

        fun getUserProfile(): String? {
            return userProfile
        }

        fun setUserDormant(userDormant: String?) {
            this@Utility.userDormant = userDormant
        }

        fun getUserDormant(): String? {
            return userDormant
        }

        fun setUserRecommend(userRecommend: String?) {
            this@Utility.userRecommend = userRecommend
        }

        fun getUserRecommend(): String? {
            return userRecommend
        }
    }

    private var mToast: Toast? = null

    fun showToast(context: Context, message: String?) {
        showToast(context, message, Toast.LENGTH_LONG)
    }

    private fun showToast(context: Context?, message: String?, length: Int) {
        if (context != null) {
            if (mToast != null) {
                mToast?.cancel()
                mToast = null
            }
            mToast = Toast.makeText(context, message, length)
            mToast?.show()
        }
    }

    fun showNotice(
        context: Context,
        titleRedId: Int,
        messageRedId: Int
    ) {
        showNotice(
            context, context.getString(titleRedId),
            context.getString(messageRedId)
        )
    }

    fun showNotice(
        context: Context,
        title: String,
        message: String
    ) {
        var popup = CustomPopup(context, title, message, null)
        popup.show()
    }

    fun showNotice(
        context: Context,
        title: String,
        message: String,
        subMessage: String,
        link: String
    ) {

        var popup = CustomPopup(context, title, message, subMessage, link, null)

        popup.show()
    }


    fun showAlert(
        context: Context,
        title: String,
        message: String,
        onClickListener: CustomDialogInterface
    ) {
        var popup = CustomPopup(context, title, message, R.drawable.ic_talk_vivid, onClickListener)
        popup.popupVM.negativeText.value = ""
        popup.show()
    }

    /*    다이얼로그(확인)    */
    fun showAlert(
        context: Context,
        titleRedId: Int,
        messageRedId: Int,
        onClickListener: DialogInterface.OnClickListener
    ) {
        showAlert(
            context,
            context.getString(titleRedId),
            context.getString(messageRedId),
            onClickListener
        )
    }

    fun showAlert(
        context: Context,
        title: String,
        message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        var popup = CustomPopup(context, title, message, R.drawable.ic_talk_vivid, onClickListener)
        popup.popupVM.negativeText.value = ""
        popup.show()
    }

    /*    다이얼로그(확인/취소)    */
    fun showTwoButtonAlert(
        context: Context,
        title: String,
        message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        showTwoButtonAlert(context, title, message, null, null, onClickListener)
    }

    /*    다이얼로그(확인/취소)    */
    fun showTwoButtonAlert(
        context: Context,
        title: String,
        message: String,
        onClickListener: CustomDialogInterface
    ) {
        var popup = CustomPopup(context, title, message, R.drawable.ic_talk_vivid, onClickListener)
        popup.show()
    }

    fun showTwoButtonAlert(
        context: Context,
        title: String,
        message: String,
        onClickListener: DialogInterface.OnClickListener,
        keyListener: DialogInterface.OnKeyListener
    ) {
        val builder = AlertDialog.Builder(context, R.style.DialogTheme)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setMessage(message)

        builder.setNegativeButton(
            if (TextUtils.isEmpty(null)) context.getString(R.string.dialog_cancel) else null,
            onClickListener
        )
        builder.setPositiveButton(
            if (TextUtils.isEmpty(null)) context.getString(R.string.dialog_enter) else null,
            onClickListener
        )
        builder.setOnKeyListener(keyListener)

        val dialog = builder.create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    fun showTwoButtonAlert(
        context: Context,
        title: String,
        message: String,
        positive: String?,
        negative: String?,
        onClickListener: DialogInterface.OnClickListener
    ) {
        var popup = CustomPopup(context, title, message, R.drawable.ic_talk_vivid, onClickListener)
        negative?.let {
            popup.popupVM.negativeText.value = negative
        }
        positive?.let {
            popup.popupVM.positiveText.value = positive
        }
        popup.show()
    }


    /*    키보드 숨기기    */
    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null) {
            hideSoftKeyboard(activity, activity.currentFocus)
        }
    }

    private fun hideSoftKeyboard(context: Context?, focusView: View?) {
        if (context != null && focusView != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
    }

    fun getDip(context: Context, dimen: Int): Int {
        return context.resources.getDimension(dimen).toInt()
    }

    fun getPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun changePxToDp(context: Context, pixel: Int): Int {
        var dp = 0f
        val metrics = context.resources.displayMetrics
        dp = pixel / (metrics.densityDpi / 160f)

        return dp.toInt()
    }

    /*    화면가로크기    */
    fun getWidth(activity: Activity): Int {
        var dm = activity.applicationContext.resources.displayMetrics

        return dm.widthPixels
    }

    /*    화면세로크기    */
    fun getHeight(activity: Activity): Int {
        var dm = activity.applicationContext.resources.displayMetrics

        return dm.heightPixels
    }

    /*    drawable image to path    */
    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage()?.name + "/" + resourceId)
            .toString()
    }

    /*    스크롤뷰 전체 스크린샷    */
    fun getSaveImage(
        context: Context,
        nestedScrollView: NestedScrollView,
        fileName: String,
        linearLayout: LinearLayout,
        isShared: Boolean
    ) {
        nestedScrollView.fullScroll(View.FOCUS_DOWN)

        val saveImgFile = Environment.getExternalStorageState()
        if (saveImgFile == Environment.MEDIA_MOUNTED) {
            val dirPath: String = if (isShared) {
                Environment.getExternalStorageDirectory().absoluteFile.absolutePath + context.resources.getString(
                    R.string.app_folder_name_shared
                )
            } else {
                Environment.getExternalStorageDirectory().absoluteFile.absolutePath + context.resources.getString(
                    R.string.app_folder_name
                )
            }
            val file = File(dirPath)
            if (!file.exists()) {
                file.mkdirs()
            }

//            if(nestedScrollView.fullScroll(View.FOCUS_DOWN)) {
            val saveBitmap = getBitmapFromView(
                nestedScrollView,
                nestedScrollView.getChildAt(0).height,
                nestedScrollView.getChildAt(0).width
            )
            nestedScrollView.isDrawingCacheEnabled = true
            val btm = nestedScrollView.drawingCache
            val fos: FileOutputStream
            try {
//                    val url = context.resources.getString(R.string.app_save_image)
//                    fos = FileOutputStream(file.toString() + url)
                fos = FileOutputStream(file.toString() + fileName)
                saveBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.flush()
                fos.close()
                linearLayout.visibility = View.GONE

                if (!isShared) {
                    showAlert(
                        context,
                        context.getString(R.string.app_name),
                        context.getString(R.string.msg_app_save_image_complete),
                        DialogInterface.OnClickListener { dialog, which ->
                            nestedScrollView.scrollTo(0, 0)
                        })
                }
            } catch (e: FileNotFoundException) {
                linearLayout.visibility = View.GONE
                if (!isShared) {
                    showAlert(
                        context,
                        context.getString(R.string.app_name),
                        context.getString(R.string.msg_app_save_image_failed),
                        DialogInterface.OnClickListener { dialog, which ->
                            nestedScrollView.scrollTo(0, 0)
                        })
                }
                e.printStackTrace()
            }
//            }
        }
    }

    private fun getBitmapFromView(view: View, totalHeight: Int, totalWidth: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background

        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        return bitmap
    }

    /*    어댑터 날짜    */
    fun dateCompare(
        context: Context,
        position: Int,
        dateStr1: String?,
        dateStr2: String?,
        dateLayout: RelativeLayout,
        dateText: TextView
    ) {
        val beforeSimpleDateFormat = SimpleDateFormat(
            context.resources?.getString(R.string.utility_date_time_second),
            java.util.Locale.getDefault()
        )
        val beforeDateFormat01 = dateStr1.run { beforeSimpleDateFormat.parse(this) }
        val beforeDateFormat02 = dateStr2.run { beforeSimpleDateFormat.parse(this) }

        val afterSimpleDateFormat = SimpleDateFormat(
            context.resources?.getString(R.string.utility_date_format_day),
            java.util.Locale.getDefault()
        )
        val afterDateFormat01 = beforeDateFormat01.run { afterSimpleDateFormat.format(this) }
        val afterDateFormat02 = beforeDateFormat02.run { afterSimpleDateFormat.format(this) }

        afterDateFormat02.compareTo(afterDateFormat01).let {
            if (position == 0 || it > 0) {
                dateLayout.visibility = View.VISIBLE
                dateText.text = afterDateFormat01
            } else {
                dateLayout.visibility = View.GONE
            }
        }
    }

    fun isDayLater(compareDate: String?, _days: Int): Boolean {
        //특정 시간보다 몇일 뒤의 시간인지 계산하는 메소드
        if (!compareDate.isNullOrBlank()) {
            val df = SimpleDateFormat("yyMMdd", Locale.getDefault())
            val nowTime = System.currentTimeMillis()
            val today = Date(nowTime)
            val todayDate = df.parse(df.format(today))
            val otherDate = df.parse(compareDate)
            val days = TimeUnit.DAYS.convert(
                todayDate.time -
                        otherDate.time,
                TimeUnit.MILLISECONDS
            )
            return days >= _days
        } else {
            //저장된 값이 없다면 늘 true
            return true
        }
    }

    fun isNetworkConnectedCheck(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        /*val activeNetworkInfo = connectivityManager.activeNetworkInfo.type*/
        val mobileConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mobileConnect.isConnected || wifiConnect.isConnected) {
            return true
        }
        return false
    }

    /*    마켓Url 공유하기    */
    fun marketShared(context: Context) {
        val marketUrl = context.resources.getString(R.string.app_market_url)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, marketUrl)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = "text/plain"
        context.startActivity(
            Intent.createChooser(
                intent,
                context.resources.getString(R.string.app_shared)
            )
        )
    }

    /*    마켓Url 공유하기    */
    fun marketSharedFemale(context: Context) {
        val marketUrl =
            String.format(
                context.resources.getString(R.string.app_shared_female),
                UserData().getUserRecommend()
            )
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, marketUrl)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = "text/plain"
        context.startActivity(
            Intent.createChooser(
                intent,
                context.resources.getString(R.string.app_shared)
            )
        )
    }

    /*    이미지파일 공유하기    */
    fun imageShared(context: Context, fileName: String) {
        val dirPath =
            Environment.getExternalStorageDirectory().absoluteFile.absolutePath + context.resources.getString(
                R.string.app_folder_name_shared
            ) + fileName
        val file = File(dirPath)
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(
            Intent.createChooser(
                intent,
                context.resources.getString(R.string.app_shared)
            )
        )
    }

    /*    폴더 및 하위 파일삭제    */
    fun deleteDirectory(dirPath: String) {
        val file = File(dirPath)
        val childFileList = file.listFiles()
        if (childFileList != null) {
            for (childFile in childFileList) {
                if (childFile.isDirectory) {
                    deleteDirectory(childFile.absolutePath)    //하위 디렉토리
                } else {
                    childFile.delete()    //하위 파일
                }
            }
            file.delete()    //root 삭제
        }
    }

    fun getPref(context: Context, key: String): String? {
        val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    fun savePref(context: Context, key: String, value: String) {
        val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    fun exifOrientationToDegrees(exifOrientation: Int): Int {
        when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            else -> return 0
        }
    }

    /**
     * 이미지를 회전시킵니다.
     *
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var bitmap = bitmap
        if (degrees != 0 && bitmap != null) {
            val m = Matrix()
            m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
            try {
                val converted =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
                if (bitmap != converted) {
                    bitmap.recycle()
                    bitmap = converted
                }
            } catch (ex: OutOfMemoryError) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }

        }
        return bitmap
    }

    fun getLoginDialog(context: Context) {
        showAlert(
            context,
            context.getString(R.string.app_name),
            context.getString(R.string.msg_app_login),
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(context, LoginEmailActivity::class.java)
                context.startActivity(intent)
            })
    }

    /*    로그아웃    */
    fun setLogout(context: Context) {
        savePref(context, AppKeyValue.instance.savePrefPassword, "")
        savePref(context, AppKeyValue.instance.savePrefType, "")
        UserData().apply {
            setUserMb(null)
            setUserGender(null)
            setUserProfile(null)
            setUserDormant(null)
            setUserRecommend(null)
        }

        //페이스북 로그아웃
        LoginManager.getInstance().logOut()

        val intent = Intent(context, LoginEmailActivity::class.java)
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /*    전화번호 받아오기    */
    fun getPhoneNumber(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = context.resources.getString(R.string.app_phone_number)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return phoneNumber
        }
        if (!TextUtils.isEmpty(telephonyManager.line1Number)) {
            phoneNumber = if (telephonyManager.line1Number.length > 11) {
                "0" + telephonyManager.line1Number.substring(3)
            } else {
                "0" + telephonyManager.line1Number.substring(1)
            }
        }
        return phoneNumber
    }

    /*    전화번호 유효성 체크    */
    fun isValidCellPhoneNumber(cellphoneNumber: String?): Boolean {
        var returnValue = false

        val regex =
            "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$"
        val p = Pattern.compile(regex)
        val m = p.matcher(cellphoneNumber)

        if (m.matches()) {
            returnValue = true
        }

        return returnValue
    }

    /*    이메일 유효성 체크    */
    fun isValidEmail(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    /*    사업자등록번호 유효성 체크    */
    fun isBusinessRegistrationNo(businessRegistrationNo: String): Boolean {
        val bizRegNo = businessRegistrationNo.replace("[^0-9]".toRegex(), "")
        if (bizRegNo.length != 10) {
            return false
        }
        val share = (Math.floor((toInt(bizRegNo[8]) * 5).toDouble()) / 10).toInt()
        val rest = toInt(bizRegNo[8]) * 5 % 10
        val sum =
            toInt(bizRegNo[0]) + toInt(bizRegNo[1]) * 3 % 10 + toInt(bizRegNo[2]) * 7 % 10 + toInt(
                bizRegNo[3]
            ) * 1 % 10 + toInt(
                bizRegNo[4]
            ) * 3 % 10 + toInt(bizRegNo[5]) * 7 % 10 + toInt(bizRegNo[6]) * 1 % 10 + toInt(bizRegNo[7]) * 3 % 10 + share + rest + toInt(
                bizRegNo[9]
            )
        return sum % 10 == 0
    }

    /**
     * char로 표현된 숫자를 타입을 int로 변경
     */
    private fun toInt(c: Char): Int {
        return Integer.parseInt(c.toString())
    }

    /**
     * String으로 표현된 숫자를 타입을 int로 변경
     */
    private fun toInt(s: String): Int {
        return Integer.parseInt(s)
    }

    /*    차량번호 유효성 체크    */
    fun isValidCarNumber(context: Context, carNum: String): Boolean {
        var returnValue = false

        try {
            var regex = context.resources.getString(R.string.utility_car_regex01)

            var p = Pattern.compile(regex)
            var m = p.matcher(carNum)
            if (m.matches()) {
                returnValue = true
            } else {
                //2번째 패턴 처리
                regex = context.resources.getString(R.string.utility_car_regex02)
                p = Pattern.compile(regex)
                m = p.matcher(carNum)
                if (m.matches()) {
                    returnValue = true
                }
            }
            return returnValue
        } catch (e: Exception) {
            return false
        }
    }

    /*    천단위 콤마    */
    fun priceFormat(price: String): String {
        val dPrice = java.lang.Double.parseDouble(price)
        val dFormat = DecimalFormat("#,###")

        return dFormat.format(dPrice)
    }

    fun getFormer(context: Context): ArrayList<String?> {
        val formerArray = ArrayList<String?>()

        val nowTime = System.currentTimeMillis()
        val date = Date(nowTime)
        val dateFormat = SimpleDateFormat(context.resources.getString(R.string.utility_date_year))
        val getYear = dateFormat.format(date)
        val getSFormer = getYear.toInt().minus(69)
        val getEFormer = getYear.toInt().minus(19)

        for (i in getSFormer..getEFormer) {
            formerArray.add(i.toString() + context.resources.getString(R.string.utility_date_former))
        }
        formerArray.reverse()

        return formerArray
    }

    fun reArrangeGrid(array: ArrayList<String?>): ArrayList<String?> {

        val newArray: Array<String?> = arrayOfNulls(array.size)
        var size = array.size
        for (str in array) {
            val currentIndex = array.indexOf(str)

            var newIndex = 0
            val dividedSize = size / 3
            if (currentIndex < dividedSize) {
                newIndex = currentIndex % dividedSize * 3
            } else if (currentIndex < dividedSize * 2) {
                newIndex = currentIndex % dividedSize * 3 + 1
            } else {
                newIndex = currentIndex % dividedSize * 3 + 2
            }
            if (str != null) {
                newArray.set(newIndex, str)
            } else {
                newArray.set(newIndex, "-")
            }
        }

        return newArray.toList() as ArrayList<String?>
    }

    /*    이혼(사별)연도    */
    fun getDivorceYear(context: Context): ArrayList<String?> {
        val formerArray = ArrayList<String?>()

        val nowTime = System.currentTimeMillis()
        val date = Date(nowTime)
        val dateFormat = SimpleDateFormat(context.resources.getString(R.string.utility_date_year))
        val getYear = dateFormat.format(date)
        val getSFormer = getYear.toInt()
        val getEFormer = 2011

        for (i in getEFormer..getSFormer) {
            if (i == getEFormer) {
                formerArray.add(
                    String.format(
                        context.resources.getString(R.string.year_before),
                        i.toString()
                    )
                )
            } else {
                formerArray.add(
                    String.format(
                        context.resources.getString(R.string.year),
                        i.toString()
                    )
                )
            }
        }
        formerArray.reverse()

        return formerArray
    }

    /*    현재날짜 형식변경    */
    fun getNowTimeFormat(context: Context): String? {
        val nowTime = System.currentTimeMillis()
        val date = Date(nowTime)
        val dateFormat = SimpleDateFormat(context.getString(R.string.utility_date_time_second))

        return dateFormat.format(date)
    }

    /*    이미지 블러처리    */
    fun blur(context: Context, sentBitmap: Bitmap, radius: Int): Bitmap {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            val bitmap = sentBitmap.copy(sentBitmap.config, true)

            val rs = RenderScript.create(context)
            val input = Allocation.createFromBitmap(
                rs,
                sentBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
            val output = Allocation.createTyped(rs, input.type)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(radius.toFloat())    //0.0f ~ 25.0f
            script.setInput(input)
            script.forEach(output)
            output.copyTo(bitmap)

            return bitmap
        }

        return sentBitmap
    }

    fun getSubArrayArea(context: Context, area: String): Array<String?> {
        val resources = context.resources
        val area01Array = resources.getStringArray(R.array.member_area_array)

        var area02Array: Array<String?> = emptyArray()
        when (area) {
            area01Array[0] -> area02Array =
                resources.getStringArray(R.array.member_area_seoul_array)
            area01Array[1] -> area02Array =
                resources.getStringArray(R.array.member_area_gyeonggi_array)
            area01Array[2] -> area02Array =
                resources.getStringArray(R.array.member_area_guangwon_array)
            area01Array[3] -> area02Array =
                resources.getStringArray(R.array.member_area_gyeongnam_array)
            area01Array[4] -> area02Array =
                resources.getStringArray(R.array.member_area_gyeongbuk_array)
            area01Array[5] -> area02Array =
                resources.getStringArray(R.array.member_area_gwangju_array)
            area01Array[6] -> area02Array =
                resources.getStringArray(R.array.member_area_daegu_array)
            area01Array[7] -> area02Array =
                resources.getStringArray(R.array.member_area_daejeon_array)
            area01Array[8] -> area02Array =
                resources.getStringArray(R.array.member_area_busan_array)
            area01Array[9] -> area02Array =
                resources.getStringArray(R.array.member_area_sejong_array)
            area01Array[10] -> area02Array =
                resources.getStringArray(R.array.member_area_ulsan_array)
            area01Array[11] -> area02Array =
                resources.getStringArray(R.array.member_area_incheon_array)
            area01Array[12] -> area02Array =
                resources.getStringArray(R.array.member_area_jeonnam_array)
            area01Array[13] -> area02Array =
                resources.getStringArray(R.array.member_area_jeonbuk_array)
            area01Array[14] -> area02Array =
                resources.getStringArray(R.array.member_area_jeju_array)
            area01Array[15] -> area02Array =
                resources.getStringArray(R.array.member_area_chungnam_array)
            area01Array[16] -> area02Array =
                resources.getStringArray(R.array.member_area_chungbuk_array)
            area01Array[17] -> area02Array =
                resources.getStringArray(R.array.member_area_foreign_array)
            else -> Log.d("warning", "area name is not found")
        }
        return area02Array
    }

    fun transformDateTime(date: String): Array<Int> {
        date.let {
            val dateTimeArr = date.split(" ")
            val dateArr = dateTimeArr[0].split("-")
            val timeArr = dateTimeArr[1].split(":")
            val resArr = arrayOf(
                dateArr[0].toInt(),
                dateArr[1].toInt(),
                dateArr[2].toInt(),
                timeArr[0].toInt(),
                timeArr[1].toInt(),
                timeArr[2].toInt()
            )

            return resArr
        }

    }


}
