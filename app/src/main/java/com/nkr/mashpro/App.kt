package com.nkr.mashpro

import android.app.Application
import com.nkr.bazaranocustomer.repo.remote.RemoteDataSourceImpl
import com.nkr.mashpro.repo.IRepoDataSource
import com.nkr.mashpro.repo.MashProRepository
import com.nkr.mashpro.repo.remote.IRemoteDataSource
import com.nkr.mashpro.ui.authentication.AuthenticationViewModel
import com.nkr.mashpro.ui.home.HomeViewModel
import com.nkr.mashpro.ui.moviePlayer.MoviePlayerViewModel
import com.nkr.mashpro.ui.upload.UploadViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
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


            //  single { MyFirebaseMessagingService(get() as IRepoDataSource) }
           // single { SharedPrefsHelper(this@App) }
            single { MashProRepository(get() as IRemoteDataSource) as IRepoDataSource }
            single { RemoteDataSourceImpl() as IRemoteDataSource }
           // single { LocalDataSourceImpl(get()) as ILocalDataSource }
          //  single { BazarAnoLocalDatabase.getInstance(this@App).productDao }
            // single { RemindersLocalRepository(get())}
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