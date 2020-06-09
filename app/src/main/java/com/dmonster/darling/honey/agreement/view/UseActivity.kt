package com.dmonster.darling.honey.agreement.view

import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.agreement.data.UseData
import com.dmonster.darling.honey.agreement.presenter.UseContract
import com.dmonster.darling.honey.agreement.presenter.UsePresenter
import com.dmonster.darling.honey.agreement.view.adapter.UseAdapter
import com.dmonster.darling.honey.base.BaseActivity
import kotlinx.android.synthetic.main.activity_use.*

class UseActivity : BaseActivity(), UseContract.View {

    private lateinit var usePresenter: UseContract.Presenter
    private lateinit var useAdapter: UseAdapter

    private var viewLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use)

        init()
    }

    private fun init() {
        ctb_act_use_toolbar.setTitle(resources.getString(R.string.main_more_menu_use))

        useAdapter = UseAdapter()
        viewLayoutManager = LinearLayoutManager(this)
        rv_act_use_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = useAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })
        }

        ll_act_use_progress.visibility = View.VISIBLE

        usePresenter = UsePresenter()
        usePresenter.attachView(this)
        usePresenter.getUseList()
    }

    /*    이용방법    */
    override fun useListComplete(data: List<UseData>) {
        rv_act_use_list.visibility = View.VISIBLE
        tv_act_use_content.visibility = View.GONE

        useAdapter.data.clear()
        useAdapter.data.addAll(data)
        useAdapter.notifyDataSetChanged()

        ll_act_use_progress.visibility = View.GONE
    }

    /*    이용방법 호출실패    */
    override fun useListFailed(error: String?) {
        if(useAdapter.data.size == 0) {
            rv_act_use_list.visibility = View.GONE
            tv_act_use_content.visibility = View.VISIBLE
            ll_act_use_progress.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usePresenter.detachView()
    }
}
