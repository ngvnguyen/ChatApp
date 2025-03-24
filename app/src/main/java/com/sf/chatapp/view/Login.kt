package com.sf.chatapp.view

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sf.chatapp.R
import com.sf.chatapp.utils.Constraint
import com.sf.chatapp.utils.LocalSession
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.utils.ToastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun AppLogin(
    modifier: Modifier = Modifier,
    onLoginSuccessfully:()->Unit = {}
){

    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()

    var account by remember { mutableStateOf(Account("","")) }
    var repeatPassword by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }
    val gmailPattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@gmail\\.com$"
    )
    val toastManager = LocalToastManager.current
    val textStyle = LocalTextStyle.current

    val session = LocalSession.current
    val onLoginSuccessful = {
        session.value = session.value.copy(hasSession = true)
        onLoginSuccessfully()
    }

    var isLogin by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 24.dp)
        )

        Text(
            text = stringResource(R.string.welcome_to_chat_app),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp))

        Text(
            text = if(isLogin) stringResource(R.string.login_to_continue) else stringResource(R.string.sign_up),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top =2.dp))


        Text(
            text = stringResource(R.string.email),
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 24.dp,end = 24.dp , top = 80.dp)
        )
        TextFieldView(
            value = account.email,
            onValueChange = {
                account = account.copy(email = it)
                isEmailValid = gmailPattern.matcher(account.email).matches()
            },
            placeholder = "abc@gmail.com",
            maxLines = 1,
            textStyle = TextStyle(color = Color.Gray, fontSize = 18.sp),
            modifier = Modifier.heightIn(min = 40.dp)
                .fillMaxWidth().padding(start = 24.dp,end = 24.dp,top = 12.dp),
            supportMatch = {
                if(!isEmailValid && account.email.isNotEmpty()) Text(
                    stringResource(R.string.please_enter_valid_email),
                    fontSize = 8.sp,
                    color = Color.Red
                )
            }
        )

        Text(
            text = stringResource(R.string.password),
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 24.dp,end = 24.dp , top = 40.dp)
        )
        TextFieldView(
            value = account.password,
            onValueChange = {
                account = account.copy(password = it)
            },
            placeholder = "********",
            maxLines = 1,
            textStyle = TextStyle(color = Color.Gray, fontSize = 18.sp),
            modifier = Modifier.heightIn(min = 40.dp)
                .fillMaxWidth().padding(start = 24.dp,end = 24.dp,top = 12.dp),
            isPassword = true
        )

        if (isLogin.not()){
            Text(
                text = stringResource(R.string.repeat_password),
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 24.dp,end = 24.dp , top = 40.dp)
            )
            TextFieldView(
                value = repeatPassword,
                onValueChange = {
                    repeatPassword = it
                },
                placeholder = "********",
                maxLines = 1,
                textStyle = TextStyle(color = Color.Gray, fontSize = 18.sp),
                modifier = Modifier.heightIn(min = 40.dp)
                    .fillMaxWidth().padding(start = 24.dp,end = 24.dp,top = 12.dp),
                isPassword = true
            )
        }else repeatPassword = ""


        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = {
                if (isLogin){
                    if (checkInput(account,gmailPattern))
                        login(firebaseAuth,account,toastManager,context,onLoginSuccessful)
                }
                else{
                    if (account.password == repeatPassword) registerAccount(firebaseAuth,account,context,toastManager)
                        else toastManager.showErrorToast(context.getString(R.string.the_repeat_password_is_incorrect))
                }},
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp),
            colors = ButtonDefaults.outlinedButtonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if(isLogin)stringResource(R.string.login) else stringResource(R.string.sign_up),
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        LoginWithProvider(
            providerName = stringResource(R.string.login_with) +" Google",
            iconRes = R.drawable.google,
            onClick = {
                loginWithGoogle(
                    firebaseAuth,
                    context,
                    scope,
                    toastManager,
                    onLoginSuccessful
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp, top = 8.dp)
        )

        Text(
            if (isLogin)stringResource(R.string.sign_up) else stringResource(R.string.sign_in),
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(bottom = 80.dp)
                .clickable {
                    isLogin = !isLogin
                },
            fontStyle = FontStyle.Italic)


    }


}

