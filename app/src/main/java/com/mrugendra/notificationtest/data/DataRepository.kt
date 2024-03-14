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
    suspend fun updateDatabaseToken(token: String, username :String, password : String)

    suspend fun checkCredentials(username: String,password: String):Boolean

    suspend fun logoutUser(token: String)

    suspend fun getIdentified():List<Identified>

    suspend fun getUnidentified():List<Unidentified>

    suspend fun getDeliveryPeopleList():List<deliveryPerson>
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

    override suspend fun getAlreadyExist(token:String): Boolean {
        return try {
            apiService.AlreadyExist(token = token)
        }catch (e:Exception){
            Log.e(TAG,"backed unable to fetch in data")
            throw e
        }
    }

    override suspend fun updateDatabaseToken(token: String, username: String, password: String) {
        try {
            apiService.updateDatabaseToken(token, username,password)
        }
        catch(e:Exception){
            throw e
        }
    }

    override suspend fun checkCredentials(username: String, password: String): Boolean {
        return try { !apiService.checkUsersReturnFalse(username, password) }
        catch(e:Exception){
            throw e
        }
    }

    override suspend fun logoutUser(token: String) {
        try{
            apiService.signOutUser(currentToken = token)
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getIdentified(): List<Identified> {
        val identified:List<Identified> = coroutineScope {
            val residents = async { apiService.getIdentifiedList() }
            residents.await()
        }
        Log.d(TAG,"Identified list is $identified")
        return identified
    }

    override suspend fun getUnidentified(): List<Unidentified> {
        val unidentified:List<Unidentified> = coroutineScope {
            val residents = async { apiService.getUnidentifiedList() }
            residents.await()
        }
        Log.d(TAG,"Unidentified list is $unidentified")
        return unidentified
    }

    override suspend fun getDeliveryPeopleList(): List<deliveryPerson> {
        val deliveryPeople:List<deliveryPerson> = coroutineScope {
            val residents = async { apiService.getDeliveryPeopleList() }
            residents.await()
        }
        Log.d(TAG,"Delivery list is $deliveryPeople")
        return deliveryPeople
    }
}