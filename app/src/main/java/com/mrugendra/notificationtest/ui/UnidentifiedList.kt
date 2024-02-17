package com.mrugendra.notificationtest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.Unidentified
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

@Composable
fun UnidentifiedListStart(
    unidentifes:List<Unidentified>,
    pressed: () -> Unit
){
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)){
        items(unidentifes){unknown->
            Unkown(unknown,pressed)
        }
    }
}

@Composable
fun UnidentifiedList(
    unidentifes: List<Unidentified>,
    pressed: () -> Unit,
){
    UnidentifiedListStart(unidentifes = unidentifes, pressed = pressed)


}

@Composable
fun Unkown(
    unknown:Unidentified,
    pressed:()->Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .height(120.dp)
            .padding(5.dp)
            .clickable { pressed },
//        border = BorderStroke(5.dp,MaterialTheme.colorScheme.primary)

    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(context= LocalContext.current)
                    .data(unknown.image)
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
                text = "Time : ${unknown.time.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))}",
                fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun PreviewUnkown(){
    Unkown(Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
        {})
}

@Preview(showSystemUi = true)
@Composable
fun PreviewUnidentifiedList(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        UnidentifiedList(unidentifes = listOf(
            Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Unidentified(image ="https://media.istockphoto.com/id/587805156/vector/profile-picture-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=N14PaYcMX9dfjIQx-gOrJcAUGyYRZ0Ohkbj5lH-GkQs=",time=LocalDateTime.of(2024, Month.JANUARY,30,22,12))
            ),{}
        )
    }
}