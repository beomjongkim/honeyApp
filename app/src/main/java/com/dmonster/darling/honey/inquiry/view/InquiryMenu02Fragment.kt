package com.dmonster.darling.honey.inquiry.view

import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.inquiry.data.InquiryData
import com.dmonster.darling.honey.inquiry.presenter.BreakdownContract
import com.dmonster.darling.honey.inquiry.presenter.BreakdownPresenter
import com.dmonster.darling.honey.inquiry.view.adapter.InquiryAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import kotlinx.android.synthetic.main.fragment_inquiry_breakdown.*

class InquiryMenu02Fragment: BaseFragment(), BreakdownContract.View {

    private lateinit var mPresenter: BreakdownContract.Presenter
    private lateinit var mAdapter: InquiryAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inquiry_breakdown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        mAdapter = InquiryAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_inquiry_breakdown_list?.apply {
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

        ll_frag_inquiry_breakdown_progress.visibility = View.VISIBLE

        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        mPresenter = BreakdownPresenter()
        mPresenter.attachView(this)
        mPresenter.getInquiryList(id)
    }

    /*    문의내역    */
    override fun setInquiryList(data: List<InquiryData>) {
        rv_frag_inquiry_breakdown_list.visibility = View.VISIBLE
        tv_frag_inquiry_breakdown_content.visibility = View.GONE

        mAdapter.data.clear()
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_frag_inquiry_breakdown_progress.visibility = View.GONE
    }

    /*    문의내역 호출실패    */
    override fun setInquiryListFailed(error: String?) {
        rv_frag_inquiry_breakdown_list.visibility = View.GONE
        tv_frag_inquiry_breakdown_content.visibility = View.VISIBLE
        ll_frag_inquiry_breakdown_progress.visibility = View.GONE

        /*context?.let { Utility.instance.showToast(it, error) }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
    }

}
