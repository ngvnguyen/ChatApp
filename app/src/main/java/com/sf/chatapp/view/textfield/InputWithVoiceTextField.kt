package com.sf.chatapp.view.textfield

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sf.chatapp.R
import java.util.Locale


@Composable
fun InputWithVoiceTextField(
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    value:String
){
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val resultText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (resultText!=null){
                onValueChange(resultText)
            }
        }
    }

    OutlinedTextField(
        value=value,
        onValueChange={onValueChange(it)},
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = {
                launcher.launch(speechRecognizerIntent)
            }) {
                Icon(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = null
                )
            }
        },
        shape = RoundedCornerShape(30.dp),
        placeholder = {
            Text(stringResource(R.string.message))
        },
        maxLines = 4
    )
}