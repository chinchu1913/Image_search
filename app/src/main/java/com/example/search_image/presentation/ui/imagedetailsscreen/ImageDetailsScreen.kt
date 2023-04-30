package com.example.search_image.presentation.ui.imagedetailsscreen

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.search_image.R
import com.example.search_image.domain.entities.Search
import com.example.search_image.presentation.ui.common.ConnectedComponent
import com.example.search_image.presentation.ui.common.NotConnectedComponent
import com.example.search_image.presentation.ui.imagedetailsscreen.viewmodel.ImageDetailsScreenViewModel
import com.example.search_image.ui.theme.White
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ImageDetailsScreen(
    navigator: DestinationsNavigator,
    search: Search,
    viewModel: ImageDetailsScreenViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Scaffold(modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background, topBar = {
            AppBarComponent(navigator = navigator)
        },
        content = { paddingValues ->

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                if (isPortrait) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        UserDetailsHeader(search = search)
                        LargeImage(search = search, context = context)
                        Spacer(modifier = Modifier.height(16.dp))
                        ImageInfoComponent(search = search)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            UserDetailsHeader(search = search)
                            ImageInfoComponent(search = search)
                        }
                        Box(
                            modifier = Modifier.weight(2f)
                        ) {
                            LargeImage(search = search, context)
                        }

                    }
                }
            }

            if (viewModel.baseState.showNetworkUnavailable) {
                NotConnectedComponent()
            }

            if (viewModel.baseState.showNetworkConnected) {
                ConnectedComponent()
            }


        }
    )
}

@Composable
fun SingleInfoItem(title: String, value: String?) {
    Column {
        Row(modifier = Modifier) {
            Text(
                text = "${title}: ",
                style = MaterialTheme.typography.subtitle2
            )
            Text(text = value ?: "", style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color.Gray.copy(alpha = 0.15f))
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun UserDetailsHeader(search: Search) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(50.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(search.userImageURL)
                    .placeholder(R.drawable.ic_user_icon)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.image_details_screen_user_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                error = painterResource(id = R.drawable.ic_user_icon)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = search.user ?: "",
            style = MaterialTheme.typography.subtitle1
        )
    }
}


@Composable
fun LargeImage(search: Search, context: Context) {
    val imageAspectRatio =
        search.imageWidth / search.imageHeight

    val isFullImageLoaded = remember { mutableStateOf(false) }

    if (isFullImageLoaded.value) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = search.largeImageURL)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = stringResource(id = R.string.image_details_screen_full_image),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio)
        )
    } else {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(search.previewURL)
                .placeholder(R.drawable.ic_image_default)
                .crossfade(true)
                .build(),
            loading = {
                Box(
                    modifier = Modifier.size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Image(
                    painter = painterResource(R.drawable.ic_image_default), contentDescription =
                    stringResource(id = R.string.image_details_screen_full_image)
                )
            },
            contentDescription = stringResource(id = R.string.image_details_screen_full_image),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio)
        )
    }

    LaunchedEffect(search.largeImageURL) {
        if (!isFullImageLoaded.value) {
            val request = ImageRequest.Builder(context)
                .data(search.largeImageURL)
                .build()
            val result = context.imageLoader.execute(request)

            if (result is SuccessResult) {
                isFullImageLoaded.value = true
            }
        }
    }
}

@Composable
fun ImageInfoComponent(search: Search) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        SingleInfoItem(
            title = stringResource(id = R.string.image_details_screen_uploaded_by),
            value = search.user
        )
        SingleInfoItem(
            title = stringResource(id = R.string.image_details_screen_tags),
            value = search.tags
        )
        SingleInfoItem(
            title = stringResource(id = R.string.image_details_screen_likes),
            value = search.likes.toString()
        )
        SingleInfoItem(
            title = stringResource(id = R.string.image_details_screen_downloads),
            value = search.downloads.toString()
        )
        SingleInfoItem(
            title = stringResource(id = R.string.image_details_screen_comments),
            value = search.comments.toString()
        )
    }
}

@Composable
fun AppBarComponent(navigator: DestinationsNavigator) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = stringResource(id = R.string.image_details_screen_back_button),
            modifier = Modifier.clickable {
                navigator.popBackStack()
            },
            tint = White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            stringResource(id = R.string.image_details_screen_title),
            style = MaterialTheme.typography.h4,
        )
    }
}