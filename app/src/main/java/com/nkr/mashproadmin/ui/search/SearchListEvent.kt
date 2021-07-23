package com.nkr.mashproadmin.ui.search





sealed class SearchListEvent {
    data class OnPopulateKeywordSuggestion(val query_string: String) : SearchListEvent()
    object OnPopulateSavedSearches : SearchListEvent()
    data class OnPopulateQueryItems(val query_string : String) : SearchListEvent()
    object OnClearSavedSearches : SearchListEvent()


}