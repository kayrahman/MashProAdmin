package com.nkr.mashpro.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseListAdapter
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.model.Keyword
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.ui.home.MovieListAdapter
import kotlinx.coroutines.launch


class SearchViewModel(app: Application, val repo: IRepoDataSource) : BaseViewModel(app) {


    val movieList = MutableLiveData<List<Movie>>()
    val isItemFound = MutableLiveData<Boolean>(false)
    val searchQuery = MutableLiveData<String>()
    var searchWords = MutableLiveData<List<SearchedWord>>()

    var searchWordsAdapter = BaseListAdapter<SearchedWord>(R.layout.item_search_word)

    val isSearched = MutableLiveData<Boolean>(false)
    val isSavedSearchEmpty = MutableLiveData<Boolean>(true)
    val isSearchTypeRemoteKeywords = MutableLiveData(false)


    val keywordAdapter = BaseListAdapter<Keyword>(R.layout.item_search_remote_key_word)

    fun handleEvent(event: SearchListEvent) {
        when (event) {
            is SearchListEvent.OnPopulateSavedSearches -> {
                getSearchedWords()
            }

            is SearchListEvent.OnClearSavedSearches -> clearSavedSearches()

            is SearchListEvent.OnPopulateQueryItems -> {
                queryString.value = event.query_string
                populateSearchItemList(event.query_string)
            }

            is SearchListEvent.OnPopulateKeywordSuggestion -> {
             //  populateKeywordSuggestions(event.query_string)
            }


        }

    }

/*

    private fun populateKeywordSuggestions(queryString: String) = viewModelScope.launch {
        delay(500)
        val keywordResponse = repo.fetchKeyWordSuggestions(queryString)
        when (keywordResponse) {
            is Result.Success -> {
                if (keywordResponse.data.isNotEmpty()) {
                    isSearchTypeRemoteKeywords.value = true
                    val sortedList = keywordResponse.data.distinctBy {
                        it.keyword
                    }

                    keywordAdapter.updateAdapterList(sortedList)
                    Timber.i("keyword_search_status : ${keywordResponse.data.toString()}")
                }
            }
            is Result.Error -> {
                Timber.i("keyword_search_status : error ${keywordResponse.exception}")
            }
        }
    }

*/


    val queryString = MutableLiveData<String>()
    private fun clearSavedSearches() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val clear_searched_word_response = repo.clearAllSearchedWords()
            when (clear_searched_word_response) {
                is Result.Success -> {
                    getSearchedWords()
                    Log.d("local_db_delete", "success")
                }
                is Result.Error -> {
                    Timber.i("search_word_insert : ${clear_searched_word_response.exception}")
                }
            }

        }

    }


/*

    val searched_products = queryString.switchMap {
        repo.observeProductsBySearchTitle(it.trim()).catch { throwable ->
            showSnackBar.value = throwable.message
        }.asLiveData()

    }
*/



    fun populateSearchItemList(queryString: String) = viewModelScope.launch {
        isSearched.value = true
        searchQuery.value = queryString

        val searchWord = RoomSearchedWord(searched_word = queryString)
        insert(searchWord)
        val search_resposne = repo.fetchProductsBySearch(queryString)
        when (search_resposne) {
            is Result.Success -> {
                search_resposne.data.let {
                    movieList.value = it
                    showNoData.value = it.isEmpty()
                    if(it.isEmpty()){
                        isSearched.value = false
                        getSearchedWords()
                    }
                }

                Timber.i("search_word_response : Success ${search_resposne.data.toString()}")
            }
            is Result.Error -> {

                Timber.i("search_word_response : ${search_resposne.exception}")
            }
        }

    }


    private suspend fun insert(searchWordRoom: RoomSearchedWord) {
        withContext(Dispatchers.Default) {
            val insert_searched_word_response = repo.insertSearchedWord(searchWordRoom)
            when (insert_searched_word_response) {
                is Result.Success -> {
                    Log.d("local_db_insert", "success")
                }
                is Result.Error -> {
                    Timber.i("search_word_insert : ${insert_searched_word_response.exception}")
                }
            }

        }
    }


    private fun getSearchedWords() = viewModelScope.launch {
        val searched_word_response = repo.getSearchedWords()
        when (searched_word_response) {
            is Result.Success -> {
                searchWords.value = searched_word_response.data!!
                isSavedSearchEmpty.value = searched_word_response.data.isNotEmpty()
                searchWordsAdapter.updateAdapterList(searched_word_response.data)
                Timber.i("search_word_fetch_status : ${searched_word_response.data}")
            }
            is Result.Error -> {
                Timber.i("search_word_fetch_error : ${searched_word_response.exception}")
            }
        }
    }

}