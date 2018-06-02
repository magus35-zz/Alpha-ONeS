package edu.csueastbay.horizon.lucifer.ones.systemTypes

import java.util.*

data class ImageType (val imagePath: String,
                    override val time: Date,
                    override val senderId: String,
        //seetting messagetype to text
                    override val type: String = MessageType.IMAGE)

    : MessageTypeSent {

    constructor() : this("", Date(0), "")

}