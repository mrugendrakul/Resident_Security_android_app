package com.mrugendra.notificationtest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrugendra.notificationtest.data.Identified
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

@Composable
fun IdentifiedList(
    identified:List<Identified>,
    button:()->Unit,
    buttonEnabled:Boolean = false
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ){
        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            items(identified) { identify ->
                Person(identify, button,buttonEnabled)
            }
        }
    }
}

@Composable
fun Person(
    identifiy:Identified,
    button:()->Unit,
    buttonEnabled: Boolean = false
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp)
            .clickable(onClick = button, enabled = buttonEnabled)  ,
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
                text = "Time : ${identifiy.time.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))}",
                fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun PreviewPerson(){
    Person(Identified(id="xyz",name="Mrugendra", time=LocalDateTime.of(2024, Month.JANUARY,30,22,12))
        ,{})
}

@Preview
@Composable
fun PreviewPeopleList(){
    MyApplicationTheme {
        IdentifiedList(identified = listOf(
            Identified("xyz","Mrugendra",LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Identified("xyz","Mrugendra",LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Identified("xyz","Mrugendra",LocalDateTime.of(2024, Month.JANUARY,30,22,12)),
            Identified("xyz","Mrugendra",LocalDateTime.of(2024, Month.JANUARY,30,22,12))
            ),{}
        )
    }
}