package com.nkr.mashpro.ui.search

import android.app.SearchManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseListAdapter
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.SearchFragmentBinding
import com.nkr.mashpro.model.Keyword
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : SearchFragmentBinding
    lateinit var searchPlate : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListener()
    }



    private fun setupListener() {
        //local
        viewModel.searchWordsAdapter.listener = object : BaseListAdapter.AdapterListener{
            override fun onItemClick(item: BaseListAdapter.ListItemViewModel) {
                //get the searc word n query again
                val search_word = item as SearchedWord
                Timber.i("search_item_click ${search_word.searched_word}")
                searchPlate.setText(search_word.searched_word)
                searchPlate.setSelection(search_word.searched_word.length)
                viewModel.handleEvent(SearchListEvent.OnPopulateQueryItems(search_word.searched_word))

            }
        }


        //remote
        viewModel.keywordAdapter.listener = object : BaseListAdapter.AdapterListener{
            override fun onItemClick(item: BaseListAdapter.ListItemViewModel) {
                //get the searc word n query again

                Timber.i("search_item_click ${(item as Keyword).toString()}")
                val search_word = item as Keyword
                searchPlate.setText(search_word.keyword)
                searchPlate.setSelection(search_word.keyword.length)
                viewModel.handleEvent(SearchListEvent.OnPopulateQueryItems(search_word.keyword))
                if(viewModel.isSavedSearchEmpty.value == false){
                    viewModel.isSavedSearchEmpty.value = true
                }

            }
        }

        binding.tvClearAllSavedSearches.setOnClickListener {
            viewModel.handleEvent(SearchListEvent.OnClearSavedSearches)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_view_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    return true
                }
            })

            searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search"


            val searchPlateView: View =
                searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(viewModel.isSavedSearchEmpty.value == false){
                        viewModel.isSavedSearchEmpty.value = true
                    }
                    viewModel.handleEvent(SearchListEvent.OnPopulateQueryItems(query.toString()))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //populate suggestion recyclerview here
                    newText?.let {
                        if(it.isNotEmpty()){
                            if(viewModel.isSearched.value == true) {
                                viewModel.isSearched.value = false
                            }
                            viewModel.showNoData.value = false
                            viewModel.handleEvent(SearchListEvent.OnPopulateKeywordSuggestion(newText.toString()))
                        }
                    }
                    return false
                }
            })

            val searchManager =
                requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

}