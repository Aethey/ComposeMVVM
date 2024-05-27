package com.example.gitsimpledemo.ui.userlist

import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gitsimpledemo.Application
import com.example.gitsimpledemo.model.entity.SearchType
import com.example.gitsimpledemo.model.entity.SearchViewType
import com.example.gitsimpledemo.route.Screens
import com.example.gitsimpledemo.ui.components.CustomLazyColumn
import com.example.gitsimpledemo.ui.components.ErrorPage
import com.example.gitsimpledemo.ui.userlist.components.AlertDialogExit
import com.example.gitsimpledemo.ui.userlist.components.SearchView
import com.example.gitsimpledemo.ui.userlist.components.UserListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:first page,git user list
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = viewModel(
        factory = UserListViewModelFactory(Application.instance.database.searchHistoryDao())
    ),
    navController: NavHostController
) {
    //  initState
    //  view all state
    val state = viewModel.uiState
    //  user list state
    val listState = rememberLazyListState()
    //  control pull refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.onRefreshData() }
    )
    //  control exit dialog
    val openAlertDialog = remember { mutableStateOf(false) }
    //  control app finish
    val shouldExit = remember { mutableStateOf(false) }
    //  init widget param
    //  screen size
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    //  control listview scroll to top
    val coroutineScope = rememberCoroutineScope()
    //  control FAV click event
    val interactionSource = remember { MutableInteractionSource() }
    //  control input text view width
    val inputTextWidth = remember { mutableIntStateOf(screenWidth) }

    //  init function
    fun onShowSearchWidget() {
        if (!state.isSearching) {
            inputTextWidth.intValue = screenWidth - 90
        }
        viewModel.onUpdateSearchViewState(SearchViewType.OPEN)
    }

    fun onCloseSearchWidget() {
        viewModel.onUpdateSearchViewState(SearchViewType.COMMON_CLOSE)
        inputTextWidth.intValue = screenWidth - 24
    }

    fun onSearch(searchQuery: String?) {
        when (searchQuery) {
            null -> viewModel.onKeyboardSearch(SearchType.USERNAME)
            else -> viewModel.onClickSearch(SearchType.USERNAME, searchQuery)
        }
        onCloseSearchWidget()
    }

    fun onClearHistory() {
        viewModel.onClearSearchHistory()
    }

    fun onSearchQueryChangeAction(searchQueryText: String) {
        viewModel.onRealtimeUpdateSearchQuery(searchQueryText.trim())
    }

    //  custom back action when isSearching
    //  when isSearching back action -> searchingWidget close & clear searchQuery
    BackHandler(true) {
        if (state.isSearching) {
            viewModel.onUpdateSearchViewState(SearchViewType.COMMON_CLOSE)
        } else if (state.searchQuery.isNotBlank()) {
            viewModel.onRealtimeUpdateSearchQuery("")
            viewModel.onRefreshData()
        } else {
            openAlertDialog.value = true
        }
    }
    if (shouldExit.value) {
        val context = LocalContext.current
        (context as ComponentActivity).finishAffinity()
    }

    //  init after view build
    LaunchedEffect(interactionSource) {
        var isLongClick = false
        //  build trie after view build
        viewModel.onBuildTrie()
        //  custom FAB long click event
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    if (listState.firstVisibleItemIndex != 0) {
                        isLongClick = false
                        delay(500)
                        isLongClick = true
                        //not top && Long click -> open search
                        onShowSearchWidget()
                    }
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        if (listState.firstVisibleItemIndex == 0) {
                            //  on the top && press -> open search
                            onShowSearchWidget()
                        } else {
                            //  not top && press -> to top
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
    //  get userList scroll state
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                run {
                    viewModel.onUpdateUserListScrollState(scrolling)
                    viewModel.onCheckListIsShowTop(listState.firstVisibleItemIndex == 0)
                }
            }
    }
    //  init view
    when {
        // ...
        openAlertDialog.value -> {
            AlertDialogExit(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    shouldExit.value = true
                },
                dialogTitle = "Exit this App",
                dialogText = "Are you sure you want to exit?",
                icon = Icons.Default.Info
            )
        }
    }

    //  main view
    AnimatedVisibility(
        visible = !state.isError,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            topBar = {
                CustomAppBar(
                    onShowSearchWidget = { onShowSearchWidget() },
                    onCloseSearchWidget = { onCloseSearchWidget() },
                    searchQuery = state.searchQuery,
                    inputTextWidth = inputTextWidth,
                    state = state,
                    onSearch = { onSearch(null) },
                    onSearchQueryChange = { onSearchQueryChangeAction(it) }
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
                    searchHistory = state.searchHistory,
                    onSearch = { onSearch(it) },
                    height = screenHeight,
                    onClearHistory = { onClearHistory() }
                )
                AnimatedVisibility(
                    visible = !state.isSearching,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                ) {

                    CustomLazyColumn(
                        listState = listState,
                        pullRefreshState = pullRefreshState,
                        dataList = state.userList,
                        listItemContent = @Composable { index: Int ->
                            UserListItem(state.userList[index])
                        },
                        modifier = Modifier.padding(
                            top = paddingValues.calculateTopPadding(),
                            start = 4.dp,
                            end = 4.dp,
                            bottom = 4.dp,
                        ),
                        hasMoreAction = {
                            LaunchedEffect(Unit) {
                                viewModel.onLoadMoreData()
                            }
                        },
                        itemClickEvent = {
                            navController.navigate(Screens.Detail.createRoute(it.login))
                        },
                        isRefreshing = state.isRefreshing,
                        hasMore = state.hasMore,
                        isShowCustomToast = (!state.hasMore && state.isLoadingMore),
                        itemClickable = true
                    )
                }
            }
        }
    }
    AnimatedVisibility(
        visible = state.isError,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
    ) {
        ErrorPage(onRefresh = { viewModel.onRefreshData() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    onShowSearchWidget: () -> Unit,
    onCloseSearchWidget: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    searchQuery: String,
    inputTextWidth: MutableIntState,
    state: UserListState
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onShowSearchWidget()
                }, modifier = Modifier.size(40.dp)) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { onSearchQueryChange(it) },
                    inputTextWidth = inputTextWidth.intValue - 40,
                    onOpenKeyboard = {
                        onShowSearchWidget()
                    },
                    keyboardEnable = state.isSearching,
                    onSearch = {
                        onSearch()
                    }
                )
                if (state.isSearching) {
                    IconButton(onClick = {
                        onCloseSearchWidget()
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
fun CustomFloatingActionButton(state: UserListState, interactionSource: MutableInteractionSource) {
    AnimatedVisibility(
        visible = !state.isScrolling && !state.isSearching,
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
    keyboardEnable: Boolean,
    onSearch: () -> Unit,
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
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        decorationBox = { innerTextField ->
            if (searchQuery.isEmpty()) {
                Text(
                    text = "Search in Github",
                    style = TextStyle(color = Color.Gray, fontSize = 12.sp)
                )
            }
            innerTextField()
        }
    )
}

