package com.mrugendra.notificationtest.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.mrugendra.notificationtest.data.residents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
val TAG = "MyFirebaseMessagingService"
@Composable
fun ResidentList(
    residents:ResidentStatus,
    pressed: () -> Unit,
    modifier: Modifier = Modifier,
    getTheList:()->Unit
){
    when(residents){
        is ResidentStatus.Success -> ResidentListSuccess(residents = residents.residents,pressed = pressed)
        is ResidentStatus.Loading -> ResidentListLoading(modifier.fillMaxSize())
        is ResidentStatus.Error -> ResidentListError(modifier.fillMaxSize(), getTheList = getTheList)
    }
}

@Composable
fun ResidentListLoading(
    modifier: Modifier = Modifier
){
//    Image(
//        modifier = modifier.size(250.dp),
//        painter = painterResource(id = R.drawable.loading_img),
//        contentDescription = stringResource(R.string.loading)
//    )
    Column(modifier) {
        ResidentLoading()
        ResidentLoading()
        ResidentLoading()
        ResidentLoading()
    }
}

@Composable
fun ResidentListError(
    modifier: Modifier = Modifier,
    getTheList:()->Unit
){
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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

@Composable
fun ResidentListSuccess(
    residents:List<residents>,
    pressed: () -> Unit
){
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)){
        items(residents){resident->
            Resident(resident,pressed)
        }
    }
}


@Composable
fun ResidentLoading(

){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp),
        colors = CardDefaults.cardColors(
        )
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
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

@Composable
fun Resident(
    resident:residents,
    pressed:()->Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp)
            .clickable { pressed },
//        border = BorderStroke(5.dp,MaterialTheme.colorScheme.primary)

    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = resident.name,
                fontSize = 25.sp
            )
            Spacer(Modifier.height(15.dp))
            Text(
                text = "Unique id: ${resident.id}",
                fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun PreviewResident(){
    Resident(residents("Mrugendra","123uuidxzyabc","Will get there","https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs="),{})
}

@Preview
@Composable
fun PreviewResidentList(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        ResidentListSuccess(residents = listOf(
            residents("Mrugendra","123uuidxzyabc","Will get there","https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs="),
            residents("Mrugendra","123uuidxzyabc","Will get there","https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs="),
            residents("Mrugendra","123uuidxzyabc","Will get there","https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=")
        ),{})
    }
}

@Preview
@Composable
fun PreviesResidentListLoading(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        ResidentListLoading()
    }
}

@Preview
@Composable
fun PreviewResidentListError(){
    MyApplicationTheme(
        dynamicColor = false
    )
    { ResidentListError(getTheList = {}) }
}