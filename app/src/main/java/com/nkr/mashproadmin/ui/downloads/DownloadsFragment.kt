package com.nkr.mashproadmin.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashproadmin.base.BaseFragment
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.databinding.DownloadsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DownloadsFragment : BaseFragment() {

    private val viewModel: DownloadsViewModel by viewModel()
    private lateinit var binding: DownloadsFragmentBinding
    override val _viewModel: BaseViewModel
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.handleEvent(DownloadEvent.OnFetchDownloadedVideos)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DownloadsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupAdapter()
    }

    private fun setupAdapter() {
        val adapter = DownloadedMovieListAdapter()
        binding.rmMovie.adapter = adapter
        viewModel.downloadedMovies.observe(viewLifecycleOwner, Observer {
            Timber.i("flow_prod_list:${it.size} ")
            //populate the adapter here
            adapter.submitList(it)
        })

        adapter.listener = DownloadedMovieListAdapter.MovieItemClickListener{
            // go to movie player
            val actionMoviePlayer = DownloadsFragmentDirections.actionDownloadsFragmentToMoviePlayerFragment(it)
            viewModel.navigationCommand.value = NavigationCommand.To(actionMoviePlayer)
        }

    }


}