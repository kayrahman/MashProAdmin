package com.nkr.mashproadmin.ui.uploadRequest

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.repo.IRepoDataSource

class PendingUploadRequestViewModel(app:Application,val repo : IRepoDataSource) : BaseViewModel(app) {

}