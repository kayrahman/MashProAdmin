package com.nkr.mashpro.ui.moviePlayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.bazaranocustomer.util.GridSpacingItemDecoration
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.MoviePlayerFragmentBinding
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.ui.home.HomeFragmentDirections
import com.nkr.mashpro.ui.home.MovieListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MoviePlayerFragment : BaseFragment() {

    private lateinit var binding: MoviePlayerFragmentBinding
    private val viewModel: MoviePlayerViewModel by viewModel()

    override val _viewModel: BaseViewModel
        get() = viewModel


    private val safeArgs: MoviePlayerFragmentArgs by navArgs()

    private lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = safeArgs.movie

        viewModel.setCurrentMovie(movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoviePlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.handleEvent(MoviePlayerEvent.OnFetchMovies)

        binding.rvMoviesNew.adapter = viewModel.adapter
        val spacing = 10 // 50px
        val includeEdge = false
        // binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(3, spacing, includeEdge))
        viewModel.movieListNew.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.submitList(it)
        })

        //adapter setup listener
        viewModel.adapter.listener = MovieListHorizontalAdapter.MovieItemClickListener {
            movie = it
            viewModel.setCurrentMovie(it)
            initializePlayer()
        }


        val slide_adapter  = MovieListHorizontalAdapter()
        binding.rvSlideMovies.adapter = slide_adapter
        binding.rvSlideMovies.addItemDecoration(GridSpacingItemDecoration(3, spacing, includeEdge))
        viewModel.movieListSlide.observe(viewLifecycleOwner, Observer {
            Timber.i("flow_prod_list:${it.size} ")
            //populate the adapter here
            slide_adapter.submitList(it)

        })

        slide_adapter.listener = MovieListHorizontalAdapter.MovieItemClickListener {
            movie = it
            viewModel.setCurrentMovie(it)
            initializePlayer()
        }



        binding.ivDownload.setOnClickListener {
            viewModel.handleEvent(MoviePlayerEvent.OnDownloadMovie(movie))
        }

    }


    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player

       // val file_loc = "file:///data/user/0/com.nkr.mashpro/cache/MashupPro2336716059174186187mp4"
        val mediaItem: MediaItem = MediaItem.fromUri(movie.video_url)

        player.setMediaItem(mediaItem)
       // player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        player.prepare()


    }


    private class PlaybackStateListener : Player.EventListener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String
            stateString = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }

        }
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    fun releasePlayer() {
        if (player != null) {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentWindowIndex
            player.stop()
            player.release()
            //  player = null
        }

    }

}