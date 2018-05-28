package edu.csueastbay.horizon.lucifer.ones.model
import java.util.*


    data class Text(val text: String,
                           override val time: Date,
                           override val senderId: String,
                    //seetting messagetype to text
                           override val type: String = MessageType.TEXT)

        : MessageTypeSent {

        constructor() : this("", Date(0), "")

    }

