package com.dmonster.darling.honey.myinformation.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.myinformation.data.RecommendData
import com.dmonster.darling.honey.myinformation.presenter.RecommendContract
import com.dmonster.darling.honey.myinformation.presenter.RecommendPresenter
import com.dmonster.darling.honey.myinformation.view.adapter.RecommendAdapter
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_recommend.*

class RecommendActivity : BaseActivity(), RecommendContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: RecommendContract.Presenter
    private lateinit var mAdapter: RecommendAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var recommend: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_recommend_toolbar.setTitle(resources.getString(R.string.information_member_recommend))
        disposeBag = CompositeDisposable()
        mAdapter = RecommendAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_recommend_list?.apply {
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

        ll_act_recommend_progress.visibility = View.VISIBLE

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        recommend = Utility.instance.UserData().getUserRecommend()

        tv_act_recommend_number.text = String.format(resources.getString(R.string.information_member_recommend_number), recommend)

        mPresenter = RecommendPresenter()
        mPresenter.attachView(this)
        mPresenter.getRecommendList(false, id, AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt)
    }

    private fun setListener() {
        /*    새로고침    */
        srl_act_recommend_layout.setOnRefreshListener {
            mPresenter.getRecommendList(false, id, AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt)
        }

        rv_act_recommend_list.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_act_recommend_layout.isEnabled = cl_act_recommend_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if(lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {
                    mPresenter.getRecommendList(true, id, lastVisibleItemPosition.toString(), AppKeyValue.instance.listLimitCnt)
                }
            }
        })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val mbNo = mAdapter.data[position].mbNo

        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
        startActivity(intent)
    }

    /*    추천인 목록    */
    override fun setRecommendList(isScroll: Boolean, data: List<RecommendData>) {
        rv_act_recommend_list.visibility = View.VISIBLE
        tv_act_recommend_content.visibility = View.GONE
        srl_act_recommend_layout.isRefreshing = false

        if(!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_recommend_progress.visibility = View.GONE
    }

    /*    추천인 목록 호출실패    */
    override fun setRecommendListFailed(error: String?) {
        if(mAdapter.data.size == 0) {
            rv_act_recommend_list.visibility = View.GONE
            tv_act_recommend_content.visibility = View.VISIBLE
            ll_act_recommend_progress.visibility = View.GONE
            srl_act_recommend_layout.isRefreshing = false

            /*Utility.instance.showToast(this, error)*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
