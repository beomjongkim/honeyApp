package com.dmonster.darling.honey.block_friends.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.viewmodel.BlockFriendsVM
import com.dmonster.darling.honey.block_friends.viewmodel.ContactVM
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.databinding.ActivityBlockFriendsBinding
import com.dmonster.darling.honey.util.AppKeyValue
import android.Manifest.permission
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.common.command.TwoBtnSwitch
import com.dmonster.darling.honey.common.viewmodel.TwoBtnSwitchVM
import com.dmonster.darling.honey.util.Utility


class BlockFriendsActivity : AppCompatActivity() {
    private val REQUEST_ACCESS_READ = 1000
    lateinit var binding: ActivityBlockFriendsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_block_friends)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_friends)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle, this)
        binding.lifecycleOwner = this
        val mb_id = Utility.instance.getPref(
            this@BlockFriendsActivity,
            AppKeyValue.instance.savePrefID
        )

        binding.contactVM = object : ContactVM(mb_id, CustomAdapter(R.layout.item_contact, this)) {
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
        }
        binding.blockfriVM = object : BlockFriendsVM(mb_id, this) {
            override fun getFacebookFriendInfo(): Boolean {
                var result = false
                binding.contactVM?.let {
                    result = it.getFacebookFriends(this@BlockFriendsActivity)
                }
                return result
            }

            override fun blockContact() {
                binding.contactVM?.blockContacts(this@BlockFriendsActivity)
            }

            override fun blockFacebook() {
                binding.contactVM?.blockFacebook(this@BlockFriendsActivity)
            }

            override fun getContactInfo() {
                binding.contactVM?.checkReadContactPermission()
            }
        }

        binding.twoBtnSwitchVM = TwoBtnSwitchVM("연락처", "페이스북", object : TwoBtnSwitch {
            override fun firstBtnClicked() {
                binding.blockfriVM?.getContactInfo()
            }

            override fun secondBtnClicked() {
                val isFacebookLoggedIn = binding.blockfriVM?.getFacebookFriendInfo()

                isFacebookLoggedIn?.let {
                    if (!it) {
                        binding.twoBtnSwitchVM?.isSwitched?.value = false
                        Utility.instance.showToast(this@BlockFriendsActivity,getString(R.string.facebook_only))
                    }
                }
            }

        })

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

    override fun onBackPressed() {
        super.onBackPressed()
        binding.blockfriVM?.onClickBackButton(binding.ivActBlkFriendsBack)
    }

}
