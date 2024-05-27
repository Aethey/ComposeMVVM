package com.example.gitsimpledemo.ui.userdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gitsimpledemo.model.entity.RepoItem
import com.example.gitsimpledemo.ui.theme.Coral
import com.example.gitsimpledemo.ui.theme.LanguageDefaultString
import com.example.gitsimpledemo.ui.theme.LightBlue
import com.example.gitsimpledemo.ui.theme.Purple
import com.example.gitsimpledemo.util.CommonUtils
import com.example.gitsimpledemo.util.LanguageColorManager

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 * user repositories list view
 * time line style
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeLineView(
    repoList: List<RepoItem>,
    width: Int,
    onOpenWeb: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(repoList.size) { index ->
            ListItem {
                // get language color from LanguageColorManager by {repoList[index].node.primaryLanguage}
                val colorString = repoList[index].node.primaryLanguage?.let {
                    LanguageColorManager.getColor(
                        it.name
                    )
                } ?: LanguageDefaultString
                val textColorString = LanguageColorManager.getAdvancedContrastColor(colorString)
                val nextColorString = if (index + 1 < repoList.size) {
                    repoList[index + 1].node.primaryLanguage?.let {
                        LanguageColorManager.getColor(it.name)
                    } ?: LanguageDefaultString
                } else {
                    LanguageDefaultString
                }
                val color = Color(android.graphics.Color.parseColor(colorString))
                val textColor = Color(android.graphics.Color.parseColor(textColorString))
                val nextColor = Color(android.graphics.Color.parseColor(nextColorString))

                TimeLineItem(
                    position = TimelineNodePosition.MIDDLE,
                    circleParameters = CircleParametersDefaults.circleParameters(
                        backgroundColor = color
                    ),
                    lineParameters = LineParametersDefaults.linearGradient(
                        startColor = color,
                        endColor = nextColor
                    ),
                ) { modifier ->
                    Column(
                        modifier = modifier
                            .width(width.dp)
                            .padding(start = 10.dp),
                    ) {
                        Text(
                            CommonUtils.convertIsoToSimpleDate(repoList[index].node.createdAt),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                            //colors = CardDefaults.cardColors(containerColor = color)
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Column(
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                repoList[index].node.primaryLanguage?.let {
                                    TagView(
                                        it.name, color, textColor
                                    )
                                }
                                    ?: TagView("Unknown", color, textColor)
                                Text(
                                    repoList[index].node.name,
                                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                repoList[index].node.description?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(start = 12.dp, top = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Localized description",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(CommonUtils.formatNumber(repoList[index].node.stargazers.totalCount))
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        "Open Link",
                                        modifier = Modifier
                                            .padding(end = 12.dp)

                                            .clickable {
                                                onOpenWeb(repoList[index].node.url)
                                            },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.Blue,
                                            textDecoration = TextDecoration.Underline
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TagView(name: String, containerColor: Color, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top

    ) {
        Box(
            modifier = Modifier
                .background(containerColor, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall.copy(color = textColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLineViewPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TimeLineItem(
            position = TimelineNodePosition.FIRST,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = LightBlue
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = LightBlue,
                endColor = Purple
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = LightBlue) }

        TimeLineItem(
            position = TimelineNodePosition.MIDDLE,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = Purple
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = Purple,
                endColor = Coral
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = Purple) }
        TimeLineItem(
            position = TimelineNodePosition.MIDDLE,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = Purple
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = Purple,
                endColor = Coral
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = Purple) }

        TimeLineItem(
            TimelineNodePosition.LAST,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = Coral
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = Coral) }
    }
}

@Composable
private fun MessageBubble(modifier: Modifier, containerColor: Color) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(100.dp)
            .padding(start = 10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Text("this is a test")
    }
}