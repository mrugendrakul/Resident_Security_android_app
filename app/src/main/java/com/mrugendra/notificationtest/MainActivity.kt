package com.mrugendra.notificationtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.AppMainScreen
import com.mrugendra.notificationtest.ui.IdentifiedList
import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.PersonDetail
import com.mrugendra.notificationtest.ui.ResidentList
import com.mrugendra.notificationtest.ui.StartHere
import com.mrugendra.notificationtest.ui.TokenRegistration
import com.mrugendra.notificationtest.ui.UnidentifiedList
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.time.LocalDateTime
import java.time.Month

val TAG = "MyFirebaseMessagingService"
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(
                dynamicColor = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: Notfi = viewModel(factory = Notfi.Factory)
                    val nofUiState = viewModel.notUiState.collectAsState().value
                    MainSceen(
                        nofUiState = nofUiState,
                        updateName = { viewModel.updateName(it) },
                        updateToken = { viewModel.updateToken() },
                        updateResidentList = {viewModel.UpdateResidentsList()},
                        forceUpdateResidentList = {viewModel.UpdateResidentsList(it)},
                        getTheList = {viewModel.UpdateResidentsList()},
                        residentRefreshState = rememberPullRefreshState(
                            refreshing = nofUiState.residentRefreshIsLoading,
                            onRefresh = {
                                Log.d(TAG,"Refresh stared with pull")
                                viewModel.UpdateResidentsList(true)
                            }),
                        getResident = {viewModel.UpdateCurrentResidentAndIdentifiedId(it)},
                        updateUsername = {viewModel.updateUsername(it)},
                        updatePassword = {viewModel.updatePassword(it)},
                        loginUser = {viewModel.updateToken()},
                        logout = {viewModel.logoutUser()}

                    )

                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun MyMainScreenPreview() {

    MyApplicationTheme(
        dynamicColor = false
    )
    {
        MainSceen(
            nofUiState = uiState(),
            updateName = {},
            updateToken = {},
            updateResidentList = {},
            forceUpdateResidentList = {},
            getTheList = {},
            residentRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            getResident = {},
            updateUsername = {},
            updatePassword = {},
            loginUser = {},
            logout = { }

        )
    }
}

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainSceen(
    nofUiState: uiState,
    updateName: (String) -> Unit,
    updateToken: () -> Unit,
    updateResidentList:()->Unit,
    forceUpdateResidentList:(Boolean)->Unit,
    getTheList:()->Unit,
    residentRefreshState:PullRefreshState,
    getResident:(String)->Unit,
    updateUsername:(String) ->Unit,
    updatePassword:(String) ->Unit,
    loginUser:()->Unit,
    logout : ()->Unit
) {
//    Log.d("check_res_with", colorResource(id = 0x1060060).toString())
    AppMainScreen(
        nofUiState = nofUiState,
        updateName = updateName,
        updateToken = updateToken,
        forceUpdateResidentList = forceUpdateResidentList,
        getTheList = getTheList,
        residentRefreshState = residentRefreshState ,
        updateResidentList = updateResidentList,
        getResident = getResident,
        updateUsername = { updateUsername(it) },
        updatePassword = {updatePassword(it)},
        loginUser = loginUser,
        logout  = logout
    )
}