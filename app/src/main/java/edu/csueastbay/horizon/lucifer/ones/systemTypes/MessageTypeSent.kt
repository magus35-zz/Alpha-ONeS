package edu.csueastbay.horizon.lucifer.ones.systemTypes

// Decides which type of message to send when called

import java.util.*
// used in ImageType and TextType
//overrides type to be a text message

// used in ImageType and TextType
    object MessageType {
    //overrides type to be a text message
    const val TEXT = "TEXT"
    // used in ImageType and TextType
    const val IMAGE = "IMAGE"

}   //used in ImageType and TextType

interface MessageTypeSent
{
    // these will all be overrode when the types call it
    val time: Date
    val senderId: String
    val type: String
    // these will all be overrode when the types call it

}