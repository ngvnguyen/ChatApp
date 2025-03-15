package com.sf.chatapp.remote.model

sealed class Resource<T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error<T>(val message: String,val data:T?): Resource<T>()
    class Loading<T>(): Resource<T>()
}