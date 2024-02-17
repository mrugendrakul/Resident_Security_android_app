package com.mrugendra.notificationtest.data

import android.util.Log
import com.mrugendra.notificationtest.Network.FirebaseAPI
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

val TAG = "MyFirebaseMessagingService"

interface DataRepository {
    suspend fun getToken(): String?
    suspend fun getResidents():List<residents>
    suspend fun getAlreadyExist(token:String):Boolean
    suspend fun updateDatabaseToken(token: String, name :String)
}

class NetworkDataRepository(
    private val apiService: FirebaseAPI,
) : DataRepository {
    override suspend fun getToken(): String {

        val token: String = coroutineScope {
            val token = async { apiService.getFCMToken() }
            token.await()
        }
        Log.d(TAG, "Token from network is $token")
        return token
    }

    override suspend fun getResidents(): List<residents> {
        val residents:List<residents> = coroutineScope {
            val residents = async { apiService.getResidentsList() }
            residents.await()
        }
        Log.d(TAG,"Resident list is $residents")
        return residents
    }

    override suspend fun getAlreadyExist(token:String): Boolean = apiService.AlreadyExist(token = token)

    override suspend fun updateDatabaseToken(token: String, name: String) {
        try {
            apiService.updateDatabaseToken(token,name)
        }
        catch(e:Exception){
            throw e
        }
    }
}