package com.dmonster.darling.honey.base

interface BasePresenter<VIEW : BaseView> {
    fun attachView(view: VIEW)

    fun detachView()
}
