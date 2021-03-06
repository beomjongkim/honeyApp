package com.dmonster.darling.honey.fcm

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.view.WebViewActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.disposables.CompositeDisposable

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var subscription: CompositeDisposable
    internal var remoteMessage: RemoteMessage? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        remoteMessage = p0
        remoteMessage?.data?.let {
            if (it.isNotEmpty()) {
                Log.d("CloudMessagingReceiver", it.toString())
                val title = it["title"]
                val message = it["body"]
                val link =it["link"]
                val notificationType = it["noti_type"]

                val roomNo = it["chat_room_no"]
                val senderId = it["chat_sender"]
                val receiveId = it["chat_receiver"]
                val talkMessage = it["chat_msg"]
                val talkImage = it["chat_img"]
                val talkType = it["chat_type"]
                val talkTime = it["chat_send_date"]
                val profileImage = it["mb_img_thumb"]

                val mbNo = it["mbNo"]
                val otherTalkId = it["mbNick"]

                val isAppTopRun = isAppTopRun(this, WebViewActivity::class.java.name.toString())
                val isActivityTop = isActivityTop(this, WebViewActivity::class.java.name.toString())

                setNotification(title, message, link)
                val myIntent = Intent("refresh")
                myIntent.putExtra("action", "aaa")
                this.sendBroadcast(myIntent)

            } else {

            }
        }
        Log.e("dddCheck", "fcm receive")
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("FCM_token", p0)
//        sendRegistrationToServer(p0)
//        subscription.clear()

    }
     private fun sendRegistrationToServer(token: String?) {
//         val model = IntroLoginModel()
//         subscription = CompositeDisposable()
//         val saveId = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
//         val savePassword = Utility.instance.getPref(this, AppKeyValue.instance.savePrefPassword)
//         val saveType = Utility.instance.getPref(this, AppKeyValue.instance.savePrefType)
//         if(saveId.isNotEmpty()&&savePassword.isNotEmpty()&&savePassword.isNotEmpty()){
//             val subscriber = object: DisposableObserver<ResultItem<IntroLoginData>>() {
//                 override fun onComplete() {
//                 }
//
//                 override fun onError(e: Throwable) {
//                     e.printStackTrace()
//                 }
//
//                 override fun onNext(item: ResultItem<IntroLoginData>) {
//                 }
//             }
//             model.requestIntroLogin(saveId,savePassword,token,saveType, subscriber)
//             subscription.add(subscriber)
//         }
        // Add custom implementation, as needed.
    }


    private fun setNotification(
        title: String?,
        messageBody: String?,
        link: String?
    ) {
        lateinit var intent: Intent
        lateinit var pendingIntent: PendingIntent
        lateinit var builder: NotificationCompat.Builder

        val notificationManager =
            getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        val isAppToRun = isAppTopRun(this, WebViewActivity::class.java.name.toString())

        intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("link", link)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


        pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification =
                Utility.instance.getPref(this, AppKeyValue.instance.notificationChannelId)
            if (TextUtils.isEmpty(notification)) {
                val channelMessage = NotificationChannel(
                    AppKeyValue.instance.notificationChannelId,
                    AppKeyValue.instance.notificationChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channelMessage.description = AppKeyValue.instance.notificationChannelDescription
                channelMessage.enableLights(true)
                channelMessage.lightColor = Color.GREEN
                channelMessage.enableVibration(true)
                channelMessage.vibrationPattern = longArrayOf(100, 200, 100, 200)
                channelMessage.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                notificationManager.createNotificationChannel(channelMessage)
                Utility.instance.savePref(
                    this,
                    AppKeyValue.instance.notificationChannelId,
                    AppKeyValue.instance.notificationChannelId
                )
            }
        }

        builder = NotificationCompat.Builder(this, resources.getString(R.string.app_channel_id))
            .setGroupSummary(true)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setGroup("honey")
            .setContentTitle(title)
            .setContentText(messageBody)
            .setVibrate(longArrayOf(1000, 1000))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setLights(Color.RED, 1, 1)
            .setChannelId(AppKeyValue.instance.notificationChannelId)


        notificationManager.notify(
            System.currentTimeMillis().toInt()/* ID of notification */,
            builder.build()
        )
    }


    /*    앱이 실행중인지 판단    */
    private fun isAppTopRun(context: Context, baseClassName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info: List<ActivityManager.RunningTaskInfo>?
        info = activityManager.getRunningTasks(1)
        if (info == null || info.isEmpty()) {
            return false
        }
        return info[0].baseActivity?.className == baseClassName
    }

    /*    채팅방이 실행중인지 판단    */
    private fun isActivityTop(context: Context, className: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info: List<ActivityManager.RunningTaskInfo>?
        info = activityManager.getRunningTasks(1)
        if (info == null || info.isEmpty()) {
            return false
        }
        return info[0].topActivity?.className == className
    }

}
