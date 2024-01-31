package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.TAG
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun MyApp(
    nofUiState: uiState,
    updateName:(String)->Unit,
    updateToken:()->Unit,
    modifier: Modifier=Modifier
) {
//    val nofUiState by notifViewModel.notUiState.collectAsState()
    val token = nofUiState.token

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.FcmToken))
        Spacer(modifier = Modifier.height(8.dp))
        SelectionContainer {
            Text("$token")
        }
        TextField(value = nofUiState.name,
            label = {
                if(nofUiState.nullName==true){
                Text(text = stringResource(R.string.name_cannot_be_null))
                }
                else if(nofUiState.exist==false)
                { Text(text = stringResource(R.string.name_for_storing_token)) }
                else if(nofUiState.exist==true){
                    Text(text = stringResource(R.string.token_exist))
                }

            },
            singleLine = true,
            onValueChange = {updateName(it)},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {updateToken()}
            ),
            isError = nofUiState.exist or nofUiState.nullName

        )
        Button(onClick = {updateToken() },

            enabled = !nofUiState.exist and !nofUiState.successSend) {
            Log.d(TAG,"Exist inside ui button : ${nofUiState.exist}")
            Text(text = stringResource(R.string.database_Sync))
        }
        if(nofUiState.exist){
            Text(text = stringResource(R.string.token_already_exist),
                color = MaterialTheme.colorScheme.error)
        }

    }
}



@Preview
@Composable
fun MyAppPreview(){
    MyApplicationTheme()
    { MyApp(
        nofUiState = uiState(),
        updateToken = {},
        updateName = {

        }
    ) }
}

