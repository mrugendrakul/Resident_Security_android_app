package com.mrugendra.notificationtest.data

import androidx.compose.material.ExperimentalMaterialApi
import com.mrugendra.notificationtest.ui.AppScreen
import com.mrugendra.notificationtest.ui.ResidentStatus

data class uiState @OptIn(ExperimentalMaterialApi::class) constructor(
    val token:String ="Loading...",
    val name:String = "",
    val FCMStatus:Boolean = false,
    val exist:Boolean = true,
    val successSend:Boolean = false,
    val nullName:Boolean = false,
    val residentStatus: ResidentStatus = ResidentStatus.Loading,
    val currentResident:residents = residents("","","",""),
    val residentRefreshIsLoading: Boolean = false,
    val username:String = "",
    val password:String = "",
    val isLoading:Boolean = false,
    val isLoginAvailable:Boolean =false ,
    val startScreen:AppScreen = AppScreen.Loading
)
