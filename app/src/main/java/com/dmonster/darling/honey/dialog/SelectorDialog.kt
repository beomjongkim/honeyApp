package com.dmonster.darling.honey.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
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
import kotlinx.android.synthetic.main.dialog_selecter.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class SelectorDialog: Dialog {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mAdapter: SelectorAdapter
    private lateinit var items: ArrayList<String?>

    private var activity: Activity? = null
    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var onSelect: DialogSelect? = null
    private var type: String? = null
    private var data: Array<String>? = null

    constructor(activity: Activity, type: String?, items: ArrayList<String?>, onSelect: DialogSelect) : super(activity) {
        init(activity, type, items, onSelect)
        setListener()
    }

    constructor(activity: Activity, type: String?, items: Array<String?>, onSelect: DialogSelect) : super(activity) {
        val itemArray = ArrayList<String?>()
        for(i in items.indices) {
            items[i]?.let { itemArray.add(it) }
        }
        init(activity, type, itemArray, onSelect)
        setListener()
    }

    private fun init(activity: Activity, type: String?, items: ArrayList<String?>, onSelect: DialogSelect?) {
        this.activity = activity
        this.type = type
        this.items = items
        this.onSelect = onSelect
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_selecter, null)

        val titleText = view.findViewById<View>(R.id.tv_selector_com_title) as TextView
        val contentText = view.findViewById<View>(R.id.tv_dlg_selector_content) as TextView
        val list = view.findViewById<View>(R.id.rv_dlg_selector_list) as androidx.recyclerview.widget.RecyclerView

        disposeBag = CompositeDisposable()
        mAdapter = SelectorAdapter(type)
        when(type) {
            AppKeyValue.instance.searchTypeArea, AppKeyValue.instance.searchTypeGender -> viewLayoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 3)
            AppKeyValue.instance.searchTypeAge -> viewLayoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        }
        list.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })
        }

        var dlgTitle: String? = null
        var dlgContent: String? = null
        when(type) {
            AppKeyValue.instance.searchTypeArea -> {
                dlgTitle = activity.resources.getString(R.string.popup_selector_area_title)
                dlgContent = activity.resources.getString(R.string.popup_selector_area_content)
            }

            AppKeyValue.instance.searchTypeGender -> {
                dlgTitle = activity.resources.getString(R.string.popup_selector_gender_title)
                dlgContent = activity.resources.getString(R.string.popup_selector_gender_content)
            }

            AppKeyValue.instance.searchTypeAge -> {
                dlgTitle = activity.resources.getString(R.string.popup_selector_age_title)
                dlgContent = activity.resources.getString(R.string.popup_selector_age_content)
            }
        }
        titleText.text = dlgTitle
        contentText.text = dlgContent

        mAdapter.data.clear()
        for(i in items.indices) {
            items[i]?.let { mAdapter.data.add(it) }
        }
        mAdapter.notifyDataSetChanged()

        /*    선택    */
        mAdapter.itemClick = View.OnClickListener {
            val position = it.tag.toString().toInt()
            if(position == 0) {
                onSelect?.onSelect(0, "")
                dismiss()
            }
        }

        super.addContentView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_selector_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    mAdapter.setSelectedClear()
                    mAdapter.notifyDataSetChanged()
                    dismiss()
                })

        /*    검색    */
        disposeBag.add(RxView.clicks(tv_dlg_selector_search)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(mAdapter.getSelectArray()?.size == 0) {
                        var message: String? = null
                        when(type) {
                            AppKeyValue.instance.searchTypeArea -> message = activity?.resources?.getString(R.string.msg_popup_selector_area)
                            AppKeyValue.instance.searchTypeGender -> message = activity?.resources?.getString(R.string.msg_popup_selector_gender)
                            AppKeyValue.instance.searchTypeAge -> message = activity?.resources?.getString(R.string.msg_popup_selector_age)
                        }
                        Utility.instance.showToast(context, message)
                    }
                    else {
                        mAdapter.getSelectArray()?.let { it1 -> onSelect?.onSelect(0, it1) }
                        dismiss()
                    }
                })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.clear()
    }

}
