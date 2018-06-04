package edu.csueastbay.horizon.lucifer.ones.systemTypes

import java.util.*

// data class specifically for sending image messages
data class ImageType (
                    // where the picture is coming from
                    val imagePath: String,
                    // time stamp
                    override val time: Date,
                     // senderID- gather from Firestore
                    override val senderId: String,
                    //seetting messagetype to text
                    override val type: String = MessageType.IMAGE)

    : MessageTypeSent {

    //Firestore wants us to have at least an empty constructor
    constructor() : this("", Date(0), "")

}