package edu.csueastbay.horizon.lucifer.ones.MessageTypes

import java.util.*
// Decides which type of message to send when called
    object MessageType {
        // used in ImageType and TextType
    //overrides type to be a text message
        const val TextMessageType = "TextMessageType"
    //overrides type to be set to image message
        const val ImageMessageType = "ImageMessageType"
                        }
// sets up the values to be sent for all messages
        interface MessageTypeSent {
    // these will all be overrode when the types call it
            val time: Date
            val senderId: String
            val type: String
    // picturepath and text are type specific so we can exclude those

}