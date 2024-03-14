package com.mrugendra.notificationtest.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun LoadingScreen(
    nofUiState:uiState,
    retry:()->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .size(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                    ,
            painter = painterResource(id = R.drawable.img),
            contentDescription = ""
        )
        LoadingIndicator(
            isLoading = nofUiState.isLoading)
        if(nofUiState.startingError){
            Text(
//                modifier = Modifier,
                text = "Error to load the data please check the internet",
                fontSize = 20.sp,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center
            )
            Button (onClick = retry){
                Text(text = "Retry",
                    color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoadingScreen(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        LoadingScreen( nofUiState = uiState(
            isLoading = false,
            startingError = true
        ),
            retry = {}
        )
    }
}