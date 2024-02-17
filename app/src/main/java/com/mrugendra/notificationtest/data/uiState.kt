package com.mrugendra.notificationtest.data

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.ResidentStatus

data class uiState @OptIn(ExperimentalMaterialApi::class) constructor(
    val token:String ="Loading...",
    val name:String = "",
    val FCMStatus:Boolean = false,
    val exist:Boolean = false,
    val successSend:Boolean = false,
    val nullName:Boolean = false,
    val residentStatus: ResidentStatus = ResidentStatus.Loading,
    val currentResident:residents = residents("","","",""),
    val residentRefreshIsLoading: Boolean = false
)
