package com.monsalud.bookshelf.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit
) {
    val titles = listOf(
        stringResource(id = R.string.onboarding_title_1),
        stringResource(id = R.string.onboarding_title_2),
        stringResource(id = R.string.onboarding_title_3),
    )
    val items = listOf(
        stringResource(id = R.string.onboarding_body_1),
        stringResource(id = R.string.onboarding_body_2),
        stringResource(id = R.string.onboarding_body_3),
    )
    val backgroundImage = painterResource(id = R.drawable.onboarding_image)
    val pagerState = rememberPagerState(pageCount = { items.size })

    val tintColor = if (isSystemInDarkTheme()) {
        Color.Black.copy(alpha = 0.7f)
    } else {
        Color.White.copy(alpha = 0.7f)
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(MaterialTheme.spacing.medium),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = MaterialTheme.spacing.small
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(MaterialTheme.spacing.medium)
                    )
                    .paint(
                        painter = backgroundImage,
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(
                            color = tintColor,
                            blendMode = BlendMode.SrcOver
                        )
                    )
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(MaterialTheme.spacing.small)
                        .zIndex(1f)
                        .testTag("closeButton")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        Modifier
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.medium),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = titles[page],
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small)
                                    .align(Alignment.Start)
                            )
                            Text(
                                text = items[page],
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.Black else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small)
                                    .background(color, CircleShape)
                                    .size(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun OnboardingDialogPreview() {
    OnboardingDialog(onDismiss = {})
}
