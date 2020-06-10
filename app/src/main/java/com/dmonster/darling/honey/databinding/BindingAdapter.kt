package com.dmonster.darling.honey.databinding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.banner.viewmodel.BannerVM
import com.dmonster.darling.honey.common.viewmodel.BaseEditRegexVM
import com.dmonster.darling.honey.common.viewmodel.SpinnerVM
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.customview.ClickableImageView
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.main.view.adapter.MainAdapter
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView


object BindingAdapter {

    @androidx.databinding.BindingAdapter("setMainDataList", "setMainScrollListener", "isScroll")
    @JvmStatic
    fun setData(
        view: RecyclerView,
        liveData: MutableLiveData<ResultListItem<MainListData>>,
        scrollListener: RecyclerView.OnScrollListener,
        isScroll: Boolean
    ) {
        liveData.value?.let {
            val mList = ArrayList<MainListData>()
            it.items?.let { it1 -> mList.addAll(it1) }
            (view.adapter as MainAdapter).also { it1 ->
                isScroll.let { it2 ->
                    if (!it2) {
                        it1.data.clear()
                    }
                }
                it1.data.addAll(mList)
                it1.notifyDataSetChanged()
            }
        }
        view.addOnScrollListener(scrollListener)
    }

    @androidx.databinding.BindingAdapter("srlRefreshListener", "isRefreshing")
    @JvmStatic
    fun setOnRefreshListener(
        view: SwipeRefreshLayout,
        refreshListener: SwipeRefreshLayout.OnRefreshListener,
        isRefreshing: MutableLiveData<Boolean>
    ) {
        view.setOnRefreshListener(refreshListener)
        isRefreshing.value?.let {
            view.isRefreshing = it
        }
    }

    @androidx.databinding.BindingAdapter("radioDistanceChecked")
    @JvmStatic
    fun setAdapter(view: RecyclerView, radioDistanceChecked: MutableLiveData<Boolean>) {

        radioDistanceChecked.value?.let {
            if (it)
                view.visibility = View.GONE
            else
                view.visibility = View.VISIBLE
        }
    }


    @androidx.databinding.BindingAdapter("CustomAdapter", "layoutType")
    @JvmStatic
    fun setAdapter(view: RecyclerView, adapter: CustomAdapter, type: String) {
        adapter.dataList.let {

            view.adapter = adapter
            when (type) {
                "grid" -> view.layoutManager =
                    GridLayoutManager(view.context, 3) as RecyclerView.LayoutManager?
                "vertical" -> view.layoutManager = LinearLayoutManager(view.context)

                else -> view.layoutManager = LinearLayoutManager(view.context)
            }
            view.setHasFixedSize(false)


        }

    }


    @androidx.databinding.BindingAdapter("onCheckedChangeListener")
    @JvmStatic
    fun setImageView(view: RadioGroup, listener: RadioGroup.OnCheckedChangeListener) {
        view.setOnCheckedChangeListener(listener)
    }


    @androidx.databinding.BindingAdapter("onCheckBoxChangedAll")
    @JvmStatic
    fun setImageView(view: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        view.setOnCheckedChangeListener(listener)
    }


