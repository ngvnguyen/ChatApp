package com.sf.chatapp.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


@Composable
fun parseText(
    input:String,
    isFirstCall:Boolean=true
):AnnotatedString{
    val annotatedRegex = Regex("(\\*\\*(.*?)\\*\\*)|(`(.*?)`)")
    return buildAnnotatedString {
        var lastIndex = 0
        annotatedRegex.findAll(input).forEach { matchResult ->
            val fullMatch = matchResult.value
            val start = matchResult.range.first
            val end = matchResult.range.last+1

            withStyle(style = SpanStyle(color =  MaterialTheme.colorScheme.onBackground.copy(0.8f))){
                append(input.substring(lastIndex,start))
            }

            val textContent = when{
                fullMatch.startsWith("**")->matchResult.groupValues[2]
                fullMatch.startsWith("`")->matchResult.groupValues[4]
                else ->""
            }

            when{
                fullMatch.startsWith("**")-> {
                    val style = SpanStyle(fontWeight = FontWeight.Bold)
                    pushStyle(style)
                    append(parseText(textContent,false))
                    pop()
                }
                fullMatch.startsWith("`")->{
                    val style = SpanStyle(fontStyle = FontStyle.Italic)
                    pushStyle(style)
                    append(parseText(textContent,false))
                    pop()
                }
            }

            lastIndex = end
        }
        if (lastIndex<input.length){
            if (isFirstCall) pushStyle(SpanStyle(color =  MaterialTheme.colorScheme.onBackground.copy(0.8f)))
            append(input.substring(lastIndex))
        }

    }
}
