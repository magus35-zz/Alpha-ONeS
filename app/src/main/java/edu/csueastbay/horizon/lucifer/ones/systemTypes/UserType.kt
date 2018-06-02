package com.example.lindsey.onesmessaging.model

data class UserType(val name: String,
                    val age: String,
                    val bio: String,
                    val profilePicturePath: String?){
    constructor(): this( "","", "",null)

}