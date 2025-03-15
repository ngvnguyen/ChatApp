package com.sf.chatapp.view.searchbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sf.chatapp.R

@Composable
fun AppSearchBar(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange:(String)->Unit
){
    var isFocusTextField by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(30.dp),
        maxLines = 1,
        modifier = modifier
            .onFocusChanged {
                isFocusTextField = it.hasFocus
            }
            .focusRequester(focusRequester),
        leadingIcon = {
            IconButton(
                onClick={
                    isFocusTextField = !isFocusTextField
                    if (isFocusTextField) focusRequester.requestFocus()
                    else focusManager.clearFocus()
                }
            ) {
                Icon(
                    painterResource(if(isFocusTextField) R.drawable.ic_back else R.drawable.search),
                    contentDescription = null
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}