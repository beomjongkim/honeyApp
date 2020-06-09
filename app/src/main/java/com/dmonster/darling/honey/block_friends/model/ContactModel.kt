package com.dmonster.darling.honey.block_friends.model

import com.dmonster.darling.honey.block_friends.data.ContactData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ContactModel {

    fun blockContacts(
        id: String?, contacts: String?, subscriber: DisposableObserver<BaseItem>
    ) {
        RetrofitProtocol().retrofit.checkContact(ServerApi.instance.blockContact, id, contacts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun checkFacebookFriends(
        id: String?, fbIds: String?, subscriber: DisposableObserver<ResultListItem<ContactData>>
    ) {
        RetrofitProtocol().retrofit.checkFacebookFriends(ServerApi.instance.blockContact, id, fbIds)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun blockMember(ids: String?, mb_id : String?,subscriber: DisposableObserver<BaseItem>) {
        RetrofitProtocol().retrofit.blockIds(ServerApi.instance.blockFacebook, ids, mb_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}