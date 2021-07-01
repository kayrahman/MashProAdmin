package com.nkr.mashpro.ui.userCredential

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nkr.mashpro.base.BaseFragment
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.databinding.UserTypeFragmentBinding
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_FREE
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_MONTHLY
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserSubscriptionPlanFragment : BaseFragment() {

    private val viewModel: UserSubscriptionPlanViewModel by viewModel()
    private lateinit var binding: UserTypeFragmentBinding
    override val _viewModel: BaseViewModel
        get() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserTypeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnFreePlan.setOnClickListener {
            viewModel.updateSubscriptionPlan(USER_SUBSCRIPTION_TYPE_FREE)
        }
        binding.btnMonthlyPlan.setOnClickListener {
            viewModel.updateSubscriptionPlan(USER_SUBSCRIPTION_TYPE_MONTHLY)
        }

    }

}