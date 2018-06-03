package edu.csueastbay.horizon.lucifer.ones.MessageTypes
import java.util.*
// overrides MessageTypeSent to specifu

    data class TextType(
            // don't need to override since imagetype doesnt have text
            val text: String,
            // do need to include our date
            override val time: Date,
            //senderID also
            override val senderId: String,
            //setting messagetype to text
            override val type: String = MessageType.TextMessageType)

        : MessageTypeSent {
// again Firestore wants us to have this constructor here
        constructor() : this("", Date(0), "")

    }
// will crash without constructor - not error

