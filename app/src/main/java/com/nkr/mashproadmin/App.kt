package com.nkr.mashproadmin

import android.app.Application
import com.nkr.mashproadmin.repo.IRepoDataSource
import com.nkr.mashproadmin.repo.MashProRepository
import com.nkr.mashproadmin.repo.local.ILocalDataSource
import com.nkr.mashproadmin.repo.local.LocalDataSourceImpl
import com.nkr.mashproadmin.repo.local.MashProLocalDatabase
import com.nkr.mashproadmin.repo.remote.IRemoteDataSource
import com.nkr.mashproadmin.repo.remote.RemoteDataSourceImpl
import com.nkr.mashproadmin.ui.account.AccountViewModel
import com.nkr.mashproadmin.ui.authentication.AuthenticationViewModel
import com.nkr.mashproadmin.ui.downloads.DownloadsViewModel
import com.nkr.mashproadmin.ui.home.HomeViewModel
import com.nkr.mashproadmin.ui.moviePlayer.MoviePlayerViewModel
import com.nkr.mashproadmin.ui.search.SearchViewModel
import com.nkr.mashproadmin.ui.upload.UploadViewModel
import com.nkr.mashproadmin.ui.uploadRequest.PendingUploadRequestFragment
import com.nkr.mashproadmin.ui.uploadRequest.PendingUploadRequestViewModel
import com.nkr.mashproadmin.ui.userCredential.UserSubscriptionPlanViewModel
import com.nkr.mashproadmin.util.SharedPrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

     lateinit var sharedPref : SharedPrefsHelper

    override fun onCreate() {
        super.onCreate()

        sharedPref = SharedPrefsHelper(this)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()

            viewModel {
                HomeViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                AuthenticationViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                UploadViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                MoviePlayerViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                DownloadsViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            single {
                UserSubscriptionPlanViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                SearchViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                AccountViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            viewModel {
                PendingUploadRequestViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }




            //  single { MyFirebaseMessagingService(get() as IRepoDataSource) }
           // single { SharedPrefsHelper(this@App) }
            single { MashProRepository(get() as IRemoteDataSource,get() as ILocalDataSource) as IRepoDataSource }
            single { RemoteDataSourceImpl() as IRemoteDataSource }
            single { LocalDataSourceImpl(get()) as ILocalDataSource }
            single { MashProLocalDatabase.getInstance(this@App).movieDao }
            // single { MashProRepository(get())}
        }

        startKoin {
            androidContext(this@App)
            modules(listOf(myModule))
        }


    }



    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"

    }

}