package com.sf.chatapp.module

import com.sf.chatapp.remote.repository.ChatBotRepository
import com.sf.chatapp.remote.repository.GeminiRepository
import com.sf.chatapp.remote.repository.GeminiRepositoryImpl
import com.sf.chatapp.remote.retrofit.RetrofitInstance
import com.sf.chatapp.utils.ConnectivityObserve
import com.sf.chatapp.viewmodel.ChatBotViewModel
import com.sf.chatapp.viewmodel.ConnectivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single<GeminiRepository> { GeminiRepositoryImpl(RetrofitInstance().geminiApi) }
    single<ConnectivityObserve> { ConnectivityObserve.get(androidApplication())}
    single<ChatBotRepository> {ChatBotRepository(get())}
    viewModel { ChatBotViewModel(get())  }
    viewModel {ConnectivityViewModel(get())}
}