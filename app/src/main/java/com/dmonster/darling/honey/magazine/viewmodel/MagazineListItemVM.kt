package com.dmonster.darling.honey.magazine.viewmodel

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM

class MagazineListItemVM(
    override var title: String?,
    var platform: String?,
    var link: String?,
    var thumnail: String?
) : RecyclerViewItemVM(title) {

    var goTo: String = ""
    var drawableId: Int = -1
    var contentLink : String = ""

    init {
        setGoToWhere()
    }

    fun onClickItem(v: View) {
        var intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        v.context.startActivity(intent)
    }

    fun onClickContentLink(v:View){
        var intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(contentLink)
        v.context.startActivity(intent)
    }

    private fun setGoToWhere() {
        when (platform) {
            "유튜브" -> {
                goTo = "유튜브 가기"
                drawableId = R.drawable.youtube_icon
                contentLink = "https://www.youtube.com/channel/UCBdhv5jPPIvjUWWfpNHX6vQ"

            }
            "네이버 블로그" -> {
                goTo = "블로그 가기"
                drawableId = R.drawable.naver_icon
                contentLink = "https://blog.naver.com/shjagiya"
            }
            "티스토리 블로그" -> {
                goTo = "블로그 가기"
                drawableId = R.drawable.tstory_icon
                contentLink = "https://jjagiya.tistory.com/"
            }
            "페이스북" -> {
                goTo = "페이스북 가기"
                drawableId = R.drawable.com_facebook_favicon_blue
                contentLink = "https://www.facebook.com/%EC%9E%90%EA%B8%B0%EC%95%BC-112479286945365/?modal=admin_todo_tour"
            }
            "인스타그램" -> {
                goTo = "인스타그램 가기"
                drawableId = R.drawable.instargram_icon
                contentLink ="https://www.instagram.com/jagiya.app/"
            }
        }
    }

}