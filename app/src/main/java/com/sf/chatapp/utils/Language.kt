package com.sf.chatapp.utils

import androidx.annotation.StringRes
import com.sf.chatapp.R




enum class Language(@StringRes val languageName:Int,val locale: String) {
    DEFAULT(R.string.default_,"default"),
    ENGLISH(R.string.english,"en-US"),
    VIETNAM(R.string.vietnam,"vi-VN")
    ;

    companion object{
        fun findLanguageByLocale(locale: String): Language {
            return when(locale){
                Language.ENGLISH.locale-> Language.ENGLISH
                Language.VIETNAM.locale-> Language.VIETNAM
                else-> Language.DEFAULT
            }
        }
    }
}


