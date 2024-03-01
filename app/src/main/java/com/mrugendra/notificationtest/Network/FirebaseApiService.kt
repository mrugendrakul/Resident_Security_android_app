package com.mrugendra.notificationtest.Network

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.messaging.FirebaseMessaging
import com.mrugendra.notificationtest.data.residents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface FirebaseAPI{
    suspend fun getFCMToken():String
    suspend fun AlreadyExist(token:String):Boolean
    suspend fun updateDatabaseToken(token:String, username:String,password: String)
    suspend fun getResidentsList():List<residents>

    suspend fun signOutUser(currentToken : String)

    suspend fun checkUsersReturnFalse(username: String, password: String):Boolean
}

val TAG = "MyFirebaseMessagingService"

class NetworkFirebaseAPI(
    val db : FirebaseFirestore,
    val tokenCollection : CollectionReference,
    val residentCollection : CollectionReference,
    val userLoginCollection :CollectionReference
):FirebaseAPI{

    override suspend fun getFCMToken():String {
        return withContext(Dispatchers.IO) {
            return@withContext suspendCoroutine<String> {
                continuation->
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                continuation.resume("Failed to fetch the token")
                                return@OnCompleteListener
                            }

                            val token = task.result
                            continuation.resume(token?:"Token is null")
                            Log.d(TAG, "token: $token")
                        })

            }
        }
    }

    override suspend fun AlreadyExist(token:String):Boolean{
        return try {
            val result = tokenCollection
                .whereEqualTo("token",token)
                .get(Source.SERVER)
                .await()
            Log.d(TAG,"${result.documents}")
            result.isEmpty
        }catch (e:Exception){
            Log.e(TAG,"Backend: unable to fetch")
            false
        }
    }

    override suspend fun updateDatabaseToken(token: String, username: String, password: String) {
//        AlreadyExist(token)
        Log.d(TAG, "Database update called")
        val resident = hashMapOf(
            "username" to username,
            "password" to password,
            "token" to token
        )

        tokenCollection
            .document(token)
            .set(resident)
            .addOnSuccessListener { documentRef ->
                Log.d(TAG, "Document updated with ID: ${documentRef.toString()}")

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error adding : ", e)
                throw e
//                return@addOnFailureListener

            }
    }

    override suspend fun getResidentsList(): List<residents> {
        val residentsList = mutableListOf<residents>()
    Log.d(TAG,"residents fetching from api")
        try{
            val querySnapshop  = residentCollection
                .get()
                .await()
            for (document in querySnapshop.documents){
                val name = document.getString("name")?:""
                val id = document.getString("id")?:""
                val info = document.getString("info")?:""
                val profilePhoto = document.getString("profilePhoto")?:""

                val resident = residents(name,id,info,profilePhoto)
                Log.d(TAG,"Resident with id : ${resident.id} got")
                residentsList.add(resident)
            }
        }
        catch( e:Exception ){
            Log.d(TAG,"Failed to get the data")
            throw e
        }
        Log.d(TAG,"residents is empyt : ${residentsList.isEmpty()}")
        return if(residentsList.isNotEmpty()) residentsList
            else throw Exception()
    }

    override suspend fun signOutUser(currentToken: String) {
        try{
            tokenCollection
                .document(currentToken)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "successfully deleted $currentToken")
                }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun checkUsersReturnFalse(username: String, password: String): Boolean {
        return try{
            val result = tokenCollection
                .whereEqualTo("username" , username)
                .whereEqualTo("password",password)
                .get()

            result.await().isEmpty
        }catch (e:Exception){
            throw e
        }
    }

}