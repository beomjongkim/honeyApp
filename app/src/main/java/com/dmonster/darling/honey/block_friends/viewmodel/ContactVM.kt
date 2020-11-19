package com.dmonster.darling.honey.block_friends.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.ArrayList

import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.block_friends.data.ContactData
import com.dmonster.darling.honey.block_friends.model.ContactModel
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.facebook.AccessToken

import io.reactivex.observers.DisposableObserver
import java.lang.StringBuilder
import com.facebook.GraphRequest
import com.dmonster.darling.honey.util.AppKeyValue
import org.json.JSONArray


abstract class ContactVM(val id: String, var customAdapter: CustomAdapter) : ViewModel() {

    var model = ContactModel()

    var phoneInfoMap = mutableMapOf<String, String>()

    var hpInfoList = ArrayList<ContactData>()

    var infoDoubleCheckMap = mutableMapOf<String, String>()

    var phoneNumberList = ""

    var contactItemList = ArrayList<ContactItemVM>()

    var accessToken: AccessToken? = null

    var fbIds = ""

    var isLoading = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var fbCountAnnounce = MutableLiveData<String>().also {
        it.value = "총 0명의 페이스북 친구가 자기야를 사용 중입니다."
    }


    var onCheckBoxChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            customAdapter.dataList.clear()
            for (item in contactItemList) {
                item.isChecked.value = isChecked
                customAdapter.dataList.add(RecyclerItemData(0, item, BR.contactItemVM))
            }

        }

    abstract fun checkReadContactPermission()

    @SuppressLint("Recycle")
    fun getContacts(mContext: Context) {
        isLoading.value = true
        /*
            Cursor
                This interface provides random read-write access to the result
                set returned by a database query.
        */
        /*
            ContactsContract
                The contract between the contacts provider and applications. Contains definitions
                for the supported URIs and columns. These APIs supersede ContactsContract.Contacts.
        */
        /*
            ContactsContract.CommonDataKinds
                Container for definitions of common data types stored in the ContactsContract.Data table.
        */
        /*
            ContactsContract.CommonDataKinds.Phone
                A data kind representing a telephone number.
        */
        /*
            CONTENT_URI
                The content:// style URI for all data records of the CONTENT_ITEM_TYPE MIME type,
                combined with the associated raw contact and aggregate contact data.
        */
        val contacts = mContext.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        contacts?.let {
            while (it.moveToNext()) {
                // Get the current contact name
                val name = it.getString(
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
                )

                // Get the current contact phone number
                var phoneNumber = it.getString(
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )

                if (phoneNumber.contains("+8210") && phoneNumber.length == 13) {
                    phoneNumber = phoneNumber.replace("+8210", "010")
                    phoneNumber = StringBuilder(phoneNumber).insert(7, "-").toString()
                    phoneNumber = StringBuilder(phoneNumber).insert(3, "-").toString()

                    phoneNumberList = "$phoneNumberList,$phoneNumber"
                } else if (phoneNumber.contains("010") && !phoneNumber.contains("-") && phoneNumber.length == 11) {
                    phoneNumber = StringBuilder(phoneNumber).insert(7, "-").toString()
                    phoneNumber = StringBuilder(phoneNumber).insert(3, "-").toString()

                    phoneNumberList = "$phoneNumberList,$phoneNumber"
                } else if (phoneNumber.contains("010") && phoneNumber.contains("-") && phoneNumber.length == 13) {
                    phoneNumberList = "$phoneNumberList,$phoneNumber"
                }
                phoneInfoMap[phoneNumber] = name
                // Display the contact to text view
                hpInfoList.add(ContactData().also {
                    it.mbHp = phoneNumber
                    it.mbNick = name
                })

            }
            hpInfoList.sortBy { data -> data.mbNick }
            makeListFromContactData(hpInfoList)
//            checkContact(id, phoneNumberList)
            it.close()
        }
    }


    fun blockFacebook(context: Context) {
        isLoading.value = true

        fbIds.let {
            model.blockMember(
                fbIds,
                id,
                object : DisposableObserver<BaseItem>() {
                    override fun onComplete() {
                        isLoading.value = false
                    }

                    override fun onNext(t: BaseItem) {
                        Utility.instance.showToast(context, t.message)

                    }

                    override fun onError(e: Throwable) {
                    }

                })
        }

    }

    fun blockContacts(context: Context) {
        isLoading.value = true

        var phoneNumberList = ""
        var itemCount = 0
        for (item in contactItemList) {
            if (item.isChecked.value!!) {
                itemCount++
                if (contactItemList.indexOf(item) == 0)
                    phoneNumberList = item.phoneNumber.toString()
                else
                    phoneNumberList += item.phoneNumber.toString()
            }
        }

        model.blockContacts(
            id,
            phoneNumberList,
            object : DisposableObserver<BaseItem>() {
                override fun onComplete() {
                    isLoading.value = false
                }

                override fun onNext(t: BaseItem) {
                    if (t.isSuccess) {
                        Utility.instance.showToast(
                            context,
                            "총 " + itemCount + "명의 회원을 " + t.message
                        )
                    } else {
                        Utility.instance.showToast(context, t.message)
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
    }


    fun makeListFromContactData(t: ArrayList<ContactData>) {
        customAdapter.dataList.clear()
        contactItemList.clear()
        infoDoubleCheckMap.clear()
        for (item in t) {
            if (!infoDoubleCheckMap.containsKey(item.mbHp!!)) {
                val contacItem =
                    ContactItemVM(phoneInfoMap[item.mbHp!!], item.mbHp, item.mbId)
                contactItemList.add(contacItem)
                customAdapter.dataList.add(
                    RecyclerItemData(
                        0,
                        contacItem,
                        BR.contactItemVM
                    )
                )
                infoDoubleCheckMap[item.mbHp!!] = item.mbNick!!
            }
        }
        customAdapter.notifyDataSetChanged()
        isLoading.value = false
    }

    fun getFacebookFriends(context: Context): Boolean {
        var isFacebookLoggedIn = false
        accessToken = AccessToken.getCurrentAccessToken()
        val id =
            Utility.instance.getPref(context, AppKeyValue.instance.savePrefID)
        if (id != null) {
            if (id.contains("facebook.com")) {
                val userId = id.replace("@facebook.com", "")
                accessToken?.let {
                    isFacebookLoggedIn = true
                    val request = GraphRequest.newGraphPathRequest(
                        it,
                        "/$userId/friends"
                    ) { response ->
                        response.jsonObject.let { jsonObj ->
                            if (!jsonObj.isNull("data")) {
                                val fbArray = jsonObj.getJSONArray("data")
                                makeListFromFacebookData(fbArray)
                            }
                        }
                    }

                    request.executeAsync()
                }

            }
        }

        return isFacebookLoggedIn
    }

    fun makeListFromFacebookData(fbArray: JSONArray) {

        customAdapter.dataList.clear()
        contactItemList.clear()
        infoDoubleCheckMap.clear()


        for (i in 0 until fbArray.length()) {
            if (i == 0) {
                fbIds = fbArray.getJSONObject(i).getString("id") + "@facebook.com"
            } else {
                fbIds += "," + fbArray.getJSONObject(i).getString("id") + "@facebook.com"
            }
        }

        fbCountAnnounce.value = "총 " + fbArray.length() + "명의 페이스북 친구가 자기야를 사용 중입니다."
    }

    abstract fun onClickBackButton()


}