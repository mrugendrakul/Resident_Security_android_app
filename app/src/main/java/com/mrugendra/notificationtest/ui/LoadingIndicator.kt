package com.mrugendra.notificationtest.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
    isLoading:Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center
){
    if (isLoading) {
        Box(
            modifier = Modifier
//                .fillMaxSize()
                .size(30.dp)
//                .background(brush = Brush.verticalGradient(),alpha  = 0f)
            ,
            contentAlignment = contentAlignment
        ) {
            CircularProgressIndicator(
//                color = Color.Black, // You can set your desired color here
                strokeWidth = 5.dp ,// You can adjust the stroke width as needed,
            )
        }
    }
}