package com.mrugendra.notificationtest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Identified(title = R.string.Identified),
    Unidentified(title = R.string.Unidentified),
    Residents(title = R.string.Residents),
    TokenRegistration(title = R.string.TokenRegs),
    PersonDetail(title = R.string.detailed_information),
    LoginPage(title = R.string.login_user),
    Loading(title = R.string.loading),
    DeliveryPersons(title = R.string.delivery_person)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AppMainScreen(
    nofUiState: uiState,
    updateName: (String) -> Unit,
    updateToken: () -> Unit,
    navController: NavHostController = rememberNavController(),
    updateResidentList: () -> Unit,
    forceUpdateResidentList: (Boolean) -> Unit,
    getTheList: () -> Unit,
    residentRefreshState: PullRefreshState,
    getResident:(String)->Unit,
    updateUsername: (String) -> Unit,
    updatePassword: (String) ->Unit,
    loginUser:()->Unit,
    logout : ()->Unit,
    retry:()->Unit,
    openCalender:()->Unit,
    searchIdentified:()->Unit,
    searchResidents:()->Unit,
    onSearchText:(String)->Unit,
    updateIdentifiedList:(Boolean)->Unit,
    identifiedRefreshState :PullRefreshState,
    permissionState: PermissionState,
    unidentifiedRefreshState : PullRefreshState,
    updateUnidentifiedList :(Boolean)->Unit,
    searchUnidentified:()->Unit,
    delRefreshState:PullRefreshState,
    updateDeliveryPeopelList:(Boolean)->Unit,
    searchDeliveryPerson :()->Unit
) {
//    Log.d("check_res_with", colorResource(id = 0x1060060).toString())
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    val slideInTransition = slideInVertically() +fadeIn()
    val slideOutTransition = slideOutVertically() + fadeOut()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
                        DrawerContent(
                            currentScreen,
                            navController,
                            forceUpdateResidentList,
                            scope = scope,
                           drawerState =  drawerState
                        )
        },
        drawerState = drawerState,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        gesturesEnabled = !(currentScreen == AppScreen.Loading || currentScreen == AppScreen.LoginPage)
    ){
        Scaffold(
            topBar = {
                IntruderAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    logout = logout,
                    drawerState = drawerState,
                    scope = scope
                )
            },

            ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = nofUiState.startScreen.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(
                    route = AppScreen.Loading.name
                ){
                    LoadingScreen(
                        nofUiState = nofUiState,
                        retry =retry
                    )
                }

                composable(
                    route = AppScreen.Start.name,
                    enterTransition = { slideInTransition },
                    exitTransition = { slideOutTransition }
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
//                        navController.navigate(AppScreen.TokenRegistration.name)
                        },
                        permissionState = permissionState
                    )
                }
                composable(
                    route = AppScreen.Identified.name,
                    enterTransition = { slideInTransition },
                    exitTransition = { slideOutTransition }
                ) {
                    updateIdentifiedList(false)
                    updateResidentList()
                    IdentifiedList(
                        nofUiState = nofUiState,
                        identified = nofUiState.identifiedStatus,
                        button = {
                            Log.d(
                                com.mrugendra.notificationtest.TAG,
                                "Identified navigate to person"
                            )
                            getResident(it)
                            navController.navigate(AppScreen.PersonDetail.name)
                        },
                        buttonEnabled = true,
                        openCalender = openCalender,
                        search = searchIdentified,
                        onSearchText = { onSearchText(it) },
                        refreshState = identifiedRefreshState,
                        errorRefresh = { updateIdentifiedList(it) }
                    )
                }
                composable(AppScreen.Unidentified.name) {
                    updateUnidentifiedList(false)
                    UnidentifiedList(
                        nofUiState = nofUiState,
                        openCalender = openCalender,
                        search = searchUnidentified,
                        refreshState = unidentifiedRefreshState,
                        errorRefresh = {updateUnidentifiedList(it)}
                    )
                }
                composable(AppScreen.Residents.name) {
                    Log.d(TAG,"Residents status : ${nofUiState.residentStatus}")
                    updateResidentList()
                    ResidentList(
                        nofUiState = nofUiState,
                        residents = nofUiState.residentStatus,
                        pressed = {
                            getResident(it)
                            navController.navigate(AppScreen.PersonDetail.name)
                        },
                        getTheList = getTheList,
                        refreshState = residentRefreshState,
                        refreshButton = { forceUpdateResidentList(true) },
                        openCalender = openCalender,
                        search = searchResidents,
                        onSearchText = { onSearchText(it) }
                    )
                }
                composable(AppScreen.TokenRegistration.name) {
                    TokenRegistration(
                        nofUiState = nofUiState,
                        updateToken = updateToken,
                        updateName = updateName
                    )
                }

                composable(
                    route = AppScreen.PersonDetail.name,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 300)
                        )  /*+ fadeIn()*/
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(durationMillis = 300)
                        ) /*+ fadeOut()*/
                    }
                ) {
                    PersonDetail(
                        residents = nofUiState.currentResident
                    )
                }
                composable(
                    route = AppScreen.LoginPage.name
                ) {
                    LoginUser(
                        nofUiState = nofUiState,
                        updateUsername = { updateUsername(it) },
                        updatePassword = { updatePassword(it) },
                        loginUser = loginUser
                    )

                }

                composable (
                    route = AppScreen.DeliveryPersons.name
                ){
                    updateDeliveryPeopelList(false)
                    DeliveryList(
                        nofUiState = nofUiState,
                        openCalender = openCalender,
                        search = searchDeliveryPerson,
                        refreshState = delRefreshState,
                        errorRefresh = {updateDeliveryPeopelList(false)}
                    )
                }

            }

        }
    }
}

