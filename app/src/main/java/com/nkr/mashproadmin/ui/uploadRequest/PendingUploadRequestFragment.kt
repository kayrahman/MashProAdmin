package com.nkr.mashproadmin.ui.uploadRequest

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashproadmin.R
import com.nkr.mashproadmin.base.BaseFragment
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.databinding.PendingUploadRequestFragmentBinding
import com.nkr.mashproadmin.ui.downloads.DownloadedMovieListAdapter
import com.nkr.mashproadmin.ui.downloads.DownloadsFragmentDirections
import com.nkr.mashproadmin.util.KEY_ACCEPT
import com.nkr.mashproadmin.util.KEY_REJECT
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PendingUploadRequestFragment : BaseFragment() {
    private val viewModel: PendingUploadRequestViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : PendingUploadRequestFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = PendingUploadRequestFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.handleEvent(UploadRequestEvent.OnFetchPendingVideos)

        setupAdapter()

    }

    private fun setupAdapter() {
        val adapter = PendingMovieListAdapter()
        binding.rvPendingMovies.adapter = adapter
        viewModel.pendingMovies.observe(viewLifecycleOwner, Observer {
            Timber.i("flow_prod_list:${it.size} ")
            //populate the adapter here
            adapter.submitList(it)
        })

        val status_listener = { movie_uid:String ,status:Int ->
            when(status){
                KEY_ACCEPT -> {
                    viewModel.handleEvent(UploadRequestEvent.OnAcceptPendingVideos(movie_uid))
                }
                KEY_REJECT -> {
                    viewModel.handleEvent(UploadRequestEvent.OnRejectPendingVideos(movie_uid))
                }
            }
        }

        adapter.listener = PendingMovieListAdapter.MovieItemClickListener({
            // go to movie player
          //  val actionMoviePlayer = DownloadsFragmentDirections.actionDownloadsFragmentToMoviePlayerFragment(it)
          //  viewModel.navigationCommand.value = NavigationCommand.To(actionMoviePlayer)
        },status_listener)

    }
    }