    @androidx.databinding.BindingAdapter("imgGlideSrc")
    @JvmStatic
    fun setImageView(view: ImageView, id: Int?) {
        Glide.with(view.context).load(id)
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_color_white))
            .into(view)
    }

    @androidx.databinding.BindingAdapter("imgGlideUrl")
    @JvmStatic
    fun setImageView(view: ImageView, path: String?) {
        Glide.with(view.context).load(path)
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_color_white))
            .into(view)
    }

    @androidx.databinding.BindingAdapter("imgGlideCircleUrl")
    @JvmStatic
    fun setImageCircleView(view: ImageView, path: String?) {
        Glide.with(view.context).load(path)
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_circle_white).circleCrop())
            .into(view)
    }

    @androidx.databinding.BindingAdapter("liveUrlCircleImg")
    @JvmStatic
    fun setImageView(view: ImageView, path: MutableLiveData<String>) {
        Glide.with(view.context).load(path.value)
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_circle_white).circleCrop())
            .thumbnail(0.5f)
            .into(view)
    }


    @androidx.databinding.BindingAdapter("imgGlideUri")
    @JvmStatic
    fun setImageView(view: ImageView, path: Uri?) {
        Glide.with(view.context).load(path)
            .apply(RequestOptions().centerInside().placeholder(R.drawable.bg_color_white))
            .into(view)
    }

    @androidx.databinding.BindingAdapter("imgGlideUriWithoutPlaceHolder")
    @JvmStatic
    fun setImage(view: ImageView, path: Uri?) {
        Glide.with(view.context).load(path)
            .apply(RequestOptions().centerInside())
            .into(view)
    }

    @androidx.databinding.BindingAdapter("imgGlideUriSimple")
    @JvmStatic
    fun setImageViewSimple(view: ImageView, mPath: Uri?) {
        Glide.with(view.context).load(mPath)
            .apply(RequestOptions().placeholder(R.drawable.bg_color_white))
            .into(view)
    }

    @androidx.databinding.BindingAdapter("imgGlideSrcSimple")
    @JvmStatic
    fun setImageViewSimple(view: ImageView, mPath: Int?) {
        Glide.with(view.context).load(mPath)
            .apply(RequestOptions().placeholder(R.drawable.bg_color_white))
            .into(view)
    }

    @androidx.databinding.BindingAdapter("BaseEditRegexVM")
    @JvmStatic
    fun setEditRegexVM(editText: EditText, baseEditRegexVM: BaseEditRegexVM) {
        baseEditRegexVM.setTextWatcher(editText)
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                editText.text = null
            }
        }
    }

    @androidx.databinding.BindingAdapter("SpinnerVM")
    @JvmStatic
    fun setSpinnerVM(spinner: Spinner, spinnerVM: SpinnerVM?) {
        //viewModel을 통해서 가져온 adapter를 스피너에 세팅해주고, 스피너와 에딧텍스트의 기본적인 행동을 정의해준다.
        //ex)아이템 클릭 시 텍스트뷰에 세팅해주고 아무것도 선택안하면 초기화해주는 것...

        spinner.adapter = spinnerVM?.arrayAdapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

                spinnerVM?.let {
                    it.text.value = ""
                }
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerVM?.let {
                    it.text.value = it.arrayAdapter.getItem(position)
                }
            }
        }
    }

    @androidx.databinding.BindingAdapter("UpperSpinner", "UpperSpinnerVM", "LowerSpinnerVM")
    @JvmStatic
    fun setLowerSpinnerVM(
        lowerSpinner: Spinner,
        upperSpinner: Spinner,
        upperSpinnerVM: SpinnerVM?,
        lowerSpinnerVM: SpinnerVM?
    ) {
        //상위의 에딧 텍스트 값이 변화하면 그에 따라 세팅해주는 어레이를 변경
        //어레이 변경 부분은 따로 모델을 만들던지 하자.
        upperSpinner.setOnTouchListener { v, event ->
            upperSpinner.adapter = upperSpinnerVM?.arrayAdapter
            false
        }

        upperSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

                upperSpinnerVM?.let {
                    it.text.value = ""
                    it.passed.value = false
                }
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var area = ""
                upperSpinnerVM?.let {
                    it.passed.value = true

                    it.text.value = it.arrayAdapter.getItem(position)

                    area = upperSpinnerVM.arrayAdapter.getItem(position)
                }
                val subAreaArray: Array<String?> =
                    Utility.instance.getSubArrayArea(lowerSpinner.context, area)
                lowerSpinnerVM?.arrayAdapter?.let {
                    lowerSpinnerVM.text.value = ""
                    lowerSpinnerVM.passed.value = false
                    it.clear()
                    it.addAll(subAreaArray.toList())
                }
            }
        }


        lowerSpinner.adapter = lowerSpinnerVM?.arrayAdapter

        lowerSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                lowerSpinnerVM?.let {
                    it.text.value = ""
                    it.passed.value = false
                }
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lowerSpinnerVM?.let {
                    it.text.value = it.arrayAdapter.getItem(position)
                    it.passed.value = true
                }
            }

        }

    }

    @androidx.databinding.BindingAdapter("textColorID")
    @JvmStatic
    fun setFontColor(textView: TextView, textColorID: Int) {
        textView.setTextColor(textColorID)
    }

    @androidx.databinding.BindingAdapter("isTintSelected")
    @JvmStatic
    fun setTintColor(imageView: ImageView, isTintGray: MutableLiveData<Boolean>) {
        isTintGray.value?.let {
            if (it) {
                imageView.clearColorFilter()
            } else {
                imageView.setColorFilter(
                    ContextCompat.getColor(
                        imageView.context,
                        R.color.color_text_gray
                    )
                )
            }
        }
    }

    @androidx.databinding.BindingAdapter("srcClickable")
    @JvmStatic
    fun setClickableImageView(imageView: ClickableImageView, drawable :Drawable){
        Glide.with(imageView.context).load(drawable)
            .thumbnail(0.3f)
            .into(imageView)
    }

    @androidx.databinding.BindingAdapter("setAdListener")
    @JvmStatic
    fun setAdView(adView: BannerAdView, adListener: AdListener) {
        adView.setClientId("DAN-1jenrlqxtcu9k")
        adView.setAdListener(adListener)
        adView.loadAd()
    }

}