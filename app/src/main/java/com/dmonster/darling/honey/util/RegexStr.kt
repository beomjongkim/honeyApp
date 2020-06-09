package com.dmonster.darling.honey.util

class RegexStr {

    companion object {
        val instance: RegexStr
            get() = Singleton.instance
    }

    private object Singleton {
        val instance = RegexStr()
    }

    val birthYear = "^(19[0-9][0-9]|20\\d{2})"
    val fourDigit = "\\d{4}"
    val overOneDigit ="\\d{1}|\\d{2}"
}