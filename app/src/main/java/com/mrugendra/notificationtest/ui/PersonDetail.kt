package com.mrugendra.notificationtest.ui

import android.graphics.Paint
import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.rotationMatrix
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.residents
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun PersonDetail(
    residents: residents
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(residents.profilePhoto)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()
            ,
            modifier = Modifier
                .size(400.dp)
                .padding(50.dp)
                .clip(MaterialTheme.shapes.medium)
            ,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
            ,
            text = residents.name,
            fontSize = 35.sp,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
            ,
            text = residents.info,
            fontSize = 20.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun PreviewPersonDetail(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
            PersonDetail(
                residents("Mrugendra Kulkarni Computer","uudirando","Student in pccoe and created this application",
                    "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=740&t=st=1708193220~exp=1708193820~hmac=b17f7cebf9d68d37476c4db78b0631d9be333b664205ddaa5973dced193bf48e")
            )
        }
}
