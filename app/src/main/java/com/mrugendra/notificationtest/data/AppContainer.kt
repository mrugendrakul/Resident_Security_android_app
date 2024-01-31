package com.mrugendra.notificationtest.data

import com.google.firebase.firestore.firestore
import com.mrugendra.notificationtest.Network.FirebaseAPI
import com.mrugendra.notificationtest.Network.NetworkFirebaseAPI

interface AppContainer {
    val tokenRepository : TokenRepository
    val apiService:FirebaseAPI
}

class DefaultAppContainer : AppContainer{
//    private val db = com.google.firebase.Firebase.firestore
//    private val residentCollection = db.collection("residents")

//    override val apiService:FirebaseAPI = NetworkFirebaseAPI(db, residentCollection)
    override val apiService:FirebaseAPI = NetworkFirebaseAPI()


    override val tokenRepository: TokenRepository = NetworkTokenRepository(apiService)
}