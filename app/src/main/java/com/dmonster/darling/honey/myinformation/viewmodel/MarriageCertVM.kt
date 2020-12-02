package com.dmonster.darling.honey.myinformation.viewmodel

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.generated.callback.OnClickListener
import com.dmonster.darling.honey.myinformation.data.PictureMarryData
import com.dmonster.darling.honey.profile.view.ImageSimpleActivity
import com.kakao.util.helper.Utility
import gun0912.tedbottompicker.TedBottomPicker

class MarriageCertVM : ViewModel() {
    val imgUri = MutableLiveData<Uri?>()
    val pictureMarryData = MutableLiveData<PictureMarryData>()
    val isModifyMode = MutableLiveData<Boolean>()
    var del_mb_marry_img :String? = null
    private var mFragmentManager: FragmentManager? = null
    init {
        imgUri.value = null
        isModifyMode.value = false
     }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        mFragmentManager = fragmentManager
    }
    fun onClickImage(view:View){
        view.context?.let{
            if (isModifyMode.value!!) {
                com.dmonster.darling.honey.util.Utility.instance.showTwoButtonAlert(it, it.getString(R.string.app_name), it.getString(R.string.msg_app_select_delete), DialogInterface.OnClickListener { dialog, which ->
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        del_mb_marry_img = pictureMarryData.value?.mbImgNo?.get(0)
                        imgUri.value=null
                    }
                })
            }else{
                val intent = Intent(view.context,ImageSimpleActivity::class.java)
                intent.putExtra("imgUri",imgUri.value.toString())
                view.context.startActivity(intent)
            }
        }
    }

    fun onClickAddPic(view: View) {
        view.context?.let { it ->
            if (isModifyMode.value!!) {
                val tedBottomPicker = TedBottomPicker.Builder(it)
                    .setImageProvider { imageView, imageUri ->
                        Glide.with(it).load(imageUri).apply(RequestOptions().centerCrop()).into(imageView)
                    }
                    .setPreviewMaxCount(1000)
                    .setCameraTile(R.drawable.camera_shooting)
                    .setCameraTileBackgroundResId(R.color.color_black)
                    .setGalleryTile(R.drawable.camera_album)
                    .setGalleryTileBackgroundResId(R.color.color_black)
                    .setOnImageSelectedListener { uri ->
                        del_mb_marry_img = pictureMarryData.value?.mbImgNo?.get(0)
                        imgUri.value = uri }
                    .create()
                mFragmentManager?.let {
                    tedBottomPicker?.show(it)
                }
            }
        }
    }

}