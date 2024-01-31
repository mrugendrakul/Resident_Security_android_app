package com.mrugendra.notificationtest.data

data class uiState(
    val token:String ="Loading...",
    val name:String = "",
    val FCMStatus:Boolean = false,
    val exist:Boolean = false,
    val successSend:Boolean = false,
    val nullName:Boolean = false
)
