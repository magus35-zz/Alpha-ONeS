package edu.csueastbay.horizon.lucifer.ones.systemTypes

// Message channel is a data class whose document will hold userIDs
// it will also hold all the properties of the messages , but Doc Ref doesn't
// need us to show that

data class MessageChannel(val userIds: MutableList<String>) {
    // Firestore requires us to have the empty constructor


    constructor() : this(mutableListOf()) // it says that it is never used, but will crash without it

}