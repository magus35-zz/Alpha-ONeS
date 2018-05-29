package com.example.lindsey.onesmessaging.util

import android.content.Context
import android.util.Log
import com.example.lindsey.onesmessaging.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.csueastbay.horizon.lucifer.ones.model.ChatPortal
import edu.csueastbay.horizon.lucifer.ones.model.MessageType
import edu.csueastbay.horizon.lucifer.ones.model.MessageTypeSent
import edu.csueastbay.horizon.lucifer.ones.model.TestType
import edu.csueastbay.horizon.lucifer.ones.recyclerview.item.PersonItem
import edu.csueastbay.horizon.lucifer.ones.recyclerview.item.TextItem

object FirestoreUtil{
        private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
        private val currentUserDocRef: DocumentReference

    get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
        ?: throw NullPointerException("UID is too small")}")

    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener{documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()

                }
            }
  else
                // if their doc already exists
        onComplete()
        }
     }
    fun updateCurrentUser(name:String = "", bio: String ="", profilePicturePath: String? = null) {
       //may need to update some part of the user
        val userFieldMap = mutableMapOf<String, Any>()
        if(name.isNotBlank()) userFieldMap["name"]= name
        if(bio.isNotBlank()) userFieldMap["bio"]= bio
        if(profilePicturePath != null)
            userFieldMap["profilePicturePath"]= profilePicturePath
        currentUserDocRef.update(userFieldMap)

    }
    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)

                }
    }
    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {

        return firestoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener

                    }

                    val items = mutableListOf<Item>()

                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }

    }
    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {

        // keeping docs on each users chat portals
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserId).get().addOnSuccessListener {
                    if (it.exists()) {
                        //if we area already chatting with user:
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }


                    // if the chat doesnt exit yet we get their id and create
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                    val newChannel = chatChannelsCollectionRef.document()
                    newChannel.set(ChatPortal(mutableListOf(currentUserId, otherUserId)))
                    // save the new chat
                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserId)
                            //id of the channel inside of firestore
                            .set(mapOf("channelId" to newChannel.id))


                    // getting the other user's document and saving the doc under other userID
                    firestoreInstance.collection("users").document(otherUserId)
                            .collection("engagedChatChannels")
                           // this is user
                            .document(currentUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)

                }

    }
    //listening for collection of messages
    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)

                        return@addSnapshotListener

                    }

                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(TextItem(it.toObject(TestType::class.java)!!, context))
                        else
                            //items.add(ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context))
                        return@forEach
                    }
                    onListen(items)
                }

    }
    fun sendMessage(messageTypeSent: MessageTypeSent, channelId: String){
        chatChannelsCollectionRef.document(channelId)
                .collection("messages")
                .add(messageTypeSent)
    }
    }
