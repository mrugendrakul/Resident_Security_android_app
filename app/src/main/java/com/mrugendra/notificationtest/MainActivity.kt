package com.mrugendra.notificationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.data.residents
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.IdentifiedList
import com.mrugendra.notificationtest.ui.ResidentList
import com.mrugendra.notificationtest.ui.StartHere
import com.mrugendra.notificationtest.ui.TokenRegistration
import com.mrugendra.notificationtest.ui.UnidentifiedList
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.time.LocalDateTime
import java.time.Month

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
    MyApplicationTheme()
    {
        MainSceen(
            nofUiState = uiState(),
            updateName = {},
            updateToken = {}
        )
    }
}

enum class AppScreen(@StringRes val title:Int){
    Start(title = R.string.app_name),
    Identified(title = R.string.Identified),
    Unidentified(title = R.string.Unidentified),
    Residents(title = R.string.Residents),
    TokenRegistration(title = R.string.TokenRegs),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSceen(
    nofUiState:uiState,
    updateName:(String)->Unit,
    updateToken:()->Unit,
    navController:NavHostController = rememberNavController(),

){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?:AppScreen.Start.name
    )
    Scaffold(
        topBar = {
            IntruderAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry!=null,
                navigateUp = { navController.navigateUp() }

            )
        },

    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(AppScreen.Start.name){
                StartHere(nofUiState = nofUiState,
                    navigateToIdentified = {
                        navController.navigate(AppScreen.Identified.name)
                    },
                    navigateToUnidentified = {
                         navController.navigate(AppScreen.Unidentified.name)
                    },
                    navigateToResidents = {
                        navController.navigate(AppScreen.Residents.name)
                    },
                    navigateToToken = {
                        navController.navigate(AppScreen.TokenRegistration.name)
                    }
                    )
            }
            composable(AppScreen.Identified.name){
                IdentifiedList(listOf(
                    Identified("xyz","Mrugendra", LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Identified("xyz","Mrugendra", LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Identified("xyz","Mrugendra", LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Identified("xyz","Mrugendra", LocalDateTime.of(2024, Month.JANUARY,30,22,12))
                ))
            }
            composable(AppScreen.Unidentified.name){
                UnidentifiedList(unidentifes = listOf(
                    Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
                    Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12))
                ))
            }
            composable(AppScreen.Residents.name){
                ResidentList(residents = listOf(
                    residents("Mrugendra","123uuidxzyabc","Will get there"),
                    residents("Mrugendra","123uuidxzyabc","Will get there"),
                    residents("Mrugendra","123uuidxzyabc","Will get there")
                ))
            }
            composable(AppScreen.TokenRegistration.name){
                TokenRegistration(
                    nofUiState = nofUiState,
                    updateToken = updateToken,
                    updateName = updateName)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntruderAppBar(
    currentScreen: AppScreen,
    canNavigateBack:Boolean,
    navigateUp:()->Unit = {},
    modifier:Modifier = Modifier
){
    TopAppBar(title = { Text (stringResource(id = currentScreen.title),
    color = MaterialTheme.colorScheme.onPrimary)},
    colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
        ),
    navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button))
            }
        }
    }
    )
}





