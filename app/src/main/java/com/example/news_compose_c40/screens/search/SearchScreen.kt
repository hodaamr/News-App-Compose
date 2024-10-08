package com.example.news_compose_c40.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news_compose_c40.R
import com.example.news_compose_c40.ui.theme.green
import com.example.news_compose_c40.ui.theme.green_50
import com.example.news_compose_c40.ui.theme.transparent
import com.example.news_compose_c40.util.getErrorMessage
import com.example.news_compose_c40.widgets.ErrorDialog
import com.example.news_compose_c40.widgets.NewsList
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

@Composable
fun SearchScreen(vm: SearchViewModel = hiltViewModel(), onNewsClick: (String, String) -> Unit) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .paint(painterResource(id = R.drawable.bg_pattern), contentScale = ContentScale.Crop)
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight(.1f)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
                .background(green)
                .padding(horizontal = 30.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            TextField(
                value = vm.searchQuery,
                onValueChange = { vm.setSearchQuery(it) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = green,
                    unfocusedTextColor = green_50,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = transparent,
                    unfocusedIndicatorColor = transparent,
                    cursorColor = green_50
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(30.dp))
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        vm.setIsFocused(focusState.isFocused)
                    },
                placeholder = {
                    Text(stringResource(id = R.string.search_articles))
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ), keyboardActions = KeyboardActions(onSearch = {
                    vm.getNews()
                    focusManager.clearFocus()
                }
                ), leadingIcon = {
                    Image(
                        painterResource(id = R.drawable.search_icon),
                        contentDescription = stringResource(
                            id = R.string.search
                        ),
                        modifier = Modifier
                            .clickable {
                                vm.getNews()
                                focusManager.clearFocus()
                            }
                    )
                }, trailingIcon =
                {
                    if (vm.isFocused) {
                        Image(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = stringResource(id = R.string.close),
                            modifier = Modifier.clickable {
                                if (vm.searchQuery.isNotEmpty())
                                    vm.setSearchQuery("")
                                else
                                    focusManager.clearFocus()

                            }
                        )
                    }
                }

            )

        }

        if (vm.isErrorDialogVisible){
            val errorMessage = getErrorMessage(errorMessage = vm.uiMessage.errorMessage, errorMessageId = vm.uiMessage.errorMessageId)
            ErrorDialog(errorMessage = errorMessage, onRetry = vm.uiMessage.retryAction) { vm.hideErrorDialog() }

            }


        NewsList(
            newsList = vm.articlesList,
            shouldDisplayNoArticlesFound = vm.uiMessage.shouldDisplayNoArticlesFound,
            loadingState = vm.uiMessage.isLoading
        ) { title, sourceName ->
            onNewsClick(title, sourceName)

        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen{ _, _ -> }

}
