package com.sf.chatapp.view.settings


import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.sf.chatapp.R
import com.sf.chatapp.utils.Language


@Composable
fun LanguageSelectScreen(
    modifier: Modifier = Modifier
) {

    val languages = Language.entries
    var currentSelect by remember { mutableIntStateOf(0) }
    currentSelect = AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.let {
        Language.findLanguageByLocale(it).ordinal
    }?: 0

    LazyColumn(
        modifier = modifier
    ) {
        item{

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 72.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.language),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = stringResource(R.string.language),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

            }
        }

        items(languages.size){index->
            val onClick = {
                currentSelect = index
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(languages[index].locale))
            }

            LanguageItem(
                language = languages[index],
                onClick = onClick,
                selected = currentSelect == index
            )

        }
    }

}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    language: Language,
    onClick:()->Unit,
    selected:Boolean
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(top = 8.dp,bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = {},
                modifier = Modifier.padding(start = 8.dp,end = 8.dp)
            )
            Text(
                text = stringResource(language.languageName)
            )

        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 2.dp)
                .fillMaxWidth(),
            thickness = 1.dp
        )
    }


}