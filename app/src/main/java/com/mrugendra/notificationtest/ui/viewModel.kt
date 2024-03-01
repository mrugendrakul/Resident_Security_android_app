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
        _uiState.update { it.copy(
            isLoginAvailable = false,
            startScreen = AppScreen.Loading,
            isLoading = true
        ) }
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
            val exists = try {
                !dataRepository.getAlreadyExist(notUiState.value.token)
            } catch (e: Exception) {
                false
            }

            if(exists){
                _uiState.update { it.copy(
                    isLoginAvailable = true,
                    startScreen = AppScreen.Start,
                    isLoading = false
                ) }
            }else{
                _uiState.update { it.copy(
                    isLoginAvailable = false,
                    startScreen = AppScreen.LoginPage,
                    isLoading = false
                ) }
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
        if(_uiState.value.username==""){
            _uiState.update { current->current.copy(
                nullName = true
            ) }
        }
        else {
            _uiState.update {
                current->current.copy(
                    nullName = false,
                    isLoading = true,
                    successSend = true,
                    exist = true
                ) }
            viewModelScope.launch(Dispatchers.IO) {
                val exists = try {
                    dataRepository.checkCredentials(_uiState.value.username,_uiState.value.password)
                } catch (e: Exception) {
                    _uiState.update { current -> current.copy(
                        successSend = false,
                        isLoading = false
                    ) }
                    false
                }
                Log.d(TAG, "Exits $exists")
//                _uiState.update { current -> current.copy(
//                    exist = exists,
//                    isLoading = false
//                ) }

                if (exists == true) {
                    val send: Boolean = try {
                        dataRepository.updateDatabaseToken(
                            token = notUiState.value.token,
                            username = notUiState.value.username,
                            password = notUiState.value.password,
                        )
                        true

                    } catch (e: Exception) {
                        Log.e(TAG, "Unable to send the token")
                        false
                    }
                    _uiState.update { current -> current.copy(
                        successSend = send,
                        isLoading = false
                    ) }
                    _uiState.update { it.copy(
                        isLoginAvailable = true,
                        startScreen = AppScreen.Start,
//                        isLoading = false
                    ) }
                }
                else{
                    Log.d(TAG,"user does not exit")
                    _uiState.update { it.copy(
                        isLoginAvailable = false,
                        exist = false,
                        successSend = false,
                        startScreen = AppScreen.LoginPage,
                        isLoading = false
                    ) }
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
                    val sortedResidentsList = residentsList.sortedBy { it.name }
                    _uiState.update { current ->
                        current.copy(
                            residentRefreshIsLoading = false,
                            residentStatus = ResidentStatus.Success(
                                sortedResidentsList
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

    fun UpdateCurrentResidentAndIdentifiedId(
        ID:String,
        dataBase:ResidentStatus = _uiState.value.residentStatus
    )
    {
        when(dataBase){
            is ResidentStatus.Success -> {
                val data = dataBase.residents
                val retData:residents? = data.find { it.id == ID }
                _uiState.update { current->current.copy(
                    currentResident = retData?:residents("Does not found","does not exits","Please don't look here","")
                ) }
            }
            else ->{
                _uiState.update { current->current.copy(
                    currentResident = residents("Does not found","does not exits","Please don't look here","")
                ) }
            }
        }

    }

    fun updateUsername(username:String){
        _uiState.update { it.copy(
            username = username
        ) }
    }

    fun updatePassword(password:String){
        _uiState.update { it.copy(
            password = password
        ) }
    }

    fun logoutUser(){
        Log.d(TAG,"started logout")
        _uiState.update { it.copy(
            isLoading = true
        ) }
        viewModelScope.launch(){
            try {
                dataRepository.logoutUser(_uiState.value.token)
                _uiState.update { it.copy(
                    isLoading = false,
                    startScreen = AppScreen.LoginPage,
                    username = "",
                    password = "",
                ) }
                _uiState.update { it.copy(
                    successSend = false,
                    isLoading = false
                ) }
            }catch (e:Exception){
                Log.d(TAG,"UNABLE TO LOGOUT")
            }
        }
    }

}
