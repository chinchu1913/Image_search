package com.example.search_image.presentation.ui.homescreen

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.search_image.R
import com.example.search_image.presentation.ui.common.ConnectedComponent
import com.example.search_image.presentation.ui.common.NotConnectedComponent
import com.example.search_image.presentation.ui.destinations.ImageDetailsScreenDestination
import com.example.search_image.presentation.ui.homescreen.viewmodel.HomeScreenViewModel
import com.example.search_image.presentation.ui.homescreen.viewmodel.SearchEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun BodyComponent(
    navigator: DestinationsNavigator,
    viewModel: HomeScreenViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val state = viewModel.state
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val index = rememberSaveable { mutableStateOf(0) }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    SearchEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(id = R.string.home_screen_place_holder))
            },
            maxLines = 1,
            singleLine = true
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isPortrait) 2 else 3),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(state.searchLists.size) { i ->
                val search = state.searchLists[i]
                Box(modifier = Modifier.clickable {
                    openDialog.value = true
                    index.value = i
                }) {
                    ListItem(
                        search.user ?: "", search.tags ?: "",
                        search.previewURL ?: ""
                    )
                }
            }

        }

    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (viewModel.baseState.showNetworkUnavailable) {
        NotConnectedComponent()
    }

    if (viewModel.baseState.showNetworkConnected) {
        ConnectedComponent()
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(id = R.string.home_screen_alert_dialog_title))
            },
            text = {
                Text(
                    stringResource(id = R.string.home_screen_alert_dialog_body)
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.home_screen_alert_dialog_negative))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            openDialog.value = false
                            navigator.navigate(
                                ImageDetailsScreenDestination(
                                    search = state.searchLists[index.value]
                                )
                            )
                        }
                    ) {
                        Text(stringResource(id = R.string.home_screen_alert_dialog_positive))
                    }
                }

            }
        )
    }

}