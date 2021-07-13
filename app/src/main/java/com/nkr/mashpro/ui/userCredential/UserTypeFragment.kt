package com.nkr.mashpro.ui.userCredential

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.FragmentUserTypeBinding
import com.nkr.mashpro.util.SharedPrefsHelper
import com.nkr.mashpro.util.USER_TYPE
import com.nkr.mashpro.util.USER_TYPE_CREATOR
import com.nkr.mashpro.util.USER_TYPE_VIEWER
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserTypeFragment : BaseFragment() {

    private val viewModel : UserSubscriptionPlanViewModel by inject()
    private lateinit var binding : FragmentUserTypeBinding


    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var sharedPref : SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPrefsHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserTypeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTypeCreator.setOnClickListener {
          //  viewModel.setupUserType(USER_TYPE_CREATOR)
            sharedPref.put(USER_TYPE,USER_TYPE_CREATOR)
            viewModel.handleEvent(UserEvent.OnUpdateUserType(USER_TYPE_CREATOR))
        }


        binding.btnTypeViewer.setOnClickListener {
        //    viewModel.setupUserType(USER_TYPE_VIEWER)
            sharedPref.put(USER_TYPE,USER_TYPE_VIEWER)
            viewModel.handleEvent(UserEvent.OnUpdateUserType(USER_TYPE_VIEWER))
        }


        viewModel.isUserTypeUpdated.observe(viewLifecycleOwner, Observer {
            if(it){
              //  viewModel.userType.value?.let { it1 -> viewModel.setupUserType(it1) }
            }
        })

        
    }





}