package com.nkr.mashpro.ui.moviePlayer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.nkr.bazaranocustomer.util.StorageUtil
import com.nkr.mashpro.R
import com.nkr.mashpro.databinding.MoviePlayerFragmentBinding
import com.nkr.mashpro.model.Movie


class MoviePlayerFragment : Fragment() {

    private lateinit var binding: MoviePlayerFragmentBinding
    private lateinit var viewModel: MoviePlayerViewModel

    private val safeArgs : MoviePlayerFragmentArgs by navArgs()

    private lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private lateinit var movie : Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = safeArgs.movie
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
        viewModel = ViewModelProvider(this).get(MoviePlayerViewModel::class.java)
        // TODO: Use the ViewModel


    }


    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player

        val video_file_loc = "https://firebasestorage.googleapis.com/v0/b/mashpro-e4cc0.appspot.com/o/video_movie%2F7REe2OGCsgeyT7MBsycAQVMTUIi21624605832912?alt=media&token=be7f3dc1-0d2e-4255-8c50-6ab07cae18da"
        //val mediaItem: MediaItem = MediaItem.fromUri(getString(R.string.media_url_mp3))
        val video_uri = StorageUtil.pathToReference(movie.video_url)
        val mediaItem: MediaItem = MediaItem.fromUri(video_file_loc)

        player.setMediaItem(mediaItem)

        player.playWhenReady = playWhenReady;
        player.seekTo(currentWindow, playbackPosition)
        player.prepare()

    }



/*

    private fun initializeExoPlayerView(video_url:String){
        try {
            // bandwidthmeter is used for getting default bandwidth
            val bandwidthMeter =  DefaultBandwidthMeter();
            // track selector is used to navigate between video using a default seeker.
            val trackSelector =  DefaultTrackSelector( AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
           val exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            // we are parsing a video url and
            // parsing its video uri.
            val videouri = Uri.parse(video_url);

            // we are creating a variable for data source
            // factory and setting its user agent as 'exoplayer_view'
            val dataSourceFactory =  DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor
            // factory and setting it to default extractor factory.
            val extractorsFactory =  DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,
            val mediaSource =  ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

            // inside our exoplayer view
            // we are setting our player
             exoPlayerView.setPlayer(exoPlayer);

            // we are preparing our exoplayer
            // with media source.
            exoPlayer.prepare(mediaSource);

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            // below line is used for handling our errors.
           // Log.e("TAG", "Error : " + e.toString());
        }
    }

*/


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