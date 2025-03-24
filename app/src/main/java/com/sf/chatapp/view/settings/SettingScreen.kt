package com.sf.chatapp.view.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.sf.chatapp.R
import com.sf.chatapp.utils.LocalSession
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.view.Screen


fun NavGraphBuilder.settingScreen(
    navController: NavController,
    onBackPressed: @Composable ()->Unit
){

    composable(Screen.Settings.route) {
        SettingScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            onBackPressed = onBackPressed
        )
    }

    composable(Screen.LanguagePicker.route) {
        LanguageSelectScreen(modifier = Modifier.fillMaxSize())
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: @Composable () -> Unit
) {
    onBackPressed()

    val options = SettingOption.entries
    val session = LocalSession.current
    val firebaseAuth = FirebaseAuth.getInstance()
    var showThemeSheet by remember { mutableStateOf(false) }
    val showThemeState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LazyColumn(
        modifier = modifier
    ) {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 72.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.settings),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = stringResource(R.string.settings),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

            }
        }


        items(options.size){index ->
            val option = options[index]
            var switchAction:(Boolean)->Unit = {}
            val action = when(option){
                SettingOption.Language->{ {
                    navController.navigate(Screen.LanguagePicker.route)
                } }

                SettingOption.Logout -> {{
                    firebaseAuth.signOut()
                    session.value = session.value.copy(hasSession = false)
                }}

                SettingOption.Theme -> { {
                    showThemeSheet = true
                } }

            }


            SettingItem(
                option = option,
                onClick = action,
                enableClick = option.enableClick,
                switchAction = switchAction
            )
        }
    }

    if (showThemeSheet){
        ModalBottomSheet(
            onDismissRequest = {showThemeSheet = false},
            sheetState = showThemeState
        ) { ThemeSelectScreen() }
    }
}


@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    option: SettingOption,
    enableClick:Boolean = false,
    onClick:()->Unit,
    switchAction:(Boolean)->Unit ={},
    switchChecked : Boolean = false
) {

    Column( modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .then(
                    if (enableClick) Modifier.clickable(onClick = onClick)
                        else Modifier
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            option.iconRes?.let { res ->
                Icon(
                    painter = painterResource(res),
                    contentDescription = option.description?.let { stringResource(it) } ,
                    modifier = Modifier.padding(start = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(option.name),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )

                option.note?.let {
                    Text(
                        text = stringResource(it),
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }


            }

            Spacer(modifier = Modifier.weight(1f))
            option.navIcon?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            if (option.enableSwitchButton){
                Switch(
                    checked = switchChecked,
                    onCheckedChange = switchAction
                )

            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp
        )
    }



}

sealed class SettingOption(
    @StringRes val name:Int,
    @StringRes val description:Int? = null,
    @DrawableRes val iconRes: Int? = null,
    @DrawableRes val navIcon: Int? = null,
    @StringRes val note:Int? = null,
    val enableSwitchButton : Boolean = false,
    val enableClick: Boolean = false
){

    data object Language: SettingOption(
        name = R.string.language,
        description = R.string.change_language,
        iconRes = R.drawable.language,
        navIcon = R.drawable.chevron_right,
        enableClick = true
    )

    data object Theme:SettingOption(
        name = R.string.theme,
        description = R.string.change_theme,
        iconRes = R.drawable.theme,
        navIcon = R.drawable.chevron_right,
        enableClick = true
    )

    data object Logout:SettingOption(
        name = R.string.logout,
        iconRes = R.drawable.logout,
        enableClick = true
    )


    companion object{
        val entries:List<SettingOption> = listOf(Language,Theme,Logout)
    }
}