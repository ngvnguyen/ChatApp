package com.sf.chatapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sf.chatapp.R
import com.sf.chatapp.remote.model.Role
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.utils.parseText
import com.sf.chatapp.view.searchbar.AppSearchBar
import com.sf.chatapp.view.textfield.InputWithVoiceTextField
import com.sf.chatapp.viewmodel.ChatBotViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun ChatBotScreen(
    modifier: Modifier = Modifier,
    onBackPressed:@Composable ()->Unit,
    ownerPaddingValues: PaddingValues = PaddingValues(0.dp)
){
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val viewModel: ChatBotViewModel = koinViewModel()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val conversation by viewModel.searchConversation.collectAsStateWithLifecycle()
    val toastManager = LocalToastManager.current
    val searchConversationQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    onBackPressed()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    LaunchedEffect(messages) {
        if(messages.isNotEmpty()) listState.scrollToItem(messages.size -1)
    }
    LaunchedEffect(Unit) {
        scope.launch {
            drawerState.close()
        }
    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.background(Color.Transparent),
        drawerContent = {


            Column(modifier =Modifier.fillMaxWidth(0.8f)) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = dimensionResource(R.dimen.padding_top_app)),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AppSearchBar(
                        value = searchConversationQuery,
                        onValueChange = {  viewModel.searchConversation(it)  },
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp,end =16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.new_window),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.createNewConversation()
                            scope.launch {
                                drawerState.close()
                            }
                        }.padding(end = 8.dp)
                    )
                }

                LazyColumn {
                    item{
                        Text(
                            text =  stringResource(R.string.conversation),
                            modifier = Modifier.padding(start = 4.dp, top=32.dp,bottom = 8.dp),
                            fontSize = MaterialTheme.typography.labelLarge.fontSize
                        )
                    }
                    conversation.forEach{(key,value)->
                        item{
                            Row(modifier = Modifier.fillMaxWidth()){
                                Text(
                                    text = value.content.get(0).parts.get(0).text.ifEmpty { stringResource(R.string.no_name) },
                                    modifier = Modifier
                                        .padding(top=24.dp,start = 8.dp,end = 8.dp)
                                        .clickable {
                                            viewModel.selectConversation(key)
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }



                }
            }

        }
    ) {


        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            bottomBar = {
                val ownerBottomPadding = ownerPaddingValues.calculateBottomPadding()
                val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                MessageBar(
                    message = message,
                    onValueChange = {message = it},
                    onSend = {
                        scope.launch {
                            viewModel.sendMessage(
                                message,
                                onSuccess = {
                                    message = ""
                                    focusManager.clearFocus()
                                },
                                onFailed = {
                                    toastManager.showErrorToast(context.getString(R.string.failed_to_create_account))
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets(
                                bottom = if (imeBottomPadding>0.dp)
                                    imeBottomPadding-ownerBottomPadding
                                else imeBottomPadding
                            )
                        )
                )
            },
            topBar = {
                TopChatBotBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isOpen) close()
                                else open()
                            }
                        }
                    },
                    onCreateNewConversation = {
                        viewModel.createNewConversation()
                    },
                    //modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_top_app))
                )
            }
        ) { innerPadding->

            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.padding(top = 4.dp)
                        .fillMaxSize()
                ) {
                    items(messages.size){index->
                        BotChatMessageBubble(
                            messages.get(index).parts[0].text,
                            messages.get(index).role == Role.USER
                        )

                    }
                }

                if (messages.isEmpty()){
                    Text(
                        text = stringResource(R.string.how_can_i_help_you),
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
            if (drawerState.isOpen) Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.01f))
                    .pointerInput(Unit){
                        detectTapGestures {
                            if (drawerState.isOpen){
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                    }
            )
        }

    }

}

@Composable
fun TopChatBotBar(
    onOpenDrawer:()->Unit,
    onCreateNewConversation:()->Unit,
    modifier: Modifier = Modifier
){
    var menuExpanded by remember { mutableStateOf(false) }
    Row(modifier = modifier) {
        IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                painterResource(R.drawable.sort),
                contentDescription = null
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onCreateNewConversation,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                painterResource(R.drawable.new_window),
                contentDescription = null
            )
        }

        Box(){
            IconButton(
                onClick = { menuExpanded = true},
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.more),
                    contentDescription = null

                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {menuExpanded = false}
            ) {
                DropdownMenuItem(
                    text = { Text("option 1") },
                    onClick = {}
                )
            }
        }

        //TopAppAds(adUnitId = Constraint.BOTTOM_AD_UNIT_ID)
    }
}

@Composable
fun MessageBar(
    message:String,
    onValueChange:(String)->Unit,
    onSend:()->Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        InputWithVoiceTextField(
            onValueChange = onValueChange,
            value = message,
            modifier = Modifier.weight(0.8f)
                .padding(start = 16.dp)
        )
        IconButton(onClick = onSend) { Icon(
            painter = painterResource(R.drawable.send),
            contentDescription = null
        ) }
    }
}



@Composable
fun BotChatMessageBubble(
    message:String,
    isMe:Boolean,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(start =2.dp,end =2.dp),
        horizontalArrangement = if(isMe) Arrangement.End else Arrangement.Start,
    ) {
        val annotatedMessage = parseText(message)
        if (isMe){
            Spacer(modifier = Modifier.fillMaxWidth(0.3f))
            Card(
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            ) {
                Text(
                    text = annotatedMessage,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        else{
            Text(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                text = annotatedMessage
            )
        }
    }
}



