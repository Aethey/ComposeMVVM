package com.example.gitsimpledemo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Author: Ryu
 * Date: 2024/05/27
 * Description:
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> CustomLazyColumn(
    hasMoreAction: () -> Unit,
    dataList: List<T>,
    navController: NavController,
    paddingValues: PaddingValues,
    listState: LazyListState,
    pullRefreshState: PullRefreshState,
    listItemContent: @Composable (T) -> Unit,
    listItemClickEvent: (T) -> Unit,
    hasMore: Boolean,
    isShowCustomToast: Boolean,
    isRefreshing: Boolean,
    padding: PaddingValues,

    ) {

    Box {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    padding
                )
                .pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(dataList.size) { itemContent ->
                ListItem(
                    modifier = Modifier.clickable {
                        listItemClickEvent(dataList[itemContent])

                    }
                ) {
                    listItemContent(dataList[itemContent])
                }
            }
            if (hasMore) {
                item {
                    hasMoreAction()
                    //LaunchedEffect(Unit) {
                    //    viewModel.onLoadMoreData()
                    //}
                    Box(
                        modifier = Modifier.fillMaxSize(), // 使 Box 填充父容器的整个空间
                        contentAlignment = Alignment.Center // 使内容居中
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(6.dp)
                                .size(32.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
        ShowCustomToast(
            showToastState = (isShowCustomToast),
            message = "NO MORE DATA",
        )
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}