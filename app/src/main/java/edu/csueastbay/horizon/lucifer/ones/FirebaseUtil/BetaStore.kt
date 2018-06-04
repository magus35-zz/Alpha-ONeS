package com.example.lindsey.onesmessaging.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


    object BetaStore
    {
    //initialize firestorage by lazy
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }


    // finding the user in Firstore beta database
    private val currentUserRef: StorageReference
    // need to call a getter to get the current user's docs
        get() = storageInstance.reference
                // get the uid of this instance
                .child(FirebaseAuth.getInstance().uid
                        // if for some reason we cant find it throw error
                        ?: throw NullPointerException("No UID"))

    // the actual path for storing the image
    fun pathToReference(path: String) = storageInstance.getReference(path)

    fun uploadProfilePhoto(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit)
    {
        //need to save picture to a new unique name
        val ref = currentUserRef.child("profilePictures/${UUID.nameUUIDFromBytes(imageBytes)}")

        // the profile pictures are stored in profilePictures so they dont get mixed with regular imgaes
        ref.putBytes(imageBytes)
                .addOnSuccessListener {
                    onSuccess(ref.path)
                }

    }

    // same as the function for upload profile picture images
    fun uploadMessageImage(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit)
    {

        // imagees will be stored in messages instead of storing in profilePictures
        val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")

        //reference for our imagebtes
        ref.putBytes(imageBytes)

                // if the image was succesfully uploaded
                .addOnSuccessListener {

                    // we are going to reference the path for storgage
                    onSuccess(ref.path)

                }

    }



    }