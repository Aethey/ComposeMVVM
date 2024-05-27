package com.example.gitsimpledemo.ui.components

import android.util.DisplayMetrics
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitsimpledemo.R
import kotlinx.coroutines.delay

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description: custom toast when data no more
 */

@Composable
fun ShowCustomToast(
    showToastState: Boolean,
    message: String,
) {
    if (showToastState) {
        CustomToast(MessageType.DEFAULT, message)
    }
}

@Composable
fun CustomToast(
    messageType: MessageType = MessageType.DEFAULT,
    message: String = "default message",
    height: Dp = 60.dp,
    width: Dp? = null,
    onDismissCallback: @Composable () -> Unit = {},
) {
    val isTransitionStarted = remember { mutableStateOf(false) }
    val clipShape = remember { mutableStateOf(CircleShape) }
    val slideAnimation = remember { mutableStateOf(true) }
    val animationStarted = remember { mutableStateOf(false) }
    val showMessage = remember { mutableStateOf(false) }
    val dismissCallback = remember { mutableStateOf(false) }

    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthInDp = width ?: (displayMetrics.widthPixels / displayMetrics.density * 0.5).dp

    val boxWidth = animateDpAsState(
        targetValue = if (isTransitionStarted.value) screenWidthInDp else 30.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Box width",
    )

    val boxHeight = animateDpAsState(
        targetValue = if (isTransitionStarted.value) height else 30.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Box Height",
    )

    val slideY = animateDpAsState(
        targetValue = if (slideAnimation.value) 100.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "Slide parameter in DP",
    )

    if (!animationStarted.value) {
        LaunchedEffect(Unit) {
            slideAnimation.value = false

            // Delay for 0.33 seconds before transitioning to rectangle
            delay(330)
            isTransitionStarted.value = true
            clipShape.value = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)
            showMessage.value = true

            // Delay for 2 seconds before transitioning back to circle
            delay(2000)
            isTransitionStarted.value = false
            showMessage.value = false

            // Delay for 0.33 seconds before sliding down
            delay(330)
            clipShape.value = CircleShape
            slideAnimation.value = true
            animationStarted.value = true
            dismissCallback.value = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .height(boxHeight.value)
                .width(boxWidth.value)
                .offset(y = slideY.value)
                .clip(clipShape.value)
                .background(getColorForMessageType(messageType))
                .align(alignment = Alignment.BottomCenter),
            contentAlignment = Alignment.Center,
        ) {
            if (showMessage.value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(R.drawable.cry),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                    Text(
                        text = message,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp),
                    )
                }
            }

            if (dismissCallback.value) onDismissCallback()
        }
    }
}

enum class MessageType {
    SUCCESS,
    ERROR,
    DEFAULT,
    DYNAMIC,
}

@Composable
fun getColorForMessageType(messageType: MessageType): Color {
    return when (messageType) {
        MessageType.SUCCESS -> Color.Green
        MessageType.ERROR -> Color.Red
        MessageType.DYNAMIC -> Color.White
        MessageType.DEFAULT -> Color.White
    }
}