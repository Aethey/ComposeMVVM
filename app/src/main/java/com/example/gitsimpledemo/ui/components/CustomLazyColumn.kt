package com.example.gitsimpledemo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.gitsimpledemo.Constants

/**
 * Author: Ryu
 * Date: 2024/05/27
 * Description:
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> CustomLazyColumn(
    hasMoreAction: @Composable () -> Unit,
    dataList: List<T>,
    listState: LazyListState,
    pullRefreshState: PullRefreshState,
    listItemContent: @Composable (Int) -> Unit,
    itemClickEvent: (T) -> Unit,
    itemClickable: Boolean,
    hasMore: Boolean,
    isShowCustomToast: Boolean,
    isRefreshing: Boolean,
    modifier: Modifier,
) {

    Box {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(dataList.size) { index ->
                ListItem(
                    modifier = Modifier
                        .then(
                            if (itemClickable) {
                                Modifier.clickable { itemClickEvent(dataList[index]) }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    listItemContent(index)
                }
            }
            if (hasMore) {
                item {
                    hasMoreAction()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
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
            message = Constants.NO_MORE,
        )
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}