@Composable
fun LoginWithProvider(
    modifier: Modifier = Modifier,
    providerName:String,
    @DrawableRes iconRes:Int,
    onClick:()->Unit
){
    OutlinedButton(
        onClick = onClick,modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = providerName)
        }
    }
}


fun login(
    firebaseAuth: FirebaseAuth,
    account: Account,
    toastManager: ToastManager,
    context: Context,
    onLoginSuccessfully: () -> Unit,
    onLoginFailure:()->Unit ={}
){
    firebaseAuth.signInWithEmailAndPassword(account.email,account.password).addOnSuccessListener {
        onLoginSuccessfully()
        toastManager.showSuccessToast(context.getString(R.string.login_successfully))
    }.addOnFailureListener{
        onLoginFailure()
        toastManager.showErrorToast(context.getString(R.string.login_failed))
    }
}

fun loginWithGoogle(
    firebaseAuth: FirebaseAuth,
    context: Context,
    scope: CoroutineScope,
    toastManager: ToastManager,
    onLoginSuccessful: () -> Unit,
    onLoginFailure: () -> Unit={}
){
    val credentialManager = CredentialManager.create(context)
    val getGoogleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(Constraint.CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()
    val getCredentialRequest = GetCredentialRequest.Builder()
        .setCredentialOptions(listOf(getGoogleIdOption))
        .build()
    scope.launch {
        try{
            val result = credentialManager.getCredential(context,getCredentialRequest)
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken,null)

            firebaseAuth.signInWithCredential(firebaseCredential).addOnSuccessListener{
                toastManager.showSuccessToast(context.getString(R.string.login_successfully))
                onLoginSuccessful()
            }.addOnFailureListener{e->
                toastManager.showErrorToast(context.getString(R.string.login_failed))
                onLoginFailure()
            }
        }catch (e:GetCredentialCancellationException){
            toastManager.showInfoToast(context.getString(R.string.login_cancelled))
        }

    }



}

fun registerAccount(
    firebaseAuth: FirebaseAuth,
    account: Account,
    context: Context,
    toastManager: ToastManager
){
    try {
        firebaseAuth.createUserWithEmailAndPassword(account.email,account.password)
            .addOnSuccessListener {
                toastManager.showSuccessToast(context.getString(R.string.account_created_successfully))
            }.addOnFailureListener{
                toastManager.showErrorToast(context.getString(R.string.failed_to_create_account))
                Log.d("Created account",it.message.toString())
            }
    }catch (e:Exception){
        toastManager.showErrorToast(context.getString(R.string.failed_to_create_account))
        Log.d("Created account",e.message.toString())
    }

}

fun checkInput(account: Account,gmailPattern: Pattern):Boolean{
    return gmailPattern.matcher(account.email).matches() && account.password.isNotEmpty()
}

@Composable
fun TextFieldView(
    value:String,
    onValueChange:(String)->Unit,
    modifier:Modifier = Modifier,
    placeholder: String,
    textStyle: TextStyle = TextStyle.Default,
    maxLines: Int = Int.MAX_VALUE,
    isPassword:Boolean = false,
    supportMatch:@Composable() () -> Unit = {}
){
    var showPassword by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            decorationBox = {innerTextField ->
                Column {
                    var height by remember { mutableIntStateOf(0) }
                    Box(
                        modifier = Modifier.onSizeChanged {
                            height = it.height
                        }
                    ){
                        if (value.isEmpty()){
                            Text(text = placeholder,color = textStyle.color, fontSize = textStyle.fontSize)
                        }
                        innerTextField()
                        Row() {
                            Spacer(modifier = Modifier.weight(1f))

                            if(isPassword) IconButton(
                                onClick = {showPassword = !showPassword},
                                modifier = Modifier.heightIn(max = (height*0.3).dp)
                            ) {
                                Icon(
                                    painter = if (showPassword)
                                        painterResource(R.drawable.visibility_off)
                                        else painterResource(R.drawable.visibility),
                                    contentDescription = null
                                )
                            }
                        }
                        
                    }
                    supportMatch()
                }

            },
            maxLines = maxLines,
            visualTransformation = if(!showPassword && isPassword) PasswordVisualTransformation()
                else VisualTransformation.None
        )

        HorizontalDivider(
            thickness = 2.dp,color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp)
        )
    }


}

data class Account(
    val email:String,
    val password:String
)

