package com.example.gitsimpledemo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitsimpledemo.R

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description: show when error
 */

@Composable
fun ErrorPage(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.broken_link),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(128.dp)
                    .width(128.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                onClick = {
                    onRefresh()
                },
                content = {
                    // Specify the icon using the icon parameter
                    Row(
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.reload),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing
                    Text("Refresh", fontSize = 16.sp, color = Color.Black)
                }
            )

        }
    }
}


@Preview
@Composable
fun ErrorPagePreview() {
    ErrorPage(onRefresh = {
        println("let us onRefresh")
    })
}