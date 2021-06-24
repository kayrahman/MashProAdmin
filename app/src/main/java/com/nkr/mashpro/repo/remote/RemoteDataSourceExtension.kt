package com.nkr.bazaranocustomer.repo.remote

import android.util.Log
import com.google.android.gms.tasks.Task
import java.util.*
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

