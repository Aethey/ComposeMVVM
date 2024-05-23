package com.example.gitsimpledemo.ui.userlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gitsimpledemo.R

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UserListScreen (
    viewModel: UserListViewModel = viewModel(factory = UserListViewModelFactory()),
){
//    initData
    val state = viewModel.uiState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paginated List") }
            )
        }
    ){paddingValues ->
        Log.d("TAG", "UserListScreen: $paddingValues")

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { viewModel.refreshData() }
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
//                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(horizontal = 16. dp, vertical = 8. dp),
                verticalArrangement = Arrangement.spacedBy(8. dp),
            ) {
                items(state.items.size) { itemContent ->
                    ListItem {
                        UserListItemWidget(state.items[itemContent])
                    }
                }
                if (state.hasMore) {
                    println("has more a has more")
                    item {
                        LaunchedEffect(Unit) {
                            viewModel.loadMoreData()
                        }
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun UserListItemWidget(content:String){
    Box(modifier = Modifier
        .height(80.dp)
        .fillMaxWidth()
        .background(Color.White, RoundedCornerShape(24.dp))
        .border(width = 1.dp, color = Color.Blue, RoundedCornerShape(24.dp))
        ){
        Row(
            modifier = Modifier
                .fillMaxSize() .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQAXr0BV0IxgvbRbTKXKLilzw7WnZomMhKDqfTRuTk6A&s")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.header_placeholder),
                contentDescription = "description",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape).size(48.dp)
            )

            Text(content)
        }
    }

}