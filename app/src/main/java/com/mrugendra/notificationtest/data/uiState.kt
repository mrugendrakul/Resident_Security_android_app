package com.mrugendra.notificationtest.data

import com.mrugendra.notificationtest.ui.Notfi
import com.mrugendra.notificationtest.ui.ResidentStatus

data class uiState(
    val token:String ="Loading...",
    val name:String = "",
    val FCMStatus:Boolean = false,
    val exist:Boolean = false,
    val successSend:Boolean = false,
    val nullName:Boolean = false,
    val residentStatus: ResidentStatus = ResidentStatus.Loading,
    val currentResidentId:String = ""
)
