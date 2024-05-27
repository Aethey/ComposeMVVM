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
fun TimeLineItem(repoList: List<RepoItem>, index: Int, width: Int, onOpenWeb: (String) -> Unit) {
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

        TimeLineItemPart(
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
                // repo create time
                RepoCreateTime(repoList[index].node.createdAt)
                // repo content
                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    //colors = CardDefaults.cardColors(containerColor = color)
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        // repo language
                        repoList[index].node.primaryLanguage?.let {
                            RepoLanguageTag(
                                it.name, color, textColor
                            )
                        } ?: RepoLanguageTag("Unknown", color, textColor)
                        // repo name
                        RepoName(repoList[index].node.name)
                        // repo description
                        repoList[index].node.description?.let {
                            RepoDescription(it)
                        }
                        // repo star
                        RepoFooter(
                            repoList[index].node.stargazers.totalCount,
                        ) {
                            onOpenWeb(repoList[index].node.url)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RepoCreateTime(createdAt: String) {
    Text(
        text = CommonUtils.convertIsoToSimpleDate(createdAt),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}


@Composable
fun RepoName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}


@Composable
fun RepoDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = Modifier.padding(12.dp)
    )
}

@Composable
fun RepoFooter(starCount: Long, onOpenWeb: () -> Unit) {
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Star icon",
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = CommonUtils.formatNumber(starCount),
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Open Link",
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable { onOpenWeb() },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ),
        )
    }
}


@Composable
fun RepoLanguageTag(name: String, containerColor: Color, textColor: Color) {
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
        TimeLineItemPart(
            position = TimelineNodePosition.FIRST,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = LightBlue
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = LightBlue,
                endColor = Purple
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = LightBlue) }

        TimeLineItemPart(
            position = TimelineNodePosition.MIDDLE,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = Purple
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = Purple,
                endColor = Coral
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = Purple) }
        TimeLineItemPart(
            position = TimelineNodePosition.MIDDLE,
            circleParameters = CircleParametersDefaults.circleParameters(
                backgroundColor = Purple
            ),
            lineParameters = LineParametersDefaults.linearGradient(
                startColor = Purple,
                endColor = Coral
            ),
        ) { modifier -> MessageBubble(modifier, containerColor = Purple) }

        TimeLineItemPart(
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