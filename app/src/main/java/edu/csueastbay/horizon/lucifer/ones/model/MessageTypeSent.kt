package edu.csueastbay.horizon.lucifer.ones.model




import java.util.*





    object MessageType {
        const val TEXT = "TEXT"
     const val IMAGE = "IMAGE"

}

        interface MessageTypeSent {

        val time: Date
        val senderId: String
         val type: String

}