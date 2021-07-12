package com.nkr.mashpro.ui.userCredential

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nkr.mashpro.R
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.FragmentUserTypeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserTypeFragment : BaseFragment() {

    private val viewModel : UserSubscriptionPlanViewModel by viewModel()
    private lateinit var binding : FragmentUserTypeBinding

    override val _viewModel: BaseViewModel
        get() = viewModel


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

        }


        binding.btnTypeViewer.setOnClickListener {

        }
    }

}