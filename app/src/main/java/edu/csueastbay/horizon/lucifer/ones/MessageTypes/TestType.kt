package edu.csueastbay.horizon.lucifer.ones.MessageTypes
import java.util.*

// these will all be overrode when the types call it

    data class TestType(
            // these will all be overrode when the types call it

            val text: String,
            // do need to include our date

            override val time: Date,
            //senderID also

            override val senderId: String,
            //setting messagetype to text
                        override val type: String = MessageType.TEXT)

        : MessageTypeSent {
// again Firestore wants us to have this constructor here

        constructor() : this("", Date(0), "")
// will crash without constructor - ignore warning
    }