@Composable
fun DrawerContent(
    currentScreen: AppScreen,
    navController: NavHostController,
    forceUpdateResidentList: (Boolean) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp),
        drawerShape = RoundedCornerShape(0.dp)


    ) {
        Text(
            text = "Alert System",
            fontSize = 25.sp,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 10.dp)
        )

        val optionsForUser = listOf(AppScreen.Identified,AppScreen.Unidentified,AppScreen.Residents,AppScreen.DeliveryPersons)
        NavigationDrawerItem(
            label = { Text(text = stringResource(AppScreen.Start.title))},
            selected = currentScreen == AppScreen.Start,
            onClick = {
                if(currentScreen!=AppScreen.Start){
                    navController.popBackStack(AppScreen.Start.name,inclusive = false)
            }
                scope.launch {
                    drawerState.close()
                }
            },
            shape = RoundedCornerShape(0.dp,25.dp,25.dp,0.dp),
            modifier =  Modifier
                .padding(end = 10.dp)
            )
        optionsForUser.map {
            NavigationDrawerItem(
                label = { Text(text = stringResource(it.title))},
                selected = currentScreen == it,
                onClick = {
                    if(currentScreen != it){
                        navController.navigate(it.name) {
                            popUpTo(AppScreen.Start.name)
                        }
                    }
                    scope.launch {
                        drawerState.close()
                    }
                },
                shape = RoundedCornerShape(0.dp,25.dp,25.dp,0.dp),
                modifier =  Modifier
                    .padding(end = 10.dp)
            )
         }
    }

}

@Preview(device = "spec:parent=pixel_5")
@Composable
fun PreviewDrawerContent(){
    MyApplicationTheme(
        dynamicColor =false
    ){
        DrawerContent(currentScreen = AppScreen.Residents,
            navController = NavHostController(context = LocalContext.current),
            forceUpdateResidentList = {},
            scope = rememberCoroutineScope(),
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntruderAppBar(
    modifier: Modifier = Modifier,
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    logout : ()->Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    var expanded by remember { mutableStateOf(false) }

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
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        tint = MaterialTheme.colorScheme.onPrimary,
//                        contentDescription = stringResource(id = R.string.back_button)
//                    )
//                }
//            }

            val showDrawer = !(currentScreen == AppScreen.Loading || currentScreen == AppScreen.LoginPage)
            if(showDrawer){
                IconButton(onClick = {
                    scope.launch() {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = ""
                    )

                }
            }

        },
        actions = {
            if (currentScreen == AppScreen.Start){
                IconButton(onClick = {expanded = !expanded}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false}) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Logout")
                        },
                        onClick = { logout() })
                }
            }
        }
    )
}




