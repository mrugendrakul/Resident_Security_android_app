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
                            })
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
            residentRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ })
        )
    }
}

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Identified(title = R.string.Identified),
    Unidentified(title = R.string.Unidentified),
    Residents(title = R.string.Residents),
    TokenRegistration(title = R.string.TokenRegs),
    PersonDetail(
        title = R.string.detailed_information
    )
}


@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainSceen(
    nofUiState: uiState,
    updateName: (String) -> Unit,
    updateToken: () -> Unit,
    navController: NavHostController = rememberNavController(),
    updateResidentList:()->Unit,
    forceUpdateResidentList:(Boolean)->Unit,
    getTheList:()->Unit,
    residentRefreshState:PullRefreshState
    ) {
//    Log.d("check_res_with", colorResource(id = 0x1060060).toString())
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )
    Scaffold(
        topBar = {
            IntruderAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }

            )
        },

        ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Start.name) {
                StartHere(nofUiState = nofUiState,
                    navigateToIdentified = {
                        navController.navigate(AppScreen.Identified.name)
                    },
                    navigateToUnidentified = {
                        navController.navigate(AppScreen.Unidentified.name)
                    },
                    navigateToResidents = {
                        updateResidentList()
                        navController.navigate(AppScreen.Residents.name)
                    },
                    navigateToToken = {
                        navController.navigate(AppScreen.TokenRegistration.name)
                    }
                )
            }
            composable(AppScreen.Identified.name) {
                IdentifiedList(
                    listOf(
                        Identified(
                            "xyz",
                            "Mrugendra",
                            LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                        ),
                        Identified(
                            "xyz",
                            "Mrugendra",
                            LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                        ),
                        Identified(
                            "xyz",
                            "Mrugendra",
                            LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                        ),
                        Identified(
                            "xyz",
                            "Mrugendra",
                            LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                        )
                    )
                ) {
                    navController.navigate(AppScreen.PersonDetail.name)
                }
            }
            composable(AppScreen.Unidentified.name) {
                UnidentifiedList(unidentifes = listOf(
                    Unidentified(
                        image = "https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",
                        time = LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                    ),
                    Unidentified(
                        image = "https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",
                        time = LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                    ),
                    Unidentified(
                        image = "https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",
                        time = LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                    ),
                    Unidentified(
                        image = "https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",
                        time = LocalDateTime.of(2024, Month.JANUARY, 30, 22, 12)
                    )
                ), {})
            }
            composable(AppScreen.Residents.name) {
                ResidentList(
                    nofUiState = nofUiState,
                    residents = nofUiState.residentStatus,
                    getTheList = getTheList,
                    pressed = {
                        navController.navigate(AppScreen.PersonDetail.name)
                    },
                    refreshState = residentRefreshState,
                    refreshButton = { forceUpdateResidentList(true) }
                )
            }
            composable(AppScreen.TokenRegistration.name) {
                TokenRegistration(
                    nofUiState = nofUiState,
                    updateToken = updateToken,
                    updateName = updateName
                )
            }
            composable(AppScreen.PersonDetail.name) {
                PersonDetail()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntruderAppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(title = {
        Text(
            stringResource(id = currentScreen.title),
            color = MaterialTheme.colorScheme.onPrimary
        )
    },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}





