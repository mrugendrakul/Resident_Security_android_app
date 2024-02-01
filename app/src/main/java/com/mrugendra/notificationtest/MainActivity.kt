package com.mrugendra.notificationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.MyApp
import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel:Notfi=viewModel(factory = Notfi.Factory)
                    MainSceen(
                        nofUiState = viewModel.notUiState.collectAsState().value,
                        updateName = { viewModel.updateName(it) },
                        updateToken = { viewModel.updateToken() })
                }
            }
        }
    }

}

@Preview
@Composable
fun MyMainScreenPreview(){
//    MyApp()
    MainSceen(
        nofUiState = uiState(),
        updateName = {},
        updateToken = {}
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSceen(
    nofUiState:uiState,
    updateName:(String)->Unit,
    updateToken:()->Unit
){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text (stringResource(id = R.string.app_name) ,
                color = MaterialTheme.colorScheme.onPrimary)},
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
            )
        },

    ) {

        MyApp(
            nofUiState = nofUiState,
            updateName = updateName,
            updateToken = updateToken,
            modifier = Modifier.padding(it)
        )
    }
}





