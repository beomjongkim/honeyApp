package com.dmonster.darling.honey.block_friends.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.viewmodel.BlockFriendsVM
import com.dmonster.darling.honey.block_friends.viewmodel.ContactVM
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.databinding.ActivityBlockFriendsBinding
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility


class BlockFriendsActivity : AppCompatActivity() {
    private val REQUEST_ACCESS_READ = 1000
    lateinit var binding: ActivityBlockFriendsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_block_friends)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_friends)

        val mb_id =     Utility.instance.getPref(applicationContext, AppKeyValue.instance.savePrefID)
        mb_id?.let{
            binding.bannerVM =
                BannerVM(
                    it,
                    lifecycle,
                    this
                )
            binding.contactVM = @SuppressLint("StaticFieldLeak")
            object : ContactVM(it, CustomAdapter(R.layout.item_contact, this)) {
                override fun checkReadContactPermission() {
                    // OS가 Marshmallow 이상일 경우 권한체크를 해야 합니다.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            this@BlockFriendsActivity,
                            permission.READ_CONTACTS
                        )
                        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                            // 권한 없음
                            ActivityCompat.requestPermissions(
                                this@BlockFriendsActivity,
                                arrayOf(permission.READ_CONTACTS),
                                REQUEST_ACCESS_READ
                            )

                        } else {
                            // ACCESS_FINE_LOCATION 에 대한 권한이 이미 있음.
                            getContacts(this@BlockFriendsActivity)
                        }
                    } else {
                        getContacts(this@BlockFriendsActivity)
                    }// OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
                }

                override fun onClickBackButton() {
                    onBackPressed()
                }
            }

            binding.blockfriVM =
                object : BlockFriendsVM(it, intent.getBooleanExtra("fromJoinActivity", false)) {


                    override fun blockContact() {
                        binding.contactVM?.blockContacts(this@BlockFriendsActivity)
                    }

                    override fun showBlockContactPopup() {
                        Utility.instance.showTwoButtonAlert(this@BlockFriendsActivity,
                            getString(R.string.noti_block_contact),
                            getString(R.string.noti_block_friends),
                            DialogInterface.OnClickListener { dialog, which ->
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    blockContact()
                                }
                                dialog.dismiss()
                            }
                        )
                    }


                    override fun getContactInfo() {
                        binding.contactVM?.checkReadContactPermission()
                    }
                }

        }

        binding.lifecycleOwner = this

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.contactVM?.getContacts(this)
        } else {
            Utility.instance.showToast(this, "")
        }
    }

}
