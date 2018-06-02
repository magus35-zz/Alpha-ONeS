package edu.csueastbay.horizon.lucifer.ones.systemTypes
import java.util.*


    data class TestType(val text: String,
                        override val time: Date,
                        override val senderId: String,
                    //seetting messagetype to text
                        override val type: String = MessageType.TEXT)

        : MessageTypeSent {

        constructor() : this("", Date(0), "")

    }

