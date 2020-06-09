package com.dmonster.darling.honey.test.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        btn_test.setOnClickListener {
            com.dmonster.darling.honey.util.Utility.instance.showTwoButtonAlert(
                this,
                "지인차단",
                "나를 알수도 있는 회원을 차단할 수 있습니다. 차단하시겠습니까?",
                DialogInterface.OnClickListener { dialog, which ->
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        startActivity(
                            Intent(this@TestActivity, BlockFriendsActivity::class.java)
                        )
                    }
                    dialog.dismiss()
                })

        }
    }
}
