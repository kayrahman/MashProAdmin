package com.nkr.bazaranocustomer.util

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


object StorageUtil {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val storageReference: StorageReference
        get() = storageInstance.reference

    fun pathToReference(path: String) = storageInstance.getReference(path)

}