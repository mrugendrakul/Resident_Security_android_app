package com.mrugendra.notificationtest

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.mrugendra.notificationtest.data.AppContainer
import com.mrugendra.notificationtest.data.DefaultAppContainer
import com.mrugendra.notificationtest.BuildConfig

class MyApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        try{ FirebaseApp.initializeApp(this) }
        catch (e:Exception){
            Log.d("MyFirebaseMessagingService","Error in initialinzing")
        }
//        if(BuildConfig.DEBUG){
//            val firestore = Firebase.firestore
//            firestore.useEmulator("10.0.2.2", 8080)
//
//            firestore.firestoreSettings = firestoreSettings {
//                isPersistenceEnabled = false
//            }
//        }

        container = DefaultAppContainer()
    }

}