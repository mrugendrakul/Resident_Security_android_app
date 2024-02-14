package com.mrugendra.notificationtest.ui

import android.annotation.SuppressLint
import android.text.Spannable.Factory
import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.mrugendra.notificationtest.MyApplication
import com.mrugendra.notificationtest.Network.FirebaseAPI
import com.mrugendra.notificationtest.Network.NetworkFirebaseAPI
import com.mrugendra.notificationtest.data.NetworkTokenRepository
import com.mrugendra.notificationtest.data.TokenRepository
import com.mrugendra.notificationtest.data.residents
import com.mrugendra.notificationtest.data.uiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class Notfi(
    private val apiService : FirebaseAPI,
    private val tokenRepository: TokenRepository
): ViewModel(){
    val TAG = "MyFirebaseMessagingService"
    var _uiState = MutableStateFlow(uiState())
    private set

    var notUiState :StateFlow<uiState> = _uiState.asStateFlow()

    private fun getFCMToken() {
        Log.d(TAG,"Inside teh getfcmtoken")
        viewModelScope.launch{
            try{
                val ton = tokenRepository.getToken()
                Log.d("TAG","Token is $ton")
                _uiState.update { current ->current.copy(token = ton?:"Null token") }
            }
            catch (e:Exception){
                Log.d(TAG,"Exception to get the token")
                _uiState.update { current ->current.copy(token = "Unable to get fetch the token") }
            }

        }

    }

    init {
            getFCMToken()
    }

    fun updateName(name:String){


            _uiState.update { current ->
                current.copy(
                    name = name
                )
            }

    }

    fun updateToken(){
        if(_uiState.value.name==""){
            _uiState.update { current->current.copy(nullName = true) }
        }
        else {
            _uiState.update { current->current.copy(nullName = false) }
            viewModelScope.launch(Dispatchers.IO) {
                val exists = try {
                    !apiService.AlreadyExist(notUiState.value.token)
                } catch (e: Exception) {
                    _uiState.update { current -> current.copy(successSend = false) }
                    false
                }
                Log.d(TAG, "Exits $exists")
                _uiState.update { current -> current.copy(exist = exists) }

                if (exists == false) {
                    val send: Boolean = try {
                        apiService.updateDatabaseToken(
                            token = notUiState.value.token,
                            name = notUiState.value.name
                        )
                        true
                    } catch (e: Exception) {
                        Log.e(TAG, "Unable to send the token")
                        false
                    }
                    _uiState.update { current -> current.copy(successSend = send) }
                }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyApplication)
                val apiService = application.container.apiService
                val tokenRepository = application.container.tokenRepository
                Notfi(
                    apiService = apiService,
                    tokenRepository = tokenRepository
                    )
            }
        }
    }

}
