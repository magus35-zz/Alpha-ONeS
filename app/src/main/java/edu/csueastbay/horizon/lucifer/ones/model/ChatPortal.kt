package edu.csueastbay.horizon.lucifer.ones.model

data class ChatPortal(val userIds: MutableList<String>) {

    constructor() : this(mutableListOf())

}