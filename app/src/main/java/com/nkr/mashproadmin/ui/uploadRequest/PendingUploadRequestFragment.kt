package com.nkr.mashproadmin.ui.uploadRequest

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nkr.mashproadmin.R
import com.nkr.mashproadmin.base.BaseFragment
import com.nkr.mashproadmin.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PendingUploadRequestFragment : BaseFragment() {
    private val viewModel: PendingUploadRequestViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pending_upload_request_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }

}