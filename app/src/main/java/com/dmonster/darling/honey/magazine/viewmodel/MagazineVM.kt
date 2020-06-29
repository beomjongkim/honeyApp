package com.dmonster.darling.honey.magazine.viewmodel

import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM

class MagazineVM(var recyclerAdapter : CustomAdapter) : ViewModel() {

    init {
        initAdapter()
    }

    private fun initAdapter(){
        recyclerAdapter.dataList.add(RecyclerItemData(0, MagazineListItemVM("blahblah","blahblah"),BR.magazineItemVM))
    }
}