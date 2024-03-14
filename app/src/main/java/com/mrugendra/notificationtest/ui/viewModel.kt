package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.Timestamp
import com.mrugendra.notificationtest.MyApplication
import com.mrugendra.notificationtest.data.DataRepository
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.data.deliveryPerson
import com.mrugendra.notificationtest.data.residents
import com.mrugendra.notificationtest.data.uiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

sealed interface ResidentStatus{
    data class Success(val residents:List<residents>):ResidentStatus
    object Error:ResidentStatus
    object Loading:ResidentStatus
}

sealed interface IdentifiedStatus{
    data class Success(val listOfIdentified:List<Identified>):IdentifiedStatus

    object Error:IdentifiedStatus

    object Loading:IdentifiedStatus
}

sealed interface UnidentifiedStatus{
    data class Success(val listOfUnidentified:List<Unidentified>):UnidentifiedStatus
    object Error:UnidentifiedStatus
    object Loading:UnidentifiedStatus
}

sealed interface DeliveryIdentifyStatus{
    data class Success(val listOfDeliveryPerson:List<deliveryPerson>):DeliveryIdentifyStatus
    object Error:DeliveryIdentifyStatus
    object Loading:DeliveryIdentifyStatus
}
enum class UserExist(){
    exist,
    notExist,
    errorToFind
}


