package com.example.gitsimpledemo.ui.userdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gitsimpledemo.Application
import com.example.gitsimpledemo.R
import com.example.gitsimpledemo.ui.userdetail.components.TimeLineView
import com.example.gitsimpledemo.util.CommonUtils
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: user detail screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavHostController,
    username: String
) {
    val viewModel: UserDetailViewModel = viewModel(
        factory = UserDetailViewModelFactory(
            username = username,
            languageColorDao = Application.instance.database.languageColorDao()
        )
    )
    // initState
    val state = viewModel.uiState
    //  init widget param
    //  screen size
    val configuration = LocalConfiguration.current
    val showModal = remember { mutableStateOf(false) }
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    // init view
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    // webview
    if (showModal.value) {
        ModalBottomWebView(
            onDismissRequest = { showModal.value = false }, url = state.currentUrl
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomAppBar(scrollBehavior, state)
        }
    ) { paddingValues ->
        print(paddingValues)
        Box(modifier = Modifier.padding(paddingValues)) {
            TimeLineView(
                repoList = state.listRepositories,
                width = screenWidth * 4 / 5
            ) {
                showModal.value = true
                state.currentUrl = it
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(scrollBehavior: TopAppBarScrollBehavior, state: UserDetailState) {

    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        title = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.avatarUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.header_placeholder),
                        contentDescription = "description",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(42.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (state.name.isNotBlank()) {
                            Text(
                                state.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            state.userName,
                            maxLines = 1,
                            style = if (state.name.isBlank()) {
                                MaterialTheme.typography.titleMedium
                            } else MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(16.dp)

                    )
                    Text(
                        state.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_supervisor_account_black_24),
                        contentDescription = null,
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp)
                            .padding(end = 4.dp),
                    )
                    Text("followers", style = MaterialTheme.typography.bodySmall)
                    Text(
                        CommonUtils.formatNumber(state.followers),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 2.dp, end = 4.dp)
                    )
                    Text("following", style = MaterialTheme.typography.bodySmall)
                    Text(
                        CommonUtils.formatNumber(state.following),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 2.dp, end = 4.dp)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ModalBottomWebView(onDismissRequest: () -> Unit, url: String) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismissRequest()
        },
        content = {
            CustomWebView(
                url
            )
        }
    )
}

@Composable
fun CustomWebView(url: String) {
    val state = rememberWebViewState(url)
    val loadingState = state.loadingState
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = loadingState.progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
        WebView(
            state,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

            )

    }
}
