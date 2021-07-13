package com.nkr.mashpro.ui.account

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.bazaranocustomer.util.GridSpacingItemDecoration
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.FragmentDashboardBinding
import com.nkr.mashpro.ui.home.HomeEvent
import com.nkr.mashpro.ui.home.MovieListAdapter
import com.nkr.mashpro.ui.userCredential.UserEvent
import com.nkr.mashpro.ui.userCredential.UserSubscriptionPlanViewModel
import com.nkr.mashpro.util.USER_TYPE_CREATOR
import com.nkr.mashpro.util.USER_TYPE_VIEWER
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment() {

    private val dashboardViewModel: UserSubscriptionPlanViewModel by inject()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        setupListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = dashboardViewModel
        binding.lifecycleOwner = this

        dashboardViewModel.handleEvent(UserEvent.OnFetchMovies)
        dashboardViewModel.handleEvent(UserEvent.OnFetchUserInfo)

        binding.ivUser.setOnClickListener {
            choosePhotoFromGallery()
        }

        setupAdapter()

    }

    private fun setupAdapter() {
        val adapter = MovieListAdapter()
        binding.rvMovies.adapter = adapter
        val spacing = 10 // 50px
        val includeEdge = false
        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(3, spacing, includeEdge))
        dashboardViewModel.movieList.observe(viewLifecycleOwner, Observer {
            Timber.i("flow_prod_list:${it.size} ")
            //populate the adapter here
            adapter.submitList(it)
        })


        adapter.listener = MovieListAdapter.MovieItemClickListener {
               val actionMoviePlayer = AccountFragmentDirections.actionNavigationDashboardToMoviePlayerFragment(it)
                  dashboardViewModel.navigationCommand.value = NavigationCommand.To(actionMoviePlayer)

        }

    }

    private fun setupListener() {
        binding.btnLogout.setOnClickListener {
            dashboardViewModel.setupUserType(USER_TYPE_CREATOR)
          /*  AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    Toast.makeText(requireContext(),"Singed out",Toast.LENGTH_SHORT).show()

                    val actionToAuthFm = AccountFragmentDirections.actionNavigationDashboardToAuthenticationFragment()
                    findNavController().navigate(actionToAuthFm)

                }*/
        }

    }


    private fun choosePhotoFromGallery() {
        if (allPermissionsGranted()) {
            var galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, REQUEST_PICK_FROM_GALLERY)

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override val _viewModel: BaseViewModel
        get() = dashboardViewModel


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        if (requestCode == REQUEST_PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentURI = data.data
                CropImage.activity(contentURI!!)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    // .setAspectRatio(2, 1)
                    .setMinCropWindowSize(200, 200)
                    .start(requireContext(),this)
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                // viewModel.userImg.value = resultUri.toString()
                val thumb_filePath = File(resultUri.path!!)

                /*     Glide.with(requireActivity())
                         .load(resultUri)
                         .into(binding.ivUser)
     */
                dashboardViewModel.handleEvent(UserEvent.OnUpdateUserImage(resultUri))

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("result_uri",error.toString())
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (permissions[0]  == Manifest.permission.READ_EXTERNAL_STORAGE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, do your work
                choosePhotoFromGallery()
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
        private const val REQUEST_PICK_FROM_GALLERY: Int = 300
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }



}