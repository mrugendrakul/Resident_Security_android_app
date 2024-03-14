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
    private val identifiedCollection = db.collection("IdentifiedPeople")
    private val unidentifiedCollection = db.collection("UnkownCollection")
    private val deliveryCollection = db.collection("DeliveryPeople")
//    override val apiService:FirebaseAPI = NetworkFirebaseAPI(db, residentCollection)
    override val apiService:FirebaseAPI = NetworkFirebaseAPI(
    db,
    tokenCollection = tokenCollection,
    residentCollection = residentCollection,
    userLoginCollection = userLoginCollection,
    identifiedCollection = identifiedCollection,
    unidentifiedCollection= unidentifiedCollection,
    deliveryCollection = deliveryCollection
)

    override val dataRepository: DataRepository = NetworkDataRepository(apiService)
}