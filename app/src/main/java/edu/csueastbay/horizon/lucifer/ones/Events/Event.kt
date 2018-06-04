package edu.csueastbay.horizon.lucifer.ones.Events

class Event ( val id: String, val name: String, val date: String, val location: String, val description: String){
    constructor() : this("","","","", "")
}