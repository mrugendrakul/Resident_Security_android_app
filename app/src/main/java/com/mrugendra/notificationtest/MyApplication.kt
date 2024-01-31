package com.mrugendra.notificationtest

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.mrugendra.notificationtest.data.AppContainer
import com.mrugendra.notificationtest.data.DefaultAppContainer

class MyApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        try{ FirebaseApp.initializeApp(this) }
        catch (e:Exception){
            Log.d("MyFirebaseMessagingService","Error in initialinzing")
        }
        container = DefaultAppContainer()
    }

}