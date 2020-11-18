package com.dmonster.darling.honey.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.widget.NestedScrollView
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility
import java.util.ArrayList

class CustomDialog: Dialog {

    private var activity: Activity? = null
    private var titleText: TextView? = null
    private var listLayout: LinearLayout? = null
    private var listScroll: NestedScrollView? = null
    private val onClick = View.OnClickListener { off() }

    internal var onCreate: Boolean = false

    constructor(activity: Activity, title: String?, items: ArrayList<String?>, onSelect: DialogSelect): super(activity/*, R.style.customDialogFull*/) {
        init(activity, title, items, onSelect)
    }

    constructor(activity: Activity, title: String?, items: Array<String?>, onSelect: DialogSelect): super(activity/*, R.style.customDialogFull*/) {
        val itemArray = ArrayList<String?>()
        for(i in items.indices) {
            items[i]?.let { itemArray.add(it) }
        }
        init(activity, title, itemArray, onSelect)
    }

    private fun init(activity: Activity, title: String?, items: ArrayList<String?>, onSelect: DialogSelect?) {
        this.activity = activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_dialog, null)

        titleText = view.findViewById<View>(R.id.tv_custom_dialog_title) as TextView
        if(TextUtils.isEmpty(title)) {
            titleText?.text = activity.resources?.getString(R.string.app_name)
        }
        else {
            titleText?.text = title
        }
        view.findViewById<View>(R.id.btn_custom_dialog_cancel).setOnClickListener(onClick)

        listScroll = view.findViewById<View>(R.id.nsv_custom_dialog_list) as NestedScrollView
        listLayout = view.findViewById<View>(R.id.ll_custom_dialog_list) as LinearLayout

        for(i in items.indices) {
            activity.let { it ->
                val layoutParams = LinearLayout.LayoutParams(-1, Utility.instance.getDip(it, R.dimen.default_margin_35dp))

                it.resources?.getDimension(R.dimen.default_margin_5dp)?.toInt()?.let { layoutParams.topMargin = it }
                it.resources?.getDimension(R.dimen.default_margin_10dp)?.toInt()?.let { layoutParams.leftMargin = it }
                it.resources?.getDimension(R.dimen.default_margin_10dp)?.toInt()?.let { layoutParams.rightMargin = it }

                val btn = Button(activity)
                btn.text = items[i]
                btn.setBackgroundResource(R.drawable.check_basic)
                btn.setTextColor(it.resources.getColorStateList(R.color.check_text_color))
                btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                btn.setPadding(0, 0, 0, 0)
                btn.setAllCaps(false)
                btn.layoutParams = layoutParams
                btn.setOnClickListener {
                    off()
                    items[i]?.let { it1 -> onSelect?.onSelect(i, it1) }
                }
                listLayout?.addView(btn)
            }
        }

        super.addContentView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(!onCreate) {
            val height = listScroll?.height
            val limitHeight = activity?.windowManager?.defaultDisplay?.height!! / 5 * 2
            if(height!! > limitHeight) {
                listScroll?.layoutParams = LinearLayout.LayoutParams(-1, limitHeight)
            }
            onCreate = true

//            listScroll?.height?.let { it ->
//                activity?.windowManager?.defaultDisplay?.height?.let{ it1 ->
//                    it1 / 5 * 2
//                    if(it > it1) {
//                        listScroll?.layoutParams = LinearLayout.LayoutParams(-1, it1)
//                    }
//                    onCreate = true
//                }
//            }
        }
    }

    private fun off() {
        super.dismiss()
    }

}
