package com.dmonster.darling.honey.test.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.databinding.ActivityTestBinding
import com.dmonster.darling.honey.test.viewmodel.TestViewmodel
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    lateinit var activityTestBinding : ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTestBinding = DataBindingUtil.setContentView(this,R.layout.activity_test)
        activityTestBinding.testViewModel = TestViewmodel()
    }
}
