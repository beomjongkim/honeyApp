package com.dmonster.darling.honey.block_friends.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.common.command.TwoBtnSwitch
import com.dmonster.darling.honey.common.viewmodel.TwoBtnSwitchVM
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility

abstract class BlockFriendsVM(val id: String, val isInProcessJoin : Boolean = false) : ViewModel() {

    var toolbar_title = MutableLiveData<String>().also {
        it.value = ""
    }

    init {
        this.getContactInfo()
        toolbar_title.value = "차단할 지인을 선택해주세요"
    }

    var customPopup: CustomPopup? = null

    fun onClickButton(view: View) {
            showBlockContactPopup(view.context)
    }

    fun showBlockContactPopup(context: Context) {
        Utility.instance.showTwoButtonAlert(context,
            context.getString(R.string.noti_block_contact),
            context.resources.getString(R.string.noti_block_friends),
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    blockContact()
                }
                dialog.dismiss()
            }
        )

    }



    abstract fun getContactInfo()


    abstract fun blockContact()

    fun blockContactComplete(context: Context) {
        Utility.instance.showToast(context, context.getString(R.string.blocked_contact))

    }

    fun blockContactFail(context: Context) {}

    fun blockFacebookComplete(context: Context) {
        Utility.instance.showToast(context, context.getString(R.string.blocked_facebook))
    }

    fun blockFacebookFail(context: Context) {}

    fun dismissPopup() {
        customPopup?.dismiss()
    }

}
