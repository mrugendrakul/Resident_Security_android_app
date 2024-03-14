package com.mrugendra.notificationtest.Network

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.messaging.FirebaseMessaging
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.data.deliveryPerson
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

    suspend fun getIdentifiedList():List<Identified>

    suspend fun getUnidentifiedList():List<Unidentified>

    suspend fun getDeliveryPeopleList():List<deliveryPerson>
}

val TAG = "MyFirebaseMessagingService"

class NetworkFirebaseAPI(
    val db : FirebaseFirestore,
    val tokenCollection : CollectionReference,
    val residentCollection : CollectionReference,
    val userLoginCollection :CollectionReference,
    val identifiedCollection: CollectionReference,
    val unidentifiedCollection:CollectionReference,
    val deliveryCollection:CollectionReference
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
            throw e
//            false
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


    override suspend fun getIdentifiedList(): List<Identified> {
        val IdentifiedList = mutableListOf<Identified>()
        Log.d(TAG,"identified fetching from api")
        try{
            val querySnapshop  = identifiedCollection
                .get()
                .await()
            for (document in querySnapshop.documents){
                val name = document.getString("name")?:""
                val id = document.getString("id")?:""
                val time = document.getTimestamp("timeStamp")?: Timestamp.now()

                val identf = Identified(id,name,time)
                Log.d(TAG,"Resident with id : ${identf.id} got")
                IdentifiedList.add(identf)
            }
        }
        catch( e:Exception ){
            Log.d(TAG,"Failed to get the idenfied data")
            throw e
        }
        Log.d(TAG,"identified is empyt : ${IdentifiedList.isEmpty()}")
        return if(IdentifiedList.isNotEmpty()) IdentifiedList
        else throw Exception()
    }

    override suspend fun getUnidentifiedList(): List<Unidentified> {
        val UnidentifiedList = mutableListOf<Unidentified>()
        Log.d(TAG,"Unidentified fetching from api")
        try{
            val querySnapshot  = unidentifiedCollection
                .get()
                .await()
            for (document in querySnapshot.documents){
                val image = document.getString("imageLink")?:""
                val time = document.getTimestamp("timeStamp")?: Timestamp.now()

                val unidentf = Unidentified(image,time)
                Log.d(TAG,"Fetched the Unidentified")
                UnidentifiedList.add(unidentf)
            }
        }
        catch( e:Exception ){
            Log.d(TAG,"Failed to get the data")
            throw e
        }
        Log.d(TAG,"Unidentified is empyt : ${UnidentifiedList.isEmpty()}")
        return if(UnidentifiedList.isNotEmpty()) UnidentifiedList
        else throw Exception()
    }

    override suspend fun getDeliveryPeopleList(): List<deliveryPerson> {
        val deliverPersonList = mutableListOf<deliveryPerson>()
        Log.d(TAG,"Delivery fetching from api")
        try{
            val querySnapshot  = deliveryCollection
                .get()
                .await()
            for (document in querySnapshot.documents){
                val image = document.getString("imageLink")?:""
                val time = document.getTimestamp("timeStamp")?: Timestamp.now()

                val unidentf = deliveryPerson(image,time)
                Log.d(TAG,"Fetched the Delivery")
                deliverPersonList.add(unidentf)
            }
        }
        catch( e:Exception ){
            Log.d(TAG,"Failed to get the data")
            throw e
        }
        Log.d(TAG,"Delivery is empyt : ${deliverPersonList.isEmpty()}")
        return if(deliverPersonList.isNotEmpty()) deliverPersonList
        else throw Exception()
    }
}