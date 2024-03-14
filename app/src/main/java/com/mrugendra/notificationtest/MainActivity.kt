package com.mrugendra.notificationtest

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.AppMainScreen
import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

val TAG = "MyFirebaseMessagingService"
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
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
//                    val launcher = registerForActivityResult(
//                        ActivityResultContracts.RequestPermission()
//                    ) { isGranted ->
//                        if (isGranted){
//
//                        } else {
//                            // permission denied or forever denied
//                        }
//                    }

                    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
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
                        logout = {viewModel.logoutUser()},
                        retry = {viewModel.getFCMToken()},
                        openCalender = {viewModel.openCalender()},
                        searchIdentified = {viewModel.searchIdentified()},
                        searchResidents = { viewModel.searchResidents() },
                        onSearchText = {viewModel.onSearchText(it)},
                        updateIdentifiedList = {viewModel.UpdateIdentifiedList(it)},
                        updateUnidentifiedList = {viewModel.UpdateUnidentifiedList(it)},
                        identifiedRefreshState = rememberPullRefreshState(
                            refreshing = nofUiState.identifiedRefresh,
                            onRefresh = {
                                Log.d(TAG,"Refresh stared with pull")
                                viewModel.UpdateIdentifiedList(true)
                            }),
                        permissionState = permissionState,
                        unidentifiedRefreshState = rememberPullRefreshState(
                            refreshing = nofUiState.unidentifiedRefresh,
                            onRefresh = { Log.d(TAG,"Refresh stared with pull")
                                viewModel.UpdateUnidentifiedList(true) }),
                        searchUnidentified = {viewModel.searchUnidentified()},
                        delRefreshState = rememberPullRefreshState(
                            refreshing = nofUiState.deliveryRefresh,
                            onRefresh = { Log.d(TAG,"Refresh stared with pull")
                                viewModel.updateDeliveryPeopleList(true) }),
                        updateDeliveryPeopelList = {viewModel.updateDeliveryPeopleList(it)},
                        searchDeliveryPerson = {viewModel.searchDeliveryList()}
                    )

                }
            }
        }
    }

}
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermissionDialog() {

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
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
            logout = { },
            retry = {},
            openCalender = {},
            searchIdentified ={},
            searchResidents = {},
            onSearchText = {},
            updateIdentifiedList={},
            updateUnidentifiedList = {},
            identifiedRefreshState  = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS),
            unidentifiedRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            searchUnidentified = {},
            delRefreshState =rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            updateDeliveryPeopelList = {},
            searchDeliveryPerson ={}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun MainSceen(
    nofUiState: uiState,
    updateName: (String) -> Unit,
    updateToken: () -> Unit,
    updateResidentList: () -> Unit,
    forceUpdateResidentList: (Boolean) -> Unit,
    getTheList: () -> Unit,
    residentRefreshState: PullRefreshState,
    getResident: (String) -> Unit,
    updateUsername: (String) -> Unit,
    updatePassword: (String) -> Unit,
    loginUser: () -> Unit,
    logout: () -> Unit,
    retry: () -> Unit,
    openCalender: () -> Unit,
    searchIdentified: () -> Unit,
    searchResidents: () -> Unit,
    onSearchText: (String) -> Unit,
    updateIdentifiedList: (Boolean) -> Unit,
    identifiedRefreshState: PullRefreshState,
    permissionState: PermissionState,
    unidentifiedRefreshState: PullRefreshState,
    updateUnidentifiedList: (Boolean) -> Unit,
    searchUnidentified:()->Unit,
    delRefreshState:PullRefreshState,
    updateDeliveryPeopelList:(Boolean)->Unit,
    searchDeliveryPerson :()->Unit
) {
//    Log.d("check_res_with", colorResource(id = 0x1060060).toString())
    AppMainScreen(
        nofUiState = nofUiState,
        updateName = updateName,
        updateToken = updateToken,
        updateResidentList = updateResidentList,
        forceUpdateResidentList = forceUpdateResidentList,
        getTheList = getTheList,
        residentRefreshState = residentRefreshState,
        getResident = getResident,
        updateUsername = { updateUsername(it) },
        updatePassword = {updatePassword(it)},
        loginUser = loginUser,
        logout  = logout,
        retry = retry,
        openCalender = openCalender,
        searchIdentified = searchIdentified,
        searchResidents = searchResidents,
        onSearchText = { onSearchText(it) },
        updateIdentifiedList = { updateIdentifiedList(it) },
        identifiedRefreshState = identifiedRefreshState,
        permissionState = permissionState,
        unidentifiedRefreshState = unidentifiedRefreshState,
        updateUnidentifiedList = {updateUnidentifiedList(it)},
        searchUnidentified = searchUnidentified,
        delRefreshState = delRefreshState,
        updateDeliveryPeopelList = { updateDeliveryPeopelList(it) },
        searchDeliveryPerson = searchDeliveryPerson
    )
}