package com.example.gitsimpledemo.ui.userlist

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gitsimpledemo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

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
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isAtTop = remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    val isScrolling = remember { mutableStateOf(false) }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        isAtTop.value = listState.firstVisibleItemIndex == 0
    }


    LaunchedEffect(interactionSource) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(500)
                    isLongClick = true
                    Toast.makeText(context, "Long click", Toast.LENGTH_SHORT).show()
                }
                is PressInteraction.Release -> {
                    if (isLongClick.not()){
                        if(listState.firstVisibleItemIndex == 0){
                            Toast.makeText(context, "Fake Long click", Toast.LENGTH_SHORT).show()
                        }else{
                            coroutineScope.launch { listState.animateScrollToItem(0) }
                        }

                    }

                }
                is PressInteraction.Cancel -> {
                    isLongClick = false
                }
            }
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                run {
                    println("scrolling $scrolling")
                    isScrolling.value = scrolling
                    println("scrolling ${isScrolling.value}")
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paginated List") }
            )
        },
                floatingActionButton = {

                    AnimatedVisibility(
                        visible = !isScrolling.value,
                        enter = scaleIn(),
                        exit = scaleOut(),
                    ) {
            if (isAtTop.value) {
                FloatingActionButton(
                    interactionSource = interactionSource,
                    onClick = {},
                    modifier = Modifier
                        .padding(10.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp),

                        )
                }
            } else {
                FloatingActionButton(
                    interactionSource = interactionSource,
                    onClick = {},
                    modifier = Modifier
                        .padding(10.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp),

                        )
                }
            }
        }}
    ){paddingValues ->
        Log.d("TAG", "UserListScreen: $paddingValues")
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { viewModel.refreshData() }
        )
        Box(modifier = Modifier
            .fillMaxSize().padding(top = paddingValues.calculateTopPadding())
            .pullRefresh(pullRefreshState)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(4.dp)
                ,
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
        .background(Color.White, RoundedCornerShape(12.dp))
//        .clip(RoundedCornerShape(16.dp))
        .shadow(2.dp, RoundedCornerShape(12.dp),ambientColor= Color.Blue, spotColor = Color.White)
        ){
        Row(
            modifier = Modifier
                .fillMaxSize().padding(horizontal = 24.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://images.pexels.com/photos/23887232/pexels-photo-23887232.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
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