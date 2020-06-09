package com.dmonster.darling.honey.question.view

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
import com.dmonster.darling.honey.question.data.QuestionData
import com.dmonster.darling.honey.question.presenter.QuestionContract
import com.dmonster.darling.honey.question.presenter.QuestionPresenter
import com.dmonster.darling.honey.question.view.adapter.QuestionAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_notice.*

class QuestionActivity : BaseActivity(), QuestionContract.View {

    private lateinit var mPresenter: QuestionContract.Presenter
    private lateinit var mAdapter: QuestionAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        init()
    }

    private fun init() {
        ctb_act_notice_toolbar.setTitle(resources.getString(R.string.main_more_menu_question))
        mAdapter = QuestionAdapter()
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

        mPresenter = QuestionPresenter()
        mPresenter.attachView(this)
        mPresenter.getQuestionList()
    }

    /*    자주묻는 질문 목록    */
    override fun setQuestionList(data: List<QuestionData>) {
        rv_act_notice_list.visibility = View.VISIBLE
        tv_act_notice_content.visibility = View.GONE

        mAdapter.data.clear()
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_notice_progress.visibility = View.GONE
    }

    /*    자주묻는 질문 목록 호출실패    */
    override fun setQuestionListFailed(error: String?) {
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
