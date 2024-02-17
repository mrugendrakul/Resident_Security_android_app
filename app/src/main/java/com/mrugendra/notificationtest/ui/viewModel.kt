package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mrugendra.notificationtest.MyApplication
import com.mrugendra.notificationtest.Network.FirebaseAPI
import com.mrugendra.notificationtest.data.DataRepository
import com.mrugendra.notificationtest.data.residents
import com.mrugendra.notificationtest.data.uiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ResidentStatus{
    data class Success(val residents:List<residents>):ResidentStatus
    object Error:ResidentStatus
    object Loading:ResidentStatus
}
@OptIn(ExperimentalMaterialApi::class)
class Notfi(
    private val dataRepository: DataRepository
): ViewModel(){
    val TAG = "MyFirebaseMessagingService"
    private var _uiState = MutableStateFlow(uiState())
    private set

    var notUiState :StateFlow<uiState> = _uiState.asStateFlow()

    private fun getFCMToken() {
        Log.d(TAG,"Inside teh getfcmtoken")
        viewModelScope.launch{
            try{
                val ton = dataRepository.getToken()
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
                    !dataRepository.getAlreadyExist(notUiState.value.token)
                } catch (e: Exception) {
                    _uiState.update { current -> current.copy(successSend = false) }
                    false
                }
                Log.d(TAG, "Exits $exists")
                _uiState.update { current -> current.copy(exist = exists) }

                if (exists == false) {
                    val send: Boolean = try {
                        dataRepository.updateDatabaseToken(
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
                val tokenRepository = application.container.dataRepository
                Notfi(
                    dataRepository = tokenRepository
                    )
            }
        }
    }

    fun UpdateResidentsList(
        isForced:Boolean = false
    ){
        if(_uiState.value.residentStatus == ResidentStatus.Error || _uiState.value.residentStatus == ResidentStatus.Loading || isForced){
            viewModelScope.launch() {
                _uiState.update { current->current.copy(
                    residentRefreshIsLoading = true,
                    residentStatus = ResidentStatus.Loading
                ) }
                delay(1000)
                try {
                    val residentsList = dataRepository.getResidents()
                    Log.d(TAG, "called the update")
                    _uiState.update { current ->
                        current.copy(
                            residentRefreshIsLoading = false,
                            residentStatus = ResidentStatus.Success(
                                residentsList
                            )
                        )
                    }
                    Log.d(TAG, "${_uiState.value.residentStatus}")
                } catch (e: Exception) {
                    Log.d(TAG, "Unable to fetch the list")
                    _uiState.update { current -> current.copy(residentStatus = ResidentStatus.Error) }
                }
            }
        }
    }


}
