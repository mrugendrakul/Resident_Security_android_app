package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdentifiedList(
    nofUiState:uiState,
    identified:IdentifiedStatus,
    button:(String)->Unit,
    buttonEnabled:Boolean = true,
    openCalender: () -> Unit,
    search:()->Unit,
    onSearchText: (String) -> Unit,
    refreshState: PullRefreshState,
    errorRefresh:(Boolean)->Unit
){
    when (identified){
        is IdentifiedStatus.Success->{
            IdentifiedListSuccess(
                nofUiState = nofUiState,
                identified = nofUiState.identifiedList,
                button =button,
                openCalender = openCalender,
                search = search,
                onSearchText = onSearchText,
                refreshState = refreshState)
        }

        IdentifiedStatus.Error -> {
            IdentifideError(modifier = Modifier.fillMaxSize(),
                getTheList = {errorRefresh(false)}
            )
        }
        IdentifiedStatus.Loading -> {
            IdentifiedListLoading(
                refreshState = refreshState,
                nofUiState = nofUiState)
        }
    }
}

@Composable
fun IdentifideError(
    modifier: Modifier = Modifier,
    getTheList: () -> Unit
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(250.dp),
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = stringResource(R.string.error)
        )
        Button(
            onClick = getTheList,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = "Retry",
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdentifiedListLoading(
    refreshState:PullRefreshState,
    modifier: Modifier= Modifier,
    nofUiState: uiState
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ){
        LazyColumn(modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(),) {
            items(6){
                IdentifiedLoading()
            }
        }
        PullRefreshIndicator(
            refreshing = nofUiState.identifiedRefresh,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun IdentifiedLoading(

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp),
        colors = CardDefaults.cardColors(
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(5.dp)
            ) {}
            Spacer(Modifier.height(15.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(5.dp)
            ) {}
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdentifiedListSuccess(
    nofUiState:uiState,
    identified:List<Identified>,
    button:(String)->Unit,
    buttonEnabled:Boolean = false,
    openCalender: () -> Unit,
    search:()->Unit,
    onSearchText: (String) -> Unit,
    refreshState: PullRefreshState
){
    Scaffold(
        topBar = { IdentifiedSearchBar(
            nofUiState = nofUiState,
            openCalender,
            search = search,
            onSearchText = onSearchText
        ) }
    ){
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .pullRefresh(refreshState)
            ) {
                items(identified) { identify ->
                    Person(identify, button, buttonEnabled)
                }
            }

            if(nofUiState.openCalender)(
                    DatePickup(
                        nofUiState,
                        openCalender,
                        search
                    )
            )

            PullRefreshIndicator(
                refreshing = nofUiState.identifiedRefresh,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifiedSearchBar(
    nofUiState: uiState,
    openCalender:()->Unit,
    search:()->Unit,
    onSearchText: (String) -> Unit
){

    TopAppBar (
        modifier = Modifier
            .height(80.dp),
        title = {
            Row{
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp)
//                        .weight(5f)
                        .fillMaxWidth(0.8f),
                    value = nofUiState.searchText,
                    onValueChange = onSearchText,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { search() }
                    )

                )
//                OutlinedButton(
//                    modifier = Modifier
//                        .fillMaxHeight()
//
//                        .padding(10.dp)
//                    ,
//                    onClick = { openCalender()},
//                    shape = MaterialTheme.shapes.small
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CalendarMonth,
//                        contentDescription = "Select Date",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .size(40.dp)
////                            .border(
////                                width = 2.dp,
////                                shape = MaterialTheme.shapes.small,
////                                color = MaterialTheme.colorScheme.secondary
////                            )
//                        )
//                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .border(
                            width = 2.dp,
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    ,
                    onClick = { openCalender()}
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Select Date",
                        modifier = Modifier
                            .fillMaxSize()
                            .size(40.dp)
//                            .border(
//                                width = 2.dp,
//                                shape = MaterialTheme.shapes.small,
//                                color = MaterialTheme.colorScheme.secondary
//                            )
                        )
                }

            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .border(
                        width = 2.dp,
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    ,
                onClick = { search()}
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Search the residents",
                    modifier = Modifier
                        .size(40.dp))
            }
        }

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickup(
    nofUiState: uiState,
    openCalender: () -> Unit,
    search: () -> Unit,
){
    val snackScope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    DatePickerDialog(
        onDismissRequest = {
            openCalender()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    Log.d(TAG,"date milis select: ${Date(nofUiState.datePickerState.selectedDateMillis?:255103362)}")
                    openCalender()
                    search()
                },
                enabled = nofUiState.datePickerState.selectedDateMillis != null
            ) {
                Text(text = "Ok")
            }
        }) {
        DatePicker(state = nofUiState.datePickerState)
    }
}

@Composable
fun Person(
    identifiy:Identified,
    button:(String)->Unit,
    buttonEnabled: Boolean = false
){
    val sdf = SimpleDateFormat("YYYY/MM/dd hh:mm a")
    val fdate = sdf.format(identifiy.time.toDate())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp)
            .clickable(onClick = { button(identifiy.id) })  ,
//        border = BorderStroke(5.dp,MaterialTheme.colorScheme.primary)

    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = identifiy.name,
                fontSize = 25.sp
            )
            Spacer(Modifier.height(15.dp))
            Text(
                text = "Time : ${fdate}",
                fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun PreviewPerson(){
    Person(Identified(id="xyz",name="Mrugendra",  time = Timestamp.now())
        ,{})
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewPeopleList() {
    MyApplicationTheme {

        IdentifiedListSuccess(
            nofUiState = uiState(
                openCalender =  false,
                identifiedRefresh = true
            ),
            identified = listOf(
            Identified("xyz", "Mrugendra", time = Timestamp.now()),
            Identified("xyz", "Mrugendra",  time = Timestamp.now()),
            Identified("xyz", "Mrugendra",  time = Timestamp.now()),
            Identified("xyz", "Mrugendra", time = Timestamp.now() )
        ), {},
            openCalender = {},
            search = {},
            onSearchText = {},
            refreshState =  rememberPullRefreshState(refreshing = true, onRefresh = { /*TODO*/ })
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewIdentifiedLoading(){
    MyApplicationTheme(
        dynamicColor =false
    ) {
        IdentifiedListLoading(
            refreshState = rememberPullRefreshState(refreshing = true, onRefresh = { /*TODO*/ }),
            nofUiState = uiState()
        )
    }
}

@Preview
@Composable
fun PreviewIndetifiedError(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        IdentifideError(modifier = Modifier.fillMaxSize(),) {

        }
    }
}