package com.nkr.mashpro

import android.app.Application
import android.content.Context
import com.nkr.mashpro.repo.IRepoDataSource
import com.nkr.mashpro.repo.MashProRepository
import com.nkr.mashpro.repo.local.ILocalDataSource
import com.nkr.mashpro.repo.local.LocalDataSourceImpl
import com.nkr.mashpro.repo.local.MashProLocalDatabase
import com.nkr.mashpro.repo.remote.IRemoteDataSource
import com.nkr.mashpro.repo.remote.RemoteDataSourceImpl
import com.nkr.mashpro.ui.account.AccountViewModel
import com.nkr.mashpro.ui.authentication.AuthenticationViewModel
import com.nkr.mashpro.ui.downloads.DownloadsViewModel
import com.nkr.mashpro.ui.home.HomeViewModel
import com.nkr.mashpro.ui.moviePlayer.MoviePlayerViewModel
import com.nkr.mashpro.ui.search.SearchViewModel
import com.nkr.mashpro.ui.upload.UploadViewModel
import com.nkr.mashpro.ui.userCredential.UserSubscriptionPlanViewModel
import com.nkr.mashpro.util.SharedPrefsHelper
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