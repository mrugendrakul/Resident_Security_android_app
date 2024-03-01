package com.mrugendra.notificationtest.data

import com.google.firebase.firestore.firestore
import com.mrugendra.notificationtest.Network.FirebaseAPI
import com.mrugendra.notificationtest.Network.NetworkFirebaseAPI

interface AppContainer {
    val dataRepository : DataRepository
    val apiService:FirebaseAPI
}

class DefaultAppContainer : AppContainer{
    private val db = com.google.firebase.Firebase.firestore
    private val tokenCollection = db.collection("residents")
    private val residentCollection = db.collection("KnownPeople")
    private val userLoginCollection = db.collection("userLogin")

//    override val apiService:FirebaseAPI = NetworkFirebaseAPI(db, residentCollection)
    override val apiService:FirebaseAPI = NetworkFirebaseAPI(
    db,
    tokenCollection = tokenCollection,
    residentCollection = residentCollection,
    userLoginCollection = userLoginCollection)

    override val dataRepository: DataRepository = NetworkDataRepository(apiService)
}