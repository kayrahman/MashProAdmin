package com.nkr.mashproadmin.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.nkr.mashproadmin.R
import com.nkr.mashproadmin.base.BaseFragment
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.databinding.AuthenticationFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

private const val SIGN_IN_REQUEST_CODE = 707
class AuthenticationFragment : BaseFragment() {

    private val viewModel: AuthenticationViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding: AuthenticationFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = AuthenticationFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    viewModel.checkIfUserExistsInRemote()
                }

                else -> {
                    startSigninFlow()
                    Timber.i("auth_ac:user not authenticated")
                }
            }
        })

        viewModel.userValidated.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(R.id.action_authenticationFragment2_to_pendingUploadRequestFragment)
            }
        })



    }


    private fun startSigninFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(providers)
                .setTheme(R.style.AuthenticationTheme)
                .setTosAndPrivacyPolicyUrls(
                    "MashPro terms and service","MashPro privacy policy")
                .build(),
            SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in

                Timber.i("auth_ac : Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Timber.i( "auth_ac: unsuccessful ${response?.error?.errorCode}")
            }
        }
    }



}