package com.sf.chatapp.view

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sf.chatapp.R
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.view.settings.settingScreen


sealed class Screen(
    val route:String,
    @DrawableRes
    val iconRes: Int? = null,
    @StringRes
    val iconName: Int? = null
){
    data object Message:Screen("message",R.drawable.chat,R.string.message)

    data object ChatBot:Screen("chat_bot",R.drawable.robot,R.string.chat_bot)

    data object Settings:Screen("settings",R.drawable.settings,R.string.settings)

    data object LanguagePicker:Screen("language_picker")

    companion object {
        val navigationItems: List<Screen> = listOf(Message, ChatBot, Settings)
        val hideNavigationBarItem :List<Screen> = listOf()
    }
}

@Composable
fun NavHome(
    modifier: Modifier = Modifier
){
    var tapCounter by remember { mutableIntStateOf(0) }
    val activity = LocalActivity.current
    val context = LocalContext.current
    val toastManager = LocalToastManager.current
    val onBackPressed = @Composable {BackHandler {
        tapCounter++
        if(tapCounter==2) {
            activity?.moveTaskToBack(true)
            tapCounter = 0
        }else {
            toastManager.showInfoToast(context.getString(R.string.press_back_again_to_exit_app))
        }
    }}

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute !in Screen.hideNavigationBarItem.map { it.route })
                BottomNavigationBar(navController)
        },
        modifier = modifier
    ) { innerPadding->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.Message.route
        ){
            composable(Screen.Message.route) {
                Text("MESSAGE")
            }
            composable(Screen.ChatBot.route) {
                ChatBotScreen(
                    onBackPressed = onBackPressed,
                    ownerPaddingValues = innerPadding
                )
            }

            settingScreen(navController,onBackPressed)
        }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController,modifier: Modifier = Modifier){
    val items = Screen.navigationItems.toTypedArray()
    val context = LocalContext.current
    val useGestureNav = remember {
        val navigationMode = Settings.Secure.getInt(context.contentResolver,"navigation_mode",0)
        navigationMode == 2
    }

    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        NavigationBar(
            modifier = Modifier
                .height(dimensionResource(R.dimen.navigation_height))
                .then(if (useGestureNav) Modifier.navigationBarsPadding() else Modifier),
            containerColor = MaterialTheme.colorScheme.background,
            windowInsets = WindowInsets(0.dp),
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed {index , screen->
                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                painterResource(screen.iconRes!!),
                                contentDescription = null,
                                tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else LocalContentColor.current )
                            Text(
                                text = stringResource(screen.iconName!!),
                                textAlign = TextAlign.Center,
                                color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                fontSize = 12.sp
                            )
                        }


                    },
                    selected = false,
                    onClick = {navController.navigate(screen.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        selectedIndex = index
                    } },
                )
            }
        }

        if (!useGestureNav){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .background(MaterialTheme.colorScheme.surface))
        }
    }


}

//@Composable
//fun ChangeGestureNavColor(){
//    val act = LocalActivity.current
//    SideEffect {
//        act?.window?.navigationBarColor = Color.Transparent.toArgb()
//    }
//}