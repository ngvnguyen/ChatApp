package com.sf.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.sf.chatapp.ui.theme.ChatAppTheme
import com.sf.chatapp.utils.LocalSession
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.utils.Session
import com.sf.chatapp.view.AppLogin
import com.sf.chatapp.view.NavHome
import com.sf.chatapp.viewmodel.ConnectivityViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("89D48DB7EB6FA963A3843B3FE8C8B386"))
                .build()
        )
        setContent {
            ChatAppTheme() {
                val connectivityViewModel = koinViewModel<ConnectivityViewModel>()
                val toastManager = LocalToastManager.current
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    var flag = true
                    connectivityViewModel.isNetworkAvailable.collectLatest {

                        if (it.not()) toastManager.showInfoToast(context.getString(R.string.no_internet_connection))
                        if (it){
                            if (flag) flag = false
                            else toastManager.showInfoToast(context.getString(R.string.internet_connection_restored))
                        }
                    }
                }


                val firebaseAuth = FirebaseAuth.getInstance()
                if (firebaseAuth.currentUser != null) LocalSession.current.value = Session(true)


                Surface(modifier = Modifier.fillMaxSize()) {
                    CompositionLocalProvider(LocalSession provides LocalSession.current) {
                        val session = LocalSession.current.value
                        if (session.hasSession){
                            NavHome()
                        }else{
                            AppLogin()
                        }
                    }

                }
            }
        }
    }
}

