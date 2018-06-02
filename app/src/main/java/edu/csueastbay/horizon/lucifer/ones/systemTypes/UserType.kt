package com.example.lindsey.onesmessaging.model

import java.security.cert.CertPath

data class UserType(val name: String,
                    val bio: String,
                    val profilePicturePath: String?){
    constructor(): this( "","",null)

}