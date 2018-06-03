package com.example.lindsey.onesmessaging.util

import android.content.Context
import android.util.Log
import com.example.lindsey.onesmessaging.MessageTypes.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.*
import edu.csueastbay.horizon.lucifer.ones.RCV.item.ImageItem
import edu.csueastbay.horizon.lucifer.ones.RCV.item.TextItem
import edu.csueastbay.horizon.lucifer.ones.RCV.item.UserModelItem

object FirebaseFirestore{
        private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
        private val currentUserDocRef: DocumentReference

    get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
        ?: throw NullPointerException("Awk You dont have any UID")}")

    // this is located in firestore and all the indiviudal chat channels / messages are stored
    private val messageChannelsCollectionRef = firestoreInstance.collection("messageChannels")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener{documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser = UserType(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
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
        if(name.isNotBlank()) userFieldMap["UsersName"]= name
        if(bio.isNotBlank()) userFieldMap["bio"]= bio
        if(profilePicturePath != null)
            userFieldMap["profilePicturePath"]= profilePicturePath
        currentUserDocRef.update(userFieldMap)

    }
    fun getCurrentUser(onComplete: (UserType) -> Unit){

        // almost like using a get method, but we are using a listener method instead
        // on User Doc Ref Firebase's way of storing documents in the database
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(UserType::class.java)!!)

                }
    }
    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {

        // need to return to .collections to update the Doc Ref of the list of users
        return firestoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener

                    }
                    // on
                    val items = mutableListOf<Item>()
                    // querySnapshot allows the current user to be in the list of users so that other users
                    // can see them in the list.
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(UserModelItem(it.toObject(UserType::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }

    }
    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {

        // keeping docs on each users chat portals
        currentUserDocRef.collection("engagedChatChannels")
               //other userid is the person we are talking to
                .document(otherUserId).get().addOnSuccessListener {
                   // if we are already chatting with the user
                    if (it.exists()) {
                        //get the snapshot
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }

                    // if the chat doesnt exit yet we get their id and create
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    // create out new channel's doc reference
                    val newChannel = messageChannelsCollectionRef.document()
                    // to create the actual new channel
                    newChannel.set(MessageChannel(mutableListOf(currentUserId, otherUserId)))
                    // save the new chat in the current user document
                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserId)
                            //id of the channel inside of firestore
                            .set(mapOf("channelId" to newChannel.id))


                    // getting the other user's document and saving the doc under other userID
                    firestoreInstance.collection("users").document(otherUserId)
                           //saving the document under the userid
                            .collection("engagedChatChannels")
                           //but this is really the main user
                            .document(currentUserId)
                            // set the map to the newchannel.id ( the user) in firebase
                            .set(mapOf("channelId" to newChannel.id))
                    // now were passing in the new channel id
                    onComplete(newChannel.id)
                }

    }
    //listening for all/ any of the messages inside a channel so we need to find the id
    fun addMessagesChannelListener(channelId: String, context: Context,
                                // items are still from the groupie library and it will return a unit
                                   onListen: (List<Item>) -> Unit): ListenerRegistration {
        // we are retuning the messagess that have the correct channelID
        return messageChannelsCollectionRef.document(channelId).collection("messages")
               // make sure the order is by times so they arn't out of order
                .orderBy("time")
                //Add the query snapshot and exception incase we hae an error
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        // if we do have an error from firestore through it
                        Log.e("FIRESTORE", "An error has occured in addmessagechannellistener", firebaseFirestoreException)
                        // now restore from the listener
                        return@addSnapshotListener

                    }
                    // if no error we want to create a list of all the items
                    val items = mutableListOf<Item>()
                    // for each document we want to make sure we are getting the correct type of item
                    querySnapshot!!.documents.forEach {

                        // if we are getting an text message type of message we want to add text message item to all the iteams
                        if (it["type"] == MessageType.TextMessageType)
                            // we can convert the textmessage snapshot to a text message document to store in firestore
                            items.add(TextItem(it.toObject(TextType::class.java)!!, context))
                        else
                            // if we have an image instead, no other type, then  we convert to an image doc
                            items.add(ImageItem(it.toObject(ImageType::class.java)!!, context))
                        //return each of the possible documents
                        return@forEach
                    }
                    // Calling the function onListen for all the the items
                    onListen(items)
                }

    }
    // actually sending the messages- checks the message type and channelid
    fun deliverMessageToPartner(messageTypeSent: MessageTypeSent, channelId: String){
        //we need to get the document of where we need to deliver the message to
        messageChannelsCollectionRef.document(channelId)
                // we want to get all the other messages from this documetn
                .collection("messages")
                // add the message to the Doc Ref
                .add(messageTypeSent)
    }
    }
