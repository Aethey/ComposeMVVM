package com.example.gitsimpledemo.ui.userlist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gitsimpledemo.R
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.ui.userlist.UserListState

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description:
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchView(
    state: UserListState,
    searchHistory: List<SearchHistoryEntity>,
    paddingValues: PaddingValues,
    height: Int,
    onSearch: (searchQuery: String) -> Unit,
    onClearHistory: () -> Unit
) {
    AnimatedVisibility(
        visible = state.isSearching,
        enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
    ) {
        Column {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
//            search history list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((height * 1 / 2 - 80).dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(searchHistory.size) { i ->
                    ListItem {
                        Row(
                            modifier = Modifier
                                .height(48.dp)
                                .clickable {
                                    onSearch(searchHistory[i].searchQuery)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            println("searchHistory is ${searchHistory[i].searchQuery}")
                            Image(
                                painter = painterResource(id = R.drawable.header_placeholder),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp),
                            )
                            Text(
                                text = searchHistory[i].searchQuery,
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                CustomButton("CLEAR") { onClearHistory() }
//                CustomButton("SEARCH USER"){}
            }
        }
    }
}

@Composable
private fun CustomButton(content:String,onClick:()->Unit){
    Box(
        modifier = Modifier.padding(6.dp)
            .border(
                width = 2.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(12.dp)
            ).clickable {
            onClick()
        }
    ) {
        Text(
            content, modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}
