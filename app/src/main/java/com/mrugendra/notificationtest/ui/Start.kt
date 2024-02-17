package com.mrugendra.notificationtest.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun StartHere(
    nofUiState: uiState,
    navigateToIdentified:()->Unit,
    navigateToUnidentified:()->Unit,
    navigateToResidents:()->Unit,
    navigateToToken:()->Unit){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(15.dp)
            .background(color = MaterialTheme.colorScheme.background)
//            .height(345.dp)
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row {
            SpcButton(
                text = "Identified",
                icon = R.drawable.ic_launcher_foreground,
                navigateToIdentified
            )
            Spacer(Modifier.width(15.dp))
            SpcButton(
                text = "Unidentified",
                icon = R.drawable.ic_launcher_foreground,
                navigateToUnidentified
            )
        }
        Spacer(Modifier.height(15.dp))
        Row {
            SpcButton(
                text = "Residents",
                icon = R.drawable.ic_launcher_foreground,
                navigateToResidents
            )
            Spacer(Modifier.width(15.dp))
            SpcButton(
                text = "Token Registration",
                icon = R.drawable.ic_launcher_foreground,
                navigateToToken
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

@Preview
@Composable
fun PreviewStartHere(){
    MyApplicationTheme()
    { StartHere(nofUiState = uiState(),{},{},{},{}) }
}

@Preview
@Composable
fun PreviewSpcButton(){
    MyApplicationTheme()
    { SpcButton("Sample Text",
        R.drawable.ic_launcher_foreground,
        onPress = {})
         }
}