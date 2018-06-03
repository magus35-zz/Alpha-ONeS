package com.example.lindsey.onesmessaging.MessageTypes
// setting up format for users including:
    // -UsersName, -age - bio - ppp

data class UserType(
        // User's first and last UsersName- will be shown on profile and RVlist
        val name: String,
        // Users age will only be shown on RVlist and user's profile
        //val age: String,
        // User's bio will be shown on RVList, and on user's profile
        val bio: String,
        // Gets the path to the profile picture the user choose
        val profilePicturePath: String?){

   // Again, Firestore wants us to have this empty constructor to run
    constructor(): this( "","", null)

}
// will crash without the constructor - ignore error