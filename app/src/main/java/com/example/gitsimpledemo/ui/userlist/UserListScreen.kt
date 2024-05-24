package com.example.gitsimpledemo.ui.userlist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = viewModel(factory = UserListViewModelFactory()),
) {
//  initState
    val state = viewModel.uiState
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.refreshData() }
    )
//  init widget param
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
//  init data
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val inputTextWidth = remember { mutableIntStateOf(screenWidth) }
//  mock date
    val searchHistory = listOf("History 1", "History 2", "History 3")
//  init function
    fun onShowSearchWidget(){
        if (!state.isSearching) {
            inputTextWidth.intValue = screenWidth- 90
        }
        viewModel.onUpdateSearchState(true)
    }

    fun onCloseSearchWidget(){
        viewModel.onUpdateSearchState(false)
        inputTextWidth.intValue = screenWidth- 24
    }


//    custom back action when isSearching
//    when isSearching back action -> searchingWidget close & clear searchQuery
    BackHandler(enabled = state.isSearching) {
        viewModel.onUpdateSearchState(false)
        searchQuery.value = ""
    }

//  custom FAB long click event
    LaunchedEffect(interactionSource) {
        var isLongClick = false
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    if(listState.firstVisibleItemIndex != 0){
                        isLongClick = false
                        delay(500)
                        isLongClick = true
//                       not top && Long click -> open search
                        onShowSearchWidget()
                    }
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        if (listState.firstVisibleItemIndex == 0) {
//                            on the top && press -> open search
                            onShowSearchWidget()
                        } else {
//                            not top && press -> to top
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
//    get userList scroll state
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                run {
                    viewModel.updateUserListScrollState(scrolling)
                    viewModel.onJudgmentShowTop(listState.firstVisibleItemIndex == 0)
                }
            }
    }
//    init view
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomAppBar(
                onShowSearchWidget = { onShowSearchWidget() },
                onCloseSearchWidget = { onCloseSearchWidget() },
                searchQuery = searchQuery,
                inputTextWidth = inputTextWidth,
                state = state
            )
        },
        floatingActionButton = {
            CustomFloatingActionButton(state, interactionSource)
        }
    ) { paddingValues ->
        Column {
            SearchView(
                paddingValues = paddingValues,
                state = state,
                searchHistory=searchHistory,
                searchQuery=searchQuery,
                viewModel=viewModel)
            AnimatedVisibility(
                visible = !state.isSearching,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .pullRefresh(pullRefreshState)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    onShowSearchWidget: () -> Unit,
    onCloseSearchWidget: () -> Unit,
    searchQuery: MutableState<String>,
    inputTextWidth: MutableIntState,
    state:UserListState
    ){
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .padding(end = 8.dp).fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onShowSearchWidget()
                }, modifier = Modifier.size(40.dp)) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                SearchBar(
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = { searchQuery.value = it },
                    inputTextWidth = inputTextWidth.intValue - 40,
                    onOpenKeyboard = {
                        onShowSearchWidget()
                    },
                    keyboardEnable = state.isSearching
                )
                if (state.isSearching) {
                    IconButton(onClick = {
                        onCloseSearchWidget()
//                                need clear searchQuery
                        searchQuery.value = ""
                    }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel"
                        )
                    }
                }
            }
        },
    )
}


@Composable
fun UserListItemWidget(content: String) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
//        .clip(RoundedCornerShape(16.dp))
            .shadow(
                2.dp,
                RoundedCornerShape(12.dp),
                ambientColor = Color.Blue,
                spotColor = Color.White
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
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
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )

            Text(content)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchView(
    state: UserListState,
    searchHistory: List<String>,
    searchQuery: MutableState<String>,
    viewModel: UserListViewModel,
    paddingValues: PaddingValues){
    AnimatedVisibility(
        visible = state.isSearching,
        enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
    ) {
        Column(
//                    modifier = Modifier.background(Color.Black)
        ) {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(searchHistory.size) { i ->
                    ListItem {
                        Text(
                            text = searchHistory[i],
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    searchQuery.value = searchHistory[i]
                                    viewModel.onUpdateSearchState(false)
                                }
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun CustomFloatingActionButton(state: UserListState, interactionSource: MutableInteractionSource){
    AnimatedVisibility(
        visible =!state.isScrolling,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        if (state.isShowTopItem) {
            FloatingActionButton(
                interactionSource = interactionSource,
                onClick = {},
                modifier = Modifier
                    .padding(10.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
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
    }
}


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    inputTextWidth: Int,
    onOpenKeyboard: () -> Unit,
    keyboardEnable: Boolean
) {

    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(keyboardEnable) {
        if (keyboardEnable) {
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        maxLines = 1,
        enabled = keyboardEnable,
        modifier = Modifier
            .width(inputTextWidth.dp)
            .focusRequester(focusRequester)
            .clickable(interactionSource = interactionSource, indication = null) {
                onOpenKeyboard()
            },
        decorationBox = { innerTextField ->
            if (searchQuery.isEmpty()) {
                Text(
                    text = "Search in Github",
                    style = TextStyle(color = Color.Gray, fontSize = 12.sp)
                )
            }
            innerTextField()
        })
}