@OptIn(ExperimentalMaterial3Api::class)
class Notfi(
    private val dataRepository: DataRepository
): ViewModel() {
    val TAG = "MyFirebaseMessagingService"
    private var _uiState = MutableStateFlow(uiState())
        private set

    var notUiState: StateFlow<uiState> = _uiState.asStateFlow()

    fun getFCMToken() {
        _uiState.update {
            it.copy(
                isLoginAvailable = false,
                startScreen = AppScreen.Loading,
                isLoading = true,
                startingError = false
            )
        }
        Log.d(TAG, "Inside teh getfcmtoken")
        viewModelScope.launch {
            try {
                val ton = dataRepository.getToken()
                Log.d("TAG", "Token is $ton")
                _uiState.update { current -> current.copy(token = ton ?: "Null token") }
            } catch (e: Exception) {
                Log.d(TAG, "Exception to get the token")
                _uiState.update { current -> current.copy(token = "Unable to get fetch the token") }
            }
            val exists: UserExist = try {
                if (!dataRepository.getAlreadyExist(notUiState.value.token)) {
                    UserExist.exist
                } else {
                    UserExist.notExist
                }
            } catch (e: Exception) {
                Log.e(TAG, "error in viewmodel for fetching")
                UserExist.errorToFind
            }

            when (exists) {
                UserExist.exist -> {
                    _uiState.update {
                        it.copy(
                            isLoginAvailable = true,
                            startScreen = AppScreen.Start,
                            isLoading = false
                        )
                    }
                }

                UserExist.notExist -> {
                    _uiState.update {
                        it.copy(
                            isLoginAvailable = false,
                            startScreen = AppScreen.LoginPage,
                            isLoading = false
                        )
                    }
                }

                UserExist.errorToFind -> {
                    _uiState.update {
                        it.copy(
                            isLoginAvailable = false,
//                        startScreen = AppScreen.LoginPage,
                            isLoading = false,
                            startingError = true
                        )
                    }
                }
            }
        }

    }

    init {
        getFCMToken()
    }

    fun updateName(name: String) {
        _uiState.update { current ->
            current.copy(
                name = name
            )
        }
    }

    fun updateToken() {
        if (_uiState.value.username == "") {
            _uiState.update { current ->
                current.copy(
                    nullName = true
                )
            }
        } else {
            _uiState.update { current ->
                current.copy(
                    nullName = false,
                    isLoading = true,
                    successSend = true,
                    exist = true
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                val exists = try {
                    dataRepository.checkCredentials(
                        _uiState.value.username,
                        _uiState.value.password
                    )
                } catch (e: Exception) {
                    _uiState.update { current ->
                        current.copy(
                            successSend = false,
                            isLoading = false
                        )
                    }
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
                    _uiState.update { current ->
                        current.copy(
                            successSend = send,
                            isLoading = false
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isLoginAvailable = true,
                            startScreen = AppScreen.Start,
//                        isLoading = false
                        )
                    }
                } else {
                    Log.d(TAG, "user does not exit")
                    _uiState.update {
                        it.copy(
                            isLoginAvailable = false,
                            exist = false,
                            successSend = false,
                            startScreen = AppScreen.LoginPage,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    companion object {
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
        isForced: Boolean = false
    ) {
        if (_uiState.value.residentStatus == ResidentStatus.Error || _uiState.value.residentStatus == ResidentStatus.Loading || isForced) {
            viewModelScope.launch() {
                _uiState.update { current ->
                    current.copy(
                        residentRefreshIsLoading = true,
                        residentStatus = ResidentStatus.Loading
                    )
                }
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
                            ),
                            residentList = sortedResidentsList
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
        ID: String,
        dataBase: ResidentStatus = _uiState.value.residentStatus
    ) {
        when (dataBase) {
            is ResidentStatus.Success -> {
                val data = dataBase.residents
                val retData: residents? = data.find { it.id == ID }
                _uiState.update { current ->
                    current.copy(
                        currentResident = retData ?: residents(
                            "Does not found",
                            "does not exits",
                            "Please don't look here",
                            ""
                        ),
                    )
                }
            }

            else -> {
                _uiState.update { current ->
                    current.copy(
                        currentResident = residents(
                            "Does not found",
                            "does not exits",
                            "Please don't look here",
                            ""
                        )
                    )
                }
            }
        }

    }

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun logoutUser() {
        Log.d(TAG, "started logout")
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch() {
            try {
                dataRepository.logoutUser(_uiState.value.token)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startScreen = AppScreen.LoginPage,
                        username = "",
                        password = "",
                    )
                }
                _uiState.update {
                    it.copy(
                        successSend = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "UNABLE TO LOGOUT")
            }
        }
    }


    fun openCalender() {
        _uiState.update {
            it.copy(
                openCalender = !it.openCalender
            )
        }
    }

    fun searchIdentified() {
        when (val resIden = _uiState.value.identifiedStatus) {
            is IdentifiedStatus.Success -> {
                val newList = resIden.listOfIdentified.filter { t ->
                    t.name.contains(_uiState.value.searchText, ignoreCase = true)
                            && t.time.toDate().date == Date(
                        _uiState.value.datePickerState.selectedDateMillis ?: 10
                    ).date
                }
                Log.d(TAG, "${newList}")
                _uiState.update {
                    it.copy(
                        identifiedList = newList
                    )
                }

            }

            IdentifiedStatus.Error -> TODO()
            IdentifiedStatus.Loading -> TODO()
        }
    }

    fun searchResidents() {
        _uiState.update { it.copy(
            residentRefreshIsLoading = true
        ) }
        when (val resSts = _uiState.value.residentStatus) {
            is ResidentStatus.Success -> {
                val newList = resSts.residents.filter { t ->
                    t.name.contains(_uiState.value.searchText, ignoreCase = true)
                }
                Log.d(TAG, "${newList}")
                _uiState.update {
                    it.copy(
                        residentList = newList,
                        residentRefreshIsLoading = false
                    )
                }

            }

            ResidentStatus.Error -> TODO()
            ResidentStatus.Loading -> TODO()
        }
    }

    fun onSearchText(Text: String) {
        _uiState.update {
            it.copy(
                searchText = Text
            )
        }
    }

    fun UpdateIdentifiedList(
        isForced: Boolean = false
    ) {
        if (_uiState.value.identifiedStatus == IdentifiedStatus.Error || _uiState.value.identifiedStatus == IdentifiedStatus.Loading || isForced) {
            viewModelScope.launch() {
                _uiState.update { current ->
                    current.copy(
                        identifiedRefresh = true,
                        identifiedStatus = IdentifiedStatus.Loading
                    )
                }
                delay(1000)
                try {
                    val identifiedList = dataRepository.getIdentified()
                    Log.d(TAG, "called the update for identified")
                    val sortedIdentifiedList = identifiedList.sortedBy { it.time }

                    _uiState.update { current ->
                        current.copy(
                            identifiedRefresh = false,
                            identifiedStatus = IdentifiedStatus.Success(
                                sortedIdentifiedList
                            ),

                            identifiedList = sortedIdentifiedList.filter { t ->
                                t.time.toDate().date == Timestamp.now().toDate().date
                            }
                        )
                    }
                    Log.d(TAG, "${_uiState.value.identifiedList}")
                } catch (e: Exception) {
                    Log.d(TAG, "Unable to fetch the Identified list")
                    _uiState.update { current -> current.copy(identifiedStatus = IdentifiedStatus.Error) }
                }
            }
        }
    }

    fun UpdateUnidentifiedList(
        isForced: Boolean = false
    ) {
        if (_uiState.value.unidentifiedStatus == UnidentifiedStatus.Error || _uiState.value.unidentifiedStatus == UnidentifiedStatus.Loading || isForced) {
            viewModelScope.launch() {
                _uiState.update { current ->
                    current.copy(
                        unidentifiedRefresh = true,
                        unidentifiedStatus = UnidentifiedStatus.Loading
                    )
                }
                delay(1000)
                try {
                    val unidentifiedList = dataRepository.getUnidentified()
                    Log.d(TAG, "called the update for unidentified")
                    val sortedUnidentifiedList = unidentifiedList.sortedBy { it.time }

                    _uiState.update { current ->
                        current.copy(
                            unidentifiedRefresh = false,
                            unidentifiedStatus = UnidentifiedStatus.Success(
                                sortedUnidentifiedList
                            ),

                            unidentifiedList = sortedUnidentifiedList.filter { t ->
                                t.time.toDate().date == Timestamp.now().toDate().date
                            }
                        )
                    }
                    Log.d(TAG, "${_uiState.value.unidentifiedList}")
                } catch (e: Exception) {
                    Log.d(TAG, "Unable to fetch the Unidentified list")
                    _uiState.update { current -> current.copy(identifiedStatus = IdentifiedStatus.Error) }
                }
            }
        }
    }

    fun updateDeliveryPeopleList(
        isForced: Boolean
    ){
        if (_uiState.value.deliveryStatus == DeliveryIdentifyStatus.Error || _uiState.value.deliveryStatus == DeliveryIdentifyStatus.Loading || isForced) {
            viewModelScope.launch() {
                _uiState.update { current ->
                    current.copy(
                        deliveryRefresh = true,
                        deliveryStatus = DeliveryIdentifyStatus.Loading
                    )
                }
//                delay(1000)
                try {
                    val delPeoList = dataRepository.getDeliveryPeopleList()
                    Log.d(TAG, "called the update for Delivery")
                    val sortedDeliveryList = delPeoList.sortedBy { it.timeStamp }

                    _uiState.update { current ->
                        current.copy(
                            deliveryRefresh = false,
                            deliveryStatus = DeliveryIdentifyStatus.Success(
                                sortedDeliveryList
                            ),

                            deliverPersonList = sortedDeliveryList.filter { t ->
                                t.timeStamp.toDate().date == Timestamp.now().toDate().date
                            }
                        )
                    }
                    Log.d(TAG, "Delivery List in viewModel : ${_uiState.value.deliverPersonList}")
                    Log.d(TAG,"Delivery list success : ${_uiState.value.deliveryStatus}")
                } catch (e: Exception) {
                    Log.d(TAG, "Unable to fetch the Delivery list")
                    _uiState.update { current -> current.copy(identifiedStatus = IdentifiedStatus.Error) }
                }
            }
        }
    }

    fun searchUnidentified() {
        _uiState.update { it.copy(
            unidentifiedRefresh = true
        ) }
        when (val resIden = _uiState.value.unidentifiedStatus) {
            is UnidentifiedStatus.Success -> {
                val newList = resIden.listOfUnidentified.filter { t ->
                            t.time.toDate().date == Date(
                        _uiState.value.datePickerState.selectedDateMillis ?: 10
                    ).date
                }
                Log.d(TAG, "${newList}")
                _uiState.update {
                    it.copy(
                        unidentifiedList = newList,
                        unidentifiedRefresh = false
                    )
                }

            }

            UnidentifiedStatus.Error -> TODO()
            UnidentifiedStatus.Loading -> TODO()
        }
    }

    fun searchDeliveryList(){

        when (val resIden = _uiState.value.deliveryStatus) {
            is DeliveryIdentifyStatus.Success -> {
                val newList = resIden.listOfDeliveryPerson.filter { t ->
                    t.timeStamp.toDate().date == Date(
                        _uiState.value.datePickerState.selectedDateMillis ?: 10
                    ).date
                }
                Log.d(TAG, "${newList}")
                _uiState.update {
                    it.copy(
                        deliverPersonList = newList,
                    )
                }

            }

            DeliveryIdentifyStatus.Error -> TODO()
            DeliveryIdentifyStatus.Loading -> TODO()
        }
    }

}
