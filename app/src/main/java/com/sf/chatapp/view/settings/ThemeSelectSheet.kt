package com.sf.chatapp.view.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sf.chatapp.R
import com.sf.chatapp.ui.theme.setColorScheme
import com.sf.chatapp.ui.theme.setDefaultColorScheme
import com.sf.chatapp.ui.theme.themes
import com.sf.chatapp.utils.ColorSchemeState
import com.sf.chatapp.utils.LocalColorSchemeState

@Composable
fun ThemeSelectScreen(
    modifier: Modifier = Modifier
){
    val colorSchemeState = LocalColorSchemeState.current
    val context = LocalContext.current

    Column(modifier = modifier){
        Text(
            text = stringResource(R.string.theme),
            modifier = Modifier.padding(start = 16.dp,bottom = 24.dp),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(modifier = modifier) {
            item {
                val isDarkMode = isSystemInDarkTheme()
                ThemeItem(
                    themeName = stringResource(R.string.default_),
                    modifier = Modifier.padding(12.dp),
                    onSelect = {
                        context.setDefaultColorScheme(colorSchemeState,isDarkMode)
                    }
                )
            }
            themes.values.forEach{theme->
                item{
                    ThemeItem(
                        themeName = stringResource(theme.name),
                        modifier = Modifier.padding(12.dp),
                        onSelect = {
                            context.setColorScheme(colorSchemeState,theme.id)
                        }
                    )
                }
            }

        }
    }


}

@Composable
fun ThemeItem(
    themeName: String,
    modifier: Modifier = Modifier,
    onSelect:()->Unit
){
    Row(
        modifier = modifier.fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        Text(
            text = themeName,
            modifier = modifier
        )
    }
}