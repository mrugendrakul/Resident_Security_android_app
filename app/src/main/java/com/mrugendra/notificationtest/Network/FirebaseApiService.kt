package com.mrugendra.notificationtest.Network

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.security.auth.callback.Callback
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface FirebaseAPI{
    suspend fun getFCMToken():String
    suspend fun AlreadyExist(token:String):Boolean
    suspend fun updateDatabaseToken(token:String, name:String)
}

val TAG = "MyFirebaseMessagingService"

class NetworkFirebaseAPI(
    val db : FirebaseFirestore,
    val residentCollection : CollectionReference
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
            val result = db.collection("residents")
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

    override suspend fun updateDatabaseToken(token: String, name: String) {
//        AlreadyExist(token)
        Log.d(TAG, "Database update called")
        val resident = hashMapOf(
            "name" to name,
            "token" to token
        )

        residentCollection
            .add(resident)
            .addOnSuccessListener { documentRef ->
                Log.d(TAG, "Document updated with ID: ${documentRef.id}")

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error adding : ", e)
                return@addOnFailureListener
            }
    }

}