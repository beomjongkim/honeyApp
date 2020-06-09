package com.dmonster.darling.honey.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.customview.DialogSelect
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_selecter_member.*
import java.util.*
import java.util.concurrent.TimeUnit

class SelectorMemberDialog : Dialog {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mAdapter: SelectorMemberAdapter
    private lateinit var items: ArrayList<String?>

    private var activity: Activity? = null
    private var linearLayout: LinearLayout? = null
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var onSelect: DialogSelect? = null

    constructor(
        activity: Activity,
        type: String?,
        adapterType: String?,
        title: String?,
        items: ArrayList<String?>,
        onSelect: DialogSelect
    ) : super(activity) {
        init(activity, type, adapterType, title, items, onSelect)
        setListener()
    }

    constructor(
        activity: Activity,
        type: String?,
        adapterType: String?,
        title: String?,
        items: Array<String?>,
        onSelect: DialogSelect
    ) : super(activity) {
        val itemArray = ArrayList<String?>()
        for (i in items.indices) {
            items[i]?.let { itemArray.add(it) }
        }
        init(activity, type, adapterType, title, itemArray, onSelect)
        setListener()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val layoutHeight = linearLayout?.height
        layoutHeight?.let {
            var layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            var recyclerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            when {
                it > 2000 -> {
                    val recyclerHeight = setRecyclerViewHeight(2000)
//                    layoutParams =
//                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2000)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }

                it > 1800 -> {
                    val recyclerHeight = setRecyclerViewHeight(1800)
//                    layoutParams =
//                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1800)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }

                it > 1600 -> {
                    val recyclerHeight = setRecyclerViewHeight(1600)
//                    layoutParams =
//                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1600)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }

                it > 1400 -> {
                    val recyclerHeight = setRecyclerViewHeight(1400)
//                    layoutParams =
//                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1400)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }

                it > 1200 -> {
                    val recyclerHeight = setRecyclerViewHeight(1200)
//                    layoutParams =
//                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }

                it > 1000 -> {
                    val recyclerHeight = setRecyclerViewHeight(1000)
//                    layoutParams =
//                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000)
                    recyclerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        recyclerHeight
                    )
                }
            }
            linearLayout?.layoutParams = layoutParams
            recyclerView?.layoutParams = recyclerParams
        }
    }

    private fun init(
        activity: Activity,
        type: String?,
        adapterType: String?,
        title: String?,
        items: ArrayList<String?>,
        onSelect: DialogSelect?
    ) {
        this.activity = activity
        this.items = items
        this.onSelect = onSelect
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_selecter_member, null)

        linearLayout = view.findViewById<View>(R.id.ll_dlg_selector_member_layout) as LinearLayout
        recyclerView =
            view.findViewById<View>(R.id.rv_dlg_selector_member_list) as androidx.recyclerview.widget.RecyclerView

        val titleText = view.findViewById<View>(R.id.tv_dlg_selector_member_title) as TextView
        val contentText = view.findViewById<View>(R.id.tv_dlg_selector_member_content) as TextView

        disposeBag = CompositeDisposable()
        mAdapter = SelectorMemberAdapter(adapterType)

        when (type) {
            AppKeyValue.instance.memberSelectOne -> viewLayoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 1)
            AppKeyValue.instance.memberSelectTwo -> viewLayoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 2)
            AppKeyValue.instance.memberSelectThree -> viewLayoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 3)
        }

        when (adapterType) {
            AppKeyValue.instance.memberTypeNotLimit -> contentText.visibility = View.GONE

            AppKeyValue.instance.memberTypeLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_limit)
            }

            AppKeyValue.instance.memberTypeAgeLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_age_limit)
            }

            AppKeyValue.instance.memberTypeIncomeLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_income_limit)
            }

            AppKeyValue.instance.memberTypeEducationLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_education_limit)
            }

            AppKeyValue.instance.memberTypeReligionLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_religion_limit)
            }

            AppKeyValue.instance.memberTypeBloodLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_blood_limit)
            }

            AppKeyValue.instance.memberTypeMyStyleLimit -> {
                contentText.visibility = View.VISIBLE
                contentText.text =
                    activity.resources.getString(R.string.msg_popup_selector_member_mystyle_limit)
            }
        }

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: androidx.recyclerview.widget.RecyclerView,
                    state: androidx.recyclerview.widget.RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })
        }

        titleText.text = title

        mAdapter.data.clear()
        for (i in items.indices) {
            items[i]?.let { mAdapter.data.add(it) }

            val selectorMemberData = SelectorMemberData()
            selectorMemberData.isSelected = false
            mAdapter.dataSelected.add(selectorMemberData)
        }
        mAdapter.notifyDataSetChanged()

        super.addContentView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_selector_member_cancel)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                dismiss()
            })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_selector_member_enter)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (mAdapter.getSelectArray()?.size != 0) {
                    val position = mAdapter.getAdapterPosition()
                    mAdapter.getSelectArray()?.let { it1 -> onSelect?.onSelect(position, it1) }
                    dismiss()
                }
            })
    }

    private fun setRecyclerViewHeight(layoutHeight: Int): Int {
        val titleHeight = tv_dlg_selector_member_title.height
        val contentHeight = tv_dlg_selector_member_content.height
//        val btnLayoutHeight = ll_dlg_selector_member_btn_layout.height
        val btnLayoutHeight = 0
        val margin1 = Utility.instance.getPx(context, 10f)
        val margin2 = Utility.instance.getPx(context, 15f)
        val margin3 = Utility.instance.getPx(context, 20f)
        return layoutHeight - (titleHeight + contentHeight + btnLayoutHeight + margin1 + margin2 * 2 + margin3 + 2)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.clear()
    }

}
