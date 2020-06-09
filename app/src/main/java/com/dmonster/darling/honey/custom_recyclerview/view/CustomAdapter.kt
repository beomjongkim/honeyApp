package com.dmonster.darling.honey.custom_recyclerview.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData


 class CustomAdapter(var itemLayoutId: Int, var lifecycleOwner: LifecycleOwner? = null) : androidx.recyclerview.widget.RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val dataList = ArrayList<RecyclerItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        val viewDatabinding = DataBindingUtil.bind<ViewDataBinding>(v)
        return ViewHolder(viewDatabinding!!)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelId = dataList[position].viewModelID
        val viewModel = dataList[position].viewItemVM
        holder.viewDataBinding.setVariable(modelId, viewModel)
        lifecycleOwner?.let{
            holder.viewDataBinding.lifecycleOwner =it
        }
            holder.viewDataBinding.executePendingBindings()//데이터바인딩을 뷰에 저장해놓는 방식. setTag와 같음
    }


    class ViewHolder(val viewDataBinding: ViewDataBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(viewDataBinding.root)

}
