package edu.csueastbay.horizon.lucifer.ones.MessageTypes

import java.util.*
// data class specifically for sending image messages
data class ImageType (
        // where the picture is coming from
        val picturePath: String,
        // time stamp
        override val time: Date,
       // senderID- gather from Firestore
        override val senderId: String,
        //overriding type to state that it is a image message
        override val type: String = MessageType.ImageMessageType)

    : MessageTypeSent {
//Firestore wants us to have at least an empty constructor
    constructor() : this("", Date(0), "")

}