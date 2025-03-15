package com.sf.chatapp.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.staticCompositionLocalOf
import es.dmoral.toasty.Toasty

class ToastManager(private val context: Context){
    fun showSuccessToast(message:String){
        Toasty.success(context,message).show()
    }
    fun showErrorToast(message:String){
        Toasty.error(context,message).show()
    }
    fun showInfoToast(message:String){
        Toasty.info(context,message).show()
    }
}

internal val LocalToastManager = staticCompositionLocalOf<ToastManager> {
    error("no provides yet")
}