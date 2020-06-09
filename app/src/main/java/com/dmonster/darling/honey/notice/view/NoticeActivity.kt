package com.dmonster.darling.honey.notice.view

import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.notice.presenter.NoticeContract
import com.dmonster.darling.honey.notice.presenter.NoticePresenter
import com.dmonster.darling.honey.notice.view.adapter.NoticeAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import kotlinx.android.synthetic.main.activity_notice.*

class NoticeActivity: BaseActivity(), NoticeContract.View {

    private lateinit var mPresenter: NoticeContract.Presenter
    private lateinit var mAdapter: NoticeAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        init()
    }

    private fun init() {
        ctb_act_notice_toolbar.setTitle(resources.getString(R.string.notice_title))
        mAdapter = NoticeAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_notice_list?.apply {
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

        ll_act_notice_progress.visibility = View.VISIBLE

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mPresenter = NoticePresenter()
        mPresenter.attachView(this)
        mPresenter.getNoticeList(id)
    }

    /*    공지사항 목록    */
    override fun setNoticeList(data: List<NoticeData>) {
        rv_act_notice_list.visibility = View.VISIBLE
        tv_act_notice_content.visibility = View.GONE

        mAdapter.data.clear()
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_notice_progress.visibility = View.GONE
    }

    /*    공지사항 목록 호출실패    */
    override fun setNoticeListFailed(error: String?) {
        rv_act_notice_list.visibility = View.GONE
        tv_act_notice_content.visibility = View.VISIBLE
        ll_act_notice_progress.visibility = View.GONE

        /*Utility.instance.showToast(this, error)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
