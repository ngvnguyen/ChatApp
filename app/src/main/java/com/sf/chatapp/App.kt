package com.sf.chatapp

import android.app.Application
import com.sf.chatapp.module.appModule
import com.sf.chatapp.remote.repository.GeminiRepository
import com.sf.chatapp.remote.repository.GeminiRepositoryImpl
import com.sf.chatapp.remote.retrofit.RetrofitInstance
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import retrofit2.Retrofit

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}