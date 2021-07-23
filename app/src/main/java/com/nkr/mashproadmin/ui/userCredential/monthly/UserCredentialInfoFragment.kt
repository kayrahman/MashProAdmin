package com.nkr.mashproadmin.ui.userCredential.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nkr.mashproadmin.base.BaseFragment
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.databinding.FragmentUserCredentialInfoBinding
import com.nkr.mashproadmin.ui.userCredential.UserSubscriptionPlanViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserCredentialInfoFragment : BaseFragment() {

    private val viewModel : UserSubscriptionPlanViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : FragmentUserCredentialInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentUserCredentialInfoBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPayment.setOnClickListener {


        }
    }
}