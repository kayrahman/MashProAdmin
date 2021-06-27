package com.nkr.mashpro.ui.downloads

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nkr.mashpro.R
import com.nkr.mashpro.databinding.DownloadsFragmentBinding
import com.nkr.mashpro.databinding.ItemListMovieHorizontalBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadsFragment : Fragment() {

    private val viewModel: DownloadsViewModel by viewModel()
    private lateinit var binding: DownloadsFragmentBinding

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

    }

}