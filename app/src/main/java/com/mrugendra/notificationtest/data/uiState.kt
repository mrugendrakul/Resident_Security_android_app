package com.mrugendra.notificationtest.data

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.mrugendra.notificationtest.ui.AppScreen
import com.mrugendra.notificationtest.ui.DeliveryIdentifyStatus
import com.mrugendra.notificationtest.ui.IdentifiedStatus
import com.mrugendra.notificationtest.ui.ResidentStatus
import com.mrugendra.notificationtest.ui.UnidentifiedStatus
import java.util.Locale

data class uiState @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class) constructor(
    val token:String ="Loading...",
    val name:String = "",
    val FCMStatus:Boolean = false,
    val exist:Boolean = true,
    val successSend:Boolean = false,
    val nullName:Boolean = false,

    val residentStatus: ResidentStatus = ResidentStatus.Loading,
    val residentList : List<residents> = listOf(),
    val currentResident:residents = residents("","","",""),
    val residentRefreshIsLoading: Boolean = false,

    val username:String = "",
    val password:String = "",
    val isLoading:Boolean = false,
    val isLoginAvailable:Boolean =false,
    val startScreen:AppScreen = AppScreen.Loading,
    val startingError:Boolean = false,

    val openCalender:Boolean = false,
    val datePickerState : DatePickerState = DatePickerState(
        locale = Locale.ENGLISH,
//        initialSelectedDateMillis = Timestamp.now().toDate().time,

    ),
    val searchText:String = "",

    val identifiedRefresh:Boolean = false,
    val identifiedStatus: IdentifiedStatus = IdentifiedStatus.Loading,
    val identifiedList:List<Identified> = listOf(),

    val unidentifiedRefresh:Boolean = false,
    val unidentifiedStatus:UnidentifiedStatus = UnidentifiedStatus.Loading,
    val unidentifiedList: List<Unidentified> = listOf(),

    val deliveryRefresh:Boolean = false,
    val deliveryStatus:DeliveryIdentifyStatus = DeliveryIdentifyStatus.Loading,
    val deliverPersonList:List<deliveryPerson> = listOf()
)
