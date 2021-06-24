package com.nkr.mashpro.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nkr.mashpro.R
import com.nkr.mashpro.databinding.UploadFragmentBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.upload_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class UploadFragment : Fragment() {

    private val viewModel: UploadViewModel by viewModel()
    private lateinit var binding : UploadFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UploadFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListener()

        populateMovieYearNTypeDropDown()

    }

    private fun setupListener() {

        binding.ivThumbnail.setOnClickListener {
            chooseImageFromGallery()
        }

        binding.ivMovie.setOnClickListener {
            chooseVideoFromGallery()
        }

        binding.btnUpload.setOnClickListener {
            viewModel.handleEvent(UploadEvent.OnUploadMovieInfo)
        }
    }

    private fun chooseImageFromGallery() {
        if (allPermissionsGranted()) {
            var galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE_FROM_GALLERY)

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun chooseVideoFromGallery() {
        if (allPermissionsGranted()) {
            var galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(galleryIntent, REQUEST_PICK_VIDEO_FROM_GALLERY)

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun populateMovieYearNTypeDropDown() {
        val movie_years = mutableListOf<Int>()
         for(i in 1900..2021){
            movie_years.add(i)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_movie_year, movie_years.reversed())
        (ti_movie_year.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        if (requestCode == REQUEST_PICK_VIDEO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val videoUri = data.data
                Timber.i("video_uri : $videoUri")
                videoUri?.let {
                    viewModel.setVideoUri(it)
                }

               /* CropImage.activity(contentURI!!)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    // .setAspectRatio(2, 1)
                    .setMinCropWindowSize(200, 200)
                    .start(requireContext(), this)*/
            }
        }
        if (requestCode == REQUEST_PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                Timber.i("video_uri : $imageUri")
                imageUri?.let {
                    viewModel.setImageUri(it)
                }

                 CropImage.activity(imageUri!!)
                     .setGuidelines(CropImageView.Guidelines.ON)
                     // .setAspectRatio(2, 1)
                     .setMinCropWindowSize(200, 200)
                     .start(requireContext(), this)
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                // viewModel.userImg.value = resultUri.toString()
                val thumb_filePath = File(resultUri.path!!)
                Glide.with(requireActivity())
                    .load(resultUri)
                    .into(binding.ivThumbnail)

                viewModel.setImageUri(resultUri)
                //  viewModel.handleEvent(UploadEvent.OnCompressMemePhoto(thumb_filePath))

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("result_uri", error.toString())
            }

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {

                // Permission is granted, do your work
                chooseVideoFromGallery()
                Timber.i("permission granted. Can open the gallery from here")
            }

        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        /* ContextCompat.checkSelfPermission(
             requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED*/

        ContextCompat.checkSelfPermission(
            requireActivity().baseContext, it
        ) == PackageManager.PERMISSION_GRANTED

    }

    companion object {
        private const val REQUEST_PICK_VIDEO_FROM_GALLERY: Int = 300
        private const val REQUEST_PICK_IMAGE_FROM_GALLERY: Int = 301
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


}