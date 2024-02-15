package com.mrugendra.notificationtest.ui

import com.mrugendra.notificationtest.data.residents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
fun ResidentList(
    residents:List<residents>
){
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)){
        items(residents){resident->
            Resident(resident)
        }
    }
}

@Composable
fun Resident(
    resident:residents
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp),
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
    Resident(residents("Mrugendra","123uuidxzyabc","Will get there"))
}

@Preview
@Composable
fun PreviewResidentList(){
    MyApplicationTheme {
        ResidentList(residents = listOf(
            residents("Mrugendra","123uuidxzyabc","Will get there"),
            residents("Mrugendra","123uuidxzyabc","Will get there"),
            residents("Mrugendra","123uuidxzyabc","Will get there")
        ))
    }
}