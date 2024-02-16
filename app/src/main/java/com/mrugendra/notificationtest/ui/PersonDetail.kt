package com.mrugendra.notificationtest.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun PersonDetail(){
    Text(text = "Here the info will be displayed with all the information about the person with every thing extra")
}

@Preview
@Composable
fun PreviewPersonDetail(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
            PersonDetail()
        }
}
