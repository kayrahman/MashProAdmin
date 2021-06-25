package com.nkr.mashpro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.bazaranocustomer.util.GridSpacingItemDecoration
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.FragmentHomeBinding
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override val _viewModel: BaseViewModel
        get() = homeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.handleEvent(HomeEvent.OnFetchMovies)
        setupListener()
        setupAdapter()
        observeViewModel()

    }

    private fun setupAdapter() {
        binding.rvMovies.adapter = homeViewModel.adapter
        val spacing = 10 // 50px
        val includeEdge = false
        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(3, spacing, includeEdge))
        homeViewModel.movieList.observe(viewLifecycleOwner, Observer {
            Timber.i("flow_prod_list:${it.size} ")
            //populate the adapter here
            homeViewModel.adapter.submitList(it)
        })
    }

    private fun setupListener() {
        homeViewModel.adapter.listener = MovieListAdapter.MovieItemClickListener{
            // go to movie player
            val actionMoviePlayer = HomeFragmentDirections.actionNavigationHomeToMoviePlayerFragment(it)
            homeViewModel.navigationCommand.value = NavigationCommand.To(actionMoviePlayer)
        }
    }

    private fun observeViewModel() {
      //  homeViewModel.handleEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}