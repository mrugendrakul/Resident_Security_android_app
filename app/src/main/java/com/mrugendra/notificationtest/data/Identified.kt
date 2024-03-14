package com.mrugendra.notificationtest.data

import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class Identified(
    val id:String,
    val name:String,
    val time:Timestamp
)
