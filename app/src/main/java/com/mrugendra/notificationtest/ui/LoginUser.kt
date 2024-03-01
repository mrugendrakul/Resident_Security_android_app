package com.mrugendra.notificationtest.ui

import android.graphics.Outline
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrugendra.notificationtest.R
import com.mrugendra.notificationtest.data.uiState
import com.mrugendra.notificationtest.ui.theme.MyApplicationTheme

@Composable
fun LoginUser(
    nofUiState:uiState,
    updateUsername:(String)->Unit,
    updatePassword:(String)->Unit,
    loginUser:()->Unit
){
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(text = stringResource(R.string.username))},
                value = nofUiState.username ,
                onValueChange ={updateUsername(it)},
                isError = nofUiState.nullName,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "")
                }
            )

            OutlinedTextField(
                label = { Text(text = stringResource(R.string.password))},
                value = nofUiState.password ,
                onValueChange ={updatePassword(it)},
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if(!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier
                .height(10.dp))
            Button(
                onClick =loginUser,
                enabled = !nofUiState.successSend
            ) {
                Text(text = "Login now!")
            }

            if(!nofUiState.exist){
                Text(
                    text = "Wrong username or password",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginUser(){
    MyApplicationTheme(
        dynamicColor = false
    ) {
        LoginUser(
            nofUiState = uiState(),
            updateUsername = {},
            updatePassword = {},
            loginUser = {}
        )
    }
}