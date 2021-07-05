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
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserSubscriptionPlanFragment : BaseFragment(), SSLCTransactionResponseListener {

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
            val sslCommerzInitialization = SSLCommerzInitialization(
                "mashp60dfe95d7583b","mashp60dfe95d7583b@ssl",
                100.0, SSLCCurrencyType.BDT,"123456789",
                "paid_movie", SSLCSdkType.TESTBOX
            )

            IntegrateSSLCommerz
                .getInstance(context)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .buildApiCall(this);


        }

         //
        }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        viewModel.updateSubscriptionPlan(USER_SUBSCRIPTION_TYPE_MONTHLY)
        viewModel.showSnackBar.value = "Transaction Successful"
    }

    override fun transactionFail(p0: String?) {
        viewModel.showSnackBar.value = "Transaction Unsuccessful"
    }

    override fun merchantValidationError(p0: String?) {
        viewModel.showSnackBar.value = "Merchant validation error"
    }


}