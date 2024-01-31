package com.mrugendra.notificationtest.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mrugendra.notificationtest.Network.FirebaseAPI
import com.mrugendra.notificationtest.Network.NetworkFirebaseAPI
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update

val TAG = "MyFirebaseMessagingService"

interface TokenRepository {
    suspend fun getToken():String?
}

class NetworkTokenRepository(
    private val apiService: FirebaseAPI
):TokenRepository{
    override suspend fun getToken(): String {

        val token:String=coroutineScope{
            val token = async {apiService.getFCMToken()}
            token.await()
        }
        Log.d(TAG,"Token from network is $token")
        return token
    }
}