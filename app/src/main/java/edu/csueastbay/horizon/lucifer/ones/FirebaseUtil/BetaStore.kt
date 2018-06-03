package com.example.lindsey.onesmessaging.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

object BetaStore{
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }



    private val currentUserRef: StorageReference
        get() = storageInstance.reference.child(FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null."))



    fun uploadImageforProfilePicture(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        //need to save picture to a new unique UsersName
        val ref = currentUserRef.child("profilePictures/${UUID.nameUUIDFromBytes(imageBytes)}")
        // the profile pictures are stored in profilePictures so they dont get mixed with regular imgaes
        ref.putBytes(imageBytes)
                .addOnSuccessListener {
                    onSuccess(ref.path)
                }

    }



// same as the function for upload profile picture images
    fun uploadImageforMessage(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
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


// the actual path for storing the image
    fun imagePathForReferences(path: String) = storageInstance.getReference(path)

}