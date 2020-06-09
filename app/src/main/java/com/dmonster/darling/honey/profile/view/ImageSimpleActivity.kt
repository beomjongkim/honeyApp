package com.dmonster.darling.honey.profile.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.databinding.ActivityImageSimpleBinding
import com.dmonster.darling.honey.profile.viewmodel.ImageSimpleVM

class ImageSimpleActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityImageSimpleBinding
    private  var imgUri : Uri? =null
    private  var nickname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgUri  = Uri.parse(intent.getStringExtra("imgUri"))
        setViewModel()
    }

    private fun setViewModel(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_image_simple)
        binding.simpleVM = ImageSimpleVM(imgUri)
        binding.lifecycleOwner = this
    }
}
