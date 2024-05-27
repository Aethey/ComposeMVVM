package com.example.gitsimpledemo.ui.userlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gitsimpledemo.R
import com.example.gitsimpledemo.model.entity.UserEntity

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description: UserListItem
 */

@Composable
fun UserListItem (content: UserEntity){
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(content.avatarUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.header_placeholder),
                contentDescription = "description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(content.login,style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    UserListItem(UserEntity(
        id = 1,
        login = "Ryu",
        avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        type = "User"
    ))
}
