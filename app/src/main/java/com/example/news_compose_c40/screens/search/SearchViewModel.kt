package com.example.news_compose_c40.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_compose_c40.R
import com.example.news_compose_c40.model.article.Article
import com.example.news_compose_c40.model.article.ArticlesResponse
import com.example.news_compose_c40.util.UIMessage
import com.example.news_compose_c40.util.fromJson
import com.route.newsappc40gsat.api.NewsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val newsService: NewsService) : ViewModel(){

    private val _searchQuery = mutableStateOf("")
    val searchQuery: String get() = _searchQuery.value

    private val _isFocused = mutableStateOf<Boolean>(false)
    val isFocused: Boolean get() = _isFocused.value

    private var _articlesList =mutableStateOf<List<Article>?>(null)
    val articlesList: List<Article>? get() = _articlesList.value

    private val _uiMessage = mutableStateOf(UIMessage())
    val uiMessage: UIMessage get() = _uiMessage.value

    private val _isErrorDialogVisible = mutableStateOf(false)
    val isErrorDialogVisible: Boolean get() = _isErrorDialogVisible.value


    fun setSearchQuery(query: String){
        _searchQuery.value = query

    }

    fun setIsFocused(isFocused: Boolean){
        _isFocused.value = isFocused

    }

    fun showErrorDialog() {
        _isErrorDialogVisible.value = true
    }

    fun hideErrorDialog() {
        _isErrorDialogVisible.value = false
    }

    fun getNews(){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _uiMessage.value = UIMessage(isLoading = true)
                val articles = newsService.getArticles(_searchQuery.value).articles
                if (!articles.isNullOrEmpty()) {
                    _articlesList.value = articles
                }
                else {
                    _uiMessage.value = UIMessage(shouldDisplayNoArticlesFound = true)
                }

                _uiMessage.value = UIMessage(isLoading = false)
            }
        }catch (e: HttpException) {
            val articlesResponse = e.response()?.errorBody()?.string()?.fromJson(
                ArticlesResponse::class.java
            )
            _uiMessage.value= UIMessage(
                isLoading = false,
                errorMessage = articlesResponse?.message,
                retryAction = {
                    getNews()
                })
            _isErrorDialogVisible.value = true


        } catch (e: UnknownHostException) {

            _uiMessage.value = UIMessage(
                isLoading = false,
                errorMessageId = R.string.connection_error,
                retryAction = {
                    getNews()
                })
            _isErrorDialogVisible.value = true

        } catch (e: Exception) {
            _uiMessage.value = UIMessage(
                isLoading = false,
                errorMessage = e.localizedMessage,
                retryAction = {
                    getNews()
                })

            _isErrorDialogVisible.value = true

        }

    }

}