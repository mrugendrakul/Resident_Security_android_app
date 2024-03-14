package com.mrugendra.notificationtest.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun StartHere(
    nofUiState: uiState,
    navigateToIdentified:()->Unit,
    navigateToUnidentified:()->Unit,
    navigateToResidents:()->Unit,
    navigateToToken:()->Unit,
    permissionState:PermissionState?){


    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Click here to navigate to other location") }
//            )
//        }
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
//                .padding(top = 100.dp)
                .verticalScroll(rememberScrollState())
//                .weight(1f)

                .background(color = MaterialTheme.colorScheme.background)
//            .height(345.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier
////                .fillMaxHeight(0.3f)
//                .padding(top = 100.dp)
//            )
            Text(
                text = stringResource(R.string.welcome),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
            Spacer(Modifier.width(35.dp))
            ElevatedCard(
                modifier = Modifier
                    .padding(25.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.project_title),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            LaunchedEffect(key1 = Unit) {
                permissionState?.launchPermissionRequest()
            }
            if(permissionState?.status?.isGranted == true){
//                Text(text = "Notification permission granted")
            }
            else{
                Card(
                    modifier = Modifier
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        modifier  = Modifier
                            .padding(10.dp)
                        ,
                        text = "Notification permission missing, grant them from setting",
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }


            Image(
                modifier = Modifier
                    .size(300.dp),

                painter = painterResource(R.drawable.main_page),
                contentDescription ="Project Intro" )

//            Row {
//                SpcButton(
//                    text = "Identified",
//                    icon = R.drawable.ic_connection_error,
//                    navigateToIdentified
//                )
//                Spacer(Modifier.width(15.dp))
//                SpcButton(
//                    text = "Unidentified",
//                    icon = R.drawable.ic_connection_error,
//                    navigateToUnidentified
//                )
//            }
//            Spacer(Modifier.height(15.dp))
//            Row {
//                SpcButton(
//                    text = "Residents",
//                    icon = R.drawable.ic_connection_error,
//                    navigateToResidents
//                )
//                Spacer(Modifier.width(15.dp))
////                SpcButton(
////                    text = "Token Registration",
////                    icon = R.drawable.ic_connection_error,
////                    navigateToToken
////                )
//            }
        }
    }
//        Spacer(modifier = Modifier.weight(1f))
}


@Composable
fun SpcButton(
    text:String,
    icon:Int,
    onPress:()->Unit
){
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary),
        modifier = Modifier.size(150.dp),
        onClick = onPress
    ) {
        Column(

        ){
            Image(painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
//            Spacer(modifier = Modifier.height(10.dp))
            Text(text = text,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
                )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun PreviewStartHere(){

    MyApplicationTheme(
        dynamicColor = false
    )
    { StartHere(nofUiState = uiState(),{},{},{},{},
         permissionState = null
    )
    }
}

@Preview
@Composable
fun PreviewSpcButton(){
    MyApplicationTheme(
        dynamicColor = false
    )
    { SpcButton("Sample Text",
        R.drawable.ic_connection_error,
        onPress = {})
         }
}