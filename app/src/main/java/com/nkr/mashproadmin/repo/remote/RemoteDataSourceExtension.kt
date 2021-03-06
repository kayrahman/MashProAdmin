package com.nkr.bazaranocustomer.repo.remote

import android.util.Log
import com.google.android.gms.tasks.Task
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashproadmin.model.FirebaseMovieInfo
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.local.model.MovieDTO
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val tasky = task.result
            continuation.resume(task.result!!)

            Log.d("task", task.result.toString())

        } else {
            continuation.resumeWithException(task.exception!!)
            Log.d("task", task.exception.toString())
        }
    }
}

//Wraps Firebase/GMS calls
internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit =
    suspendCoroutine { continuation ->
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Unit)
                Log.d("task_prod", task.result.toString())
            } else {
                continuation.resumeWithException(task.exception!!)
                Log.d("task_prod", task.result.toString())
            }
        }
    }


internal suspend fun <T> awaitTaskResultForVideoUri(task: Task<T>): T =
    suspendCoroutine { continuation ->
        task.addOnSuccessListener { task ->
            Timber.i("video_uri : $task")
            continuation.resume(task)
        }
    }


val FirebaseMovieInfo.toMovie: Movie
    get() = Movie(
        uid = "",
        video_url = this.video_url,
        video_ref = this.video_ref,
        img_url = this.img_url,
        movie_title = this.movie_title,
        movie_year = this.movie_year,
        description = this.description,
        type = this.type
    )


val Movie.toMovieDTO : MovieDTO
get() = MovieDTO(
    uid = this.uid,
    video_url = this.video_url,
    video_ref = this.video_ref.toString(),
    img_url = this.img_url,
    movie_title = this.movie_title,
    movie_year = this.movie_year,
    description = this.description,
    type = this.type,
    download_uri = ""
)

val List<MovieDTO>.toMovies: List<Movie>
    get() = this.map {
        Movie(
            uid = it.uid,
            description = it.description,
            video_url = it.video_url,
            video_ref = it.video_ref,
            img_url = it.img_url,
            movie_title = it.movie_title,
            movie_year = it.movie_year,
            type = it.type
        )
    }



internal fun List<RoomSearchedWord>.toSearchWordListFromRoom(): List<SearchedWord> = this.flatMap {
    listOf(it.toSearchWord)
}


internal val RoomSearchedWord.toSearchWord: SearchedWord
    get() = SearchedWord(this.searched_word, null)

