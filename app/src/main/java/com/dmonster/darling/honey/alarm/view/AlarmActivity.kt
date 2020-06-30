package com.dmonster.darling.honey.alarm.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.alarm.presenter.AlarmContract
import com.dmonster.darling.honey.alarm.presenter.AlarmPresenter
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.databinding.ActivityAlarmBinding
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_alarm.*
import java.util.concurrent.TimeUnit

class AlarmActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener,
    AlarmContract.View {


    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: AlarmContract.Presenter


    private var switchArray: Array<Switch>? = null
    private var buttons: Array<ImageButton>? = null
    private var agreed: Boolean = false
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_alarm)
        val binding : ActivityAlarmBinding = DataBindingUtil.setContentView(this,R.layout.activity_alarm)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle,this)
        binding.lifecycleOwner = this
        init()
        setListener()
    }

    private fun init() {
        ctb_act_alarm_toolbar.setTitle(resources.getString(R.string.main_more_menu_alarm))
        disposeBag = CompositeDisposable()

        switchArray = arrayOf(
            sw_act_alarm_all_on,
            sw_act_alarm_new_talk,
            sw_act_alarm_good,
            sw_act_alarm_new_member,
            sw_act_alarm_love_card,
            sw_act_alarm_open,
            sw_act_alarm_ad,
            sw_act_alarm_notice
        )
        buttons = arrayOf(ib_alarm_disagree, ib_alarm_agree)

        when (Utility.instance.UserData().getUserGender()) {
            "F" -> tv_act_alarm_good.text = resources.getString(R.string.alarm_interest)
            "M" -> tv_act_alarm_good.text = resources.getString(R.string.alarm_good)
        }

        mPresenter = AlarmPresenter()
        mPresenter.attachView(this)

        ll_act_alarm_progress.visibility = View.VISIBLE

        for (button in buttons!!) {
            if (Utility.instance.getPref(this, AppKeyValue.instance.alarm_event_agreed) == "true") {
                if (button.id == R.id.ib_alarm_disagree) {
                    agreed = true
                    button.setImageResource(R.drawable.switch_dot_on)
                    button.tag = "clicked"
                } else {
                    button.setImageResource(R.drawable.switch_dot_off)
                    button.tag = "unClicked"
                }
            } else {
                if (button.id == R.id.ib_alarm_disagree) {
                    button.setImageResource(R.drawable.switch_dot_off)
                    button.tag = "unClicked"
                } else {
                    button.setImageResource(R.drawable.switch_dot_on)
                    button.tag = "clicked"
                }
            }
        }

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mPresenter.getAlarmSetting(id)
    }

    private fun setListener() {
        switchArray?.let {
            for (checkedArray in it) {
                checkedArray.setOnCheckedChangeListener(this)
            }
        }
        buttons?.let {
            for (button in it) {
                button.setOnClickListener(this)
            }
        }
    }

    override fun onClick(v: View?) {
        (v as ImageButton).let {
            if (it.id == buttons?.get(0)?.id ?: Int) {
                Utility.instance.savePref(this, AppKeyValue.instance.alarm_event_agreed, "true")
            } else {
                Utility.instance.savePref(this, AppKeyValue.instance.alarm_event_agreed, "false")
            }
            if (it.tag == "clicked")
                return

            for (button in buttons!!) {

                if (button.tag == "clicked") {
                    button.tag = "unClicked"
                    button.setImageResource(R.drawable.switch_dot_off)
                } else {
                    button.tag = "clicked"
                    button.setImageResource(R.drawable.switch_dot_on)
                }
            }
        }
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Log.d("alarm", isChecked.toString())
        val switch = buttonView?.id?.let { findViewById<Switch>(it) }
        val viewGroup: ViewGroup = switch?.parent as ViewGroup

        setAlpha(viewGroup as RelativeLayout, isChecked)

        /*    알림설정    */
        disposeBag.add(RxView.clicks(ctb_act_alarm_toolbar.getLeftBtn())
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val all = if (sw_act_alarm_all_on.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val newTalk = if (sw_act_alarm_new_talk.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val good = if (sw_act_alarm_good.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val newMember = if (sw_act_alarm_new_member.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val loveCard = if (sw_act_alarm_love_card.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val notice = if (sw_act_alarm_notice.isChecked) {
                    "1"
                } else {
                    "0"
                }
                val open = if (sw_act_alarm_open.isChecked) {
                    "1"
                } else {
                    "0"
                }

                ll_act_alarm_progress.visibility = View.VISIBLE
                mPresenter.setAlarm(id, all, newTalk, good, newMember, loveCard, notice)
            })

        /*    모든 알림 켜기    */
        disposeBag.add(RxView.clicks(sw_act_alarm_all_on)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                switchArray?.let { it1 ->
                    if (sw_act_alarm_all_on.isChecked) {
                        for (i in 1 until it1.size) {
                            it1[i].isChecked = false
                        }
                    } else {
                        for (i in 1 until it1.size) {
                            it1[i].isChecked = true
                        }
                    }
                }
            })


        switchArray?.let {
            for (i in 1 until it.size) {
                if (!it[i].isChecked) {
                    sw_act_alarm_all_on.isChecked = false
                    return
                }
            }
        }

        sw_act_alarm_all_on.isChecked = true
    }

    /*    설정된 알림    */
    override fun getAlarmSetting(
        all: String?,
        newTalk: String?,
        good: String?,
        newMember: String?,
        loveCard: String?,
        notice: String?
    ) {
        sw_act_alarm_all_on.isChecked = all == "1"
        sw_act_alarm_new_talk.isChecked = newTalk == "1"
        sw_act_alarm_good.isChecked = good == "1"
        sw_act_alarm_new_member.isChecked = newMember == "1"
        sw_act_alarm_love_card.isChecked = loveCard == "1"
        sw_act_alarm_notice.isChecked = notice == "1"
        sw_act_alarm_ad.isChecked = true
        sw_act_alarm_open.isChecked = true

        setAlpha(rl_act_alarm_all_on, sw_act_alarm_all_on.isChecked)
        setAlpha(rl_act_alarm_new_talk, sw_act_alarm_new_talk.isChecked)
        setAlpha(rl_act_alarm_good, sw_act_alarm_good.isChecked)
        setAlpha(rl_act_alarm_new_member, sw_act_alarm_new_member.isChecked)
        setAlpha(rl_act_alarm_love_card, sw_act_alarm_love_card.isChecked)
        setAlpha(rl_act_alarm_notice, sw_act_alarm_notice.isChecked)
        setAlpha(rl_act_alarm_ad, sw_act_alarm_ad.isChecked)
        setAlpha(rl_act_alarm_open, sw_act_alarm_open.isChecked)

        ll_act_alarm_progress.visibility = View.GONE
    }

    private fun setAlpha(relativeLayout: RelativeLayout, isChecked: Boolean) {
        if (!isChecked) {
            relativeLayout.alpha = 0.5f
        } else {
            relativeLayout.alpha = 1f
        }
    }

    /*    알림 설정하기    */
    override fun setAlarmSettingComplete() {
        ll_act_alarm_progress.visibility = View.VISIBLE
        finish()
    }

    /*    알림 설정 호출실패    */
    override fun setAlarmSettingFailed(error: String?) {
        Utility.instance.showToast(this, error)
        ll_act_alarm_progress.visibility = View.VISIBLE
        finish()
    }

    override fun onBackPressed() {
        val all = if (sw_act_alarm_all_on.isChecked) {
            "1"
        } else {
            "0"
        }
        val newTalk = if (sw_act_alarm_new_talk.isChecked) {
            "1"
        } else {
            "0"
        }
        val good = if (sw_act_alarm_good.isChecked) {
            "1"
        } else {
            "0"
        }
        val newMember = if (sw_act_alarm_new_member.isChecked) {
            "1"
        } else {
            "0"
        }
        val loveCard = if (sw_act_alarm_love_card.isChecked) {
            "1"
        } else {
            "0"
        }
        val notice = if (sw_act_alarm_notice.isChecked) {
            "1"
        } else {
            "0"
        }
        val open = if (sw_act_alarm_open.isChecked) {
            "1"
        } else {
            "0"
        }

        ll_act_alarm_progress.visibility = View.VISIBLE
        mPresenter.setAlarm(id, all, newTalk, good, newMember, loveCard, notice)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
