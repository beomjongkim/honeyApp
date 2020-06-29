package com.dmonster.darling.honey.magazine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM

class MagazineListItemVM(override var title : String,var platform : String ) : RecyclerViewItemVM(title) {

    var buttonText = MutableLiveData<String>().also {
        it.value = "블로그 가기"
    }

}