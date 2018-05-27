package edu.csueastbay.horizon.lucifer.ones

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
    if(FirebaseAuth.getInstance().currentUser == null)
        startActivity<SignInActivity>()
    else
        startActivity<MainActivity>()
    finish()
    }
}
