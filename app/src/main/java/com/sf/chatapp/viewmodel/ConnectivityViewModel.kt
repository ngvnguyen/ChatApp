package com.sf.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sf.chatapp.utils.ConnectivityObserve
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConnectivityViewModel(
    private val connectivityObserve: ConnectivityObserve
):ViewModel() {
    val isNetworkAvailable = connectivityObserve.isNetworkAvailable.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        true
    )

}