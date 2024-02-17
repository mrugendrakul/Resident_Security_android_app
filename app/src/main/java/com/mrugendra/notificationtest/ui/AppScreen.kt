package com.mrugendra.notificationtest.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.data.uiState
import java.time.LocalDateTime
import java.time.Month

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Identified(title = R.string.Identified),
    Unidentified(title = R.string.Unidentified),
    Residents(title = R.string.Residents),
    TokenRegistration(title = R.string.TokenRegs),
    PersonDetail(title = R.string.detailed_information)
}

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AppMainScreen(
    nofUiState: uiState,
    updateName: (String) -> Unit,
    updateToken: () -> Unit,
    navController: NavHostController = rememberNavController(),
    updateResidentList: () -> Unit,
    forceUpdateResidentList: (Boolean) -> Unit,
    getTheList: () -> Unit,
    residentRefreshState: PullRefreshState
) {
//    Log.d("check_res_with", colorResource(id = 0x1060060).toString())
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    val slideInTransition = slideInVertically() +fadeIn()
    val slideOutTransition = slideOutVertically() + fadeOut()

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
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(
                route = AppScreen.Start.name,
                enterTransition = { slideInTransition },
                exitTransition = {slideOutTransition}
            ) {
                Log.d(com.mrugendra.notificationtest.TAG, "starting to navigate")
                StartHere(nofUiState = nofUiState,
                    navigateToIdentified = {
                        Log.d(com.mrugendra.notificationtest.TAG, "Identified navigate")
                        navController.navigate(AppScreen.Identified.name)
                    },
                    navigateToUnidentified = {
                        Log.d(com.mrugendra.notificationtest.TAG, "Unidentified navigate")
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
            composable(
                route = AppScreen.Identified.name,
                enterTransition = { slideInTransition },
                exitTransition = {slideOutTransition}
            ) {
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
                    ),
                     button = {
                        Log.d(com.mrugendra.notificationtest.TAG, "Identified navigate to person")
                        navController.navigate(AppScreen.PersonDetail.name)
                    },
                    buttonEnabled = true
                    )
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
                ), {
                    Log.d(com.mrugendra.notificationtest.TAG, "Unidentified navigate to person")
                    navController.navigate(AppScreen.PersonDetail.name)
                })
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
        colors = TopAppBarDefaults.topAppBarColors(
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





