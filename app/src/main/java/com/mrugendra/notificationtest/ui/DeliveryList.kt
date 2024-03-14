package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.deliveryPerson
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeliveryListSuccess(
    nofUiState:uiState,
    refreshState: PullRefreshState,
    openCalender: () -> Unit,
    search: () -> Unit,

    ){
    Scaffold(
        topBar ={
            DeliverySearchBar(
                nofUiState = nofUiState,
                openCalender = openCalender,
                search = search
            )
        }
    ){
        Box (
            modifier = Modifier
                .padding(it)

        ){
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .pullRefresh(refreshState)
            ) {
                items(nofUiState.deliverPersonList) { deliveryPerson ->
                    DeliveryPerson(deliveryPerson)
                }
            }
            if(nofUiState.openCalender)(
                    DelDatePickup(
                        nofUiState,
                        openCalender,
                        search
                    )
                    )
            PullRefreshIndicator(
                refreshing = nofUiState.deliveryRefresh,
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
fun DelDatePickup(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliverySearchBar(
    nofUiState: uiState,
    openCalender:()->Unit,
    search:()->Unit,
){

    TopAppBar (
        modifier = Modifier
            .height(80.dp),
        title = {
            Row(modifier = Modifier
                .fillMaxWidth(),

                horizontalArrangement = Arrangement.Center
            ){
//                OutlinedTextField(
//                    value = nofUiState.searchText,
//                    onValueChange = onSearchText,
//                    singleLine = true,
//
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .padding(8.dp)
////                        .weight(5f)
//                        .fillMaxWidth(0.8f)
//                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                        .fillMaxWidth()
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeliveryList(
    nofUiState:uiState,
    openCalender: () -> Unit,
    search:()->Unit,
    refreshState: PullRefreshState,
    errorRefresh:(Boolean)->Unit
){
    val deliveryStatus = nofUiState.deliveryStatus
    when(deliveryStatus){
        is DeliveryIdentifyStatus.Success->{
            DeliveryListSuccess(
                nofUiState = nofUiState,
                refreshState = refreshState,
                search = search,
                openCalender =  openCalender)
        }

        DeliveryIdentifyStatus.Loading -> {
            DeliveryListLoading(
                nofUiState = nofUiState,
                refreshState = refreshState
            )
        }
        DeliveryIdentifyStatus.Error -> {
            DeliveryListError(
                modifier = Modifier
                    .fillMaxSize(),
                getTheList = { errorRefresh(false) }
            )
        }
    }
}

@Composable
fun DeliveryListError(
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
fun DeliveryListLoading(
    nofUiState: uiState,
    refreshState: PullRefreshState
){
    Box(modifier = Modifier
        .fillMaxSize()){
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            items(6) {
                UnknownLoading(

                )
            }
        }
        PullRefreshIndicator(
            refreshing = nofUiState.deliveryRefresh,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeliveryLoading(

){
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .height(120.dp)
            .padding(5.dp)

//        border = BorderStroke(5.dp,MaterialTheme.colorScheme.primary)

    ){
        Column(
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
        ){
            Card(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ){}
            Spacer(Modifier.height(15.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(5.dp)
                    .padding(top = 0.dp)
                ,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {

            }
        }
    }
}

@Composable
fun DeliveryPerson(
    delivery:deliveryPerson,
){
    val sdf = SimpleDateFormat("hh:mm a YYYY/MM/dd")
    val fdate = sdf.format(delivery.timeStamp.toDate())
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .height(120.dp)
            .padding(5.dp)

//        border = BorderStroke(5.dp,MaterialTheme.colorScheme.primary)

    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(context= LocalContext.current)
                    .data(delivery.image)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .build(),
                contentDescription = "Unkown person",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop)
            Spacer(Modifier.height(15.dp))
            Text(
                text = "Time : ${fdate}",
                fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun PreviewDelivery(){
    DeliveryPerson(deliveryPerson(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",timeStamp= Timestamp.now()),
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true)
@Composable
fun PreviewDeliveryListSuccess(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        DeliveryListSuccess(nofUiState = uiState(
            deliverPersonList = listOf(
                deliveryPerson(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",timeStamp=Timestamp.now()),
                deliveryPerson(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",timeStamp=Timestamp.now()),
                deliveryPerson(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",timeStamp=Timestamp.now()),
                deliveryPerson(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",timeStamp=Timestamp.now())
            ),
            openCalender = false
        ),
            refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            search = {
            },
            openCalender = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    showSystemUi = true
)
@Composable
fun PreviewDeliveryLoading(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        DeliveryListLoading(
            nofUiState = uiState(),
            refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
        )
    }
}