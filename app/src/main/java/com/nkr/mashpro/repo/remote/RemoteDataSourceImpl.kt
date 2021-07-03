package com.nkr.mashpro.repo.remote

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nkr.bazarano.service.MyFirebaseMessagingService
import com.nkr.bazaranocustomer.repo.remote.awaitTaskCompletable
import com.nkr.bazaranocustomer.repo.remote.awaitTaskResult
import com.nkr.bazaranocustomer.repo.remote.awaitTaskResultForVideoUri
import com.nkr.bazaranocustomer.repo.remote.toMovie
import com.nkr.bazaranocustomer.util.StorageUtil
import com.nkr.mashpro.model.FirebaseMovie
import com.nkr.mashpro.model.FirebaseUserInfo
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.model.MovieLocationInfo
import com.nkr.mashpro.repo.remote.IRemoteDataSource
import timber.log.Timber
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.util.COLLECTION_MOVIES
import com.nkr.mashpro.util.COLLECTION_USERS
import com.nkr.mashpro.util.NODE_MOVIE_TITLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RemoteDataSourceImpl(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IRemoteDataSource {

    private fun getActiveUser(): String {
        return auth.currentUser?.uid.toString()
    }

    override suspend fun getRegistrationToken(): Result<ArrayList<String>> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .get()
            )

            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                Result.Success(user?.registration_tokens!!)

            } else {
                throw Exception("user not found")
            }
        } catch (e: Exception) {
            // false
            Result.Error(e)
        }

    }

    override suspend fun setFCMRegistrationToken(tokens: ArrayList<String>): Result<Unit> {
        val user_map = HashMap<String, Any>()
        user_map["registration_tokens"] = tokens

        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .update(user_map)
            )

            Result.Success(Unit)

        } catch (e: Exception) {
            // false
            Result.Error(e)
        }
    }

    //----------------USER AUTHENTICATION ACTIVITY--------------/
    override suspend fun setupUserInRemote(): Result<Unit> {

        Timber.i("user_first_time:settting up user")
        val user_uid = getActiveUser()
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS).document(user_uid).get()
            )
            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                // fetch the user data and insert into local db
                Timber.i("user_first_time:false")
                if (user != null) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        MyFirebaseMessagingService.addTokenToFirestore(it)
                    }

                    Result.Success(Unit)
                } else {
                    Timber.i("user_first_time:not found")
                    throw Exception("firebase user not found")
                }
            } else {
                Timber.i("user_first_time:true")
                val user = insertUserIntoRemote(user_uid)
                when (user) {
                    is Result.Success -> {
                        //update registrationTokens
                        FirebaseMessaging.getInstance().token.addOnSuccessListener {
                            MyFirebaseMessagingService.addTokenToFirestore(it)
                        }

                        Result.Success(Unit)
                    }
                    is Result.Error -> {
                        throw user.exception
                    }
                }

                //fetch the latest 30 products and categories from remote, save into local db

            }
        } catch (e: Exception) {
            // false
            throw e
        }

    }

    override suspend fun updateUserSubscriptionPlan(sub_plan: String): Result<Unit> {
        val map = HashMap<String, Any>()
        map["subscription_plan"] = sub_plan

        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .update(map)
            )
            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun getUserInfo(): Result<FirebaseUserInfo> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS).document(getActiveUser()).get()
            )

            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                Result.Success(user!!)
            } else {
                throw Exception("no user found")
            }

        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    suspend fun insertUserIntoRemote(user_uid: String): Result<FirebaseUserInfo> {
        val firebaseUser = auth.currentUser
        return try {
            val user = FirebaseUserInfo(
                firebaseUser?.uid.toString(),
                firebaseUser?.displayName.toString(),
                email = firebaseUser?.email.toString(),
                img_url = firebaseUser?.photoUrl.toString()
            )
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(user_uid)
                    .set(
                        user
                    )
            )

            Result.Success(user)

        } catch (e: Exception) {
            throw e
        }

    }


    //-----------------------storage-----------------------//
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val storageReference: StorageReference
        get() = storageInstance.reference

    override suspend fun uploadVideoInfo(uri: Uri): Result<MovieLocationInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val user_uid = getActiveUser()
                val unique_file_name = user_uid + System.currentTimeMillis()
                val ref = storageReference.child("video_movie/$unique_file_name")
                val upload_task = awaitTaskResult(ref.putFile(uri))
                val uri_task = awaitTaskResultForVideoUri(ref.downloadUrl)
                Timber.i("upload_uri ${ref.path}")
                val movie_loc =
                    MovieLocationInfo(video_url = uri_task.toString(), video_ref = ref.path)
                Result.Success(movie_loc)
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
    }

    override suspend fun uploadMovieThumbImage(uri: Uri): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val user_uid = getActiveUser()
                val unique_file_name = user_uid + System.currentTimeMillis()
                val ref = storageReference.child("movie_thumb_image/$unique_file_name")
                val upload_task = awaitTaskResult(ref.putFile(uri))
                Result.Success(ref.path)
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
    }

    override suspend fun uploadMovieInfo(movie: FirebaseMovie): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                awaitTaskCompletable(
                    remote.collection(COLLECTION_MOVIES)
                        .add(movie)
                )
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun fetchMovies(): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val task = awaitTaskResult(
                    remote.collection(COLLECTION_MOVIES)
                        .get()
                )

                getMoviesFromTask(task)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun downloadMovieToLocalFile(movie: String): Result<String> {
        //  Timber.i("downloadUri : ${downloadUrl.toString()}")
        val localFile = File.createTempFile("MashupPro", "mp4")
        return withContext(Dispatchers.IO) {
            try {
                //  val demo_video_ref = storageReference.child("video_movie/7REe2OGCsgeyT7MBsycAQVMTUIi21624513261237.mp4")
                val demo_video_ref =
                    StorageUtil.pathToReference("/video_movie/UqG0bPSdCNU0jqwzNsFbNWN0Ebk21624700078870")

                awaitTaskCompletable(demo_video_ref.getFile(localFile))
                val uri = Uri.fromFile(localFile)

                /* demo_video_ref.getFile(localFile).addOnSuccessListener {
                     val uri = Uri.fromFile(localFile)
                     //insert movie info into local url
                     Timber.i("video_download_uri : ${uri.toString()}")
                 }.addOnFailureListener {
                     Timber.i("video_download_uri : ${it.toString()}")
                 }*/

                Result.Success(uri.toString())

            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun fetchMoviesBySearch(queryString: String): Result<List<Movie>> {
        return try {
            val task_query_title = awaitTaskResult(
                remote.collection(COLLECTION_MOVIES)
                    .whereGreaterThanOrEqualTo(NODE_MOVIE_TITLE, queryString.toLowerCase())
                    .whereLessThan(NODE_MOVIE_TITLE, queryString + 'z')
                    .get()
            )

          getMoviesFromTask(task_query_title)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun getMoviesFromTask(task: QuerySnapshot?): Result<List<Movie>> {
        val movies = mutableListOf<Movie>()
        task?.documents?.forEach {
            val movie = it.toObject(FirebaseMovie::class.java)?.toMovie
            movie?.uid = it.id
            movies.add(movie!!)
        }

        return Result.Success(movies)
    }


}
