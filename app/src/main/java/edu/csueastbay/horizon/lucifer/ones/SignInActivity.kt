package edu.csueastbay.horizon.lucifer.ones

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.lindsey.onesmessaging.util.FirestoreFirebase
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.design.longSnackbar
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.*


class SignInActivity : AppCompatActivity() {
//firebase signin
    private val RC_SIGN_IN = 1
    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

    //for facebook login
    private var auth:FirebaseAuth = FirebaseAuth.getInstance()
    private var callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        imageView.setBackgroundColor(Color.BLACK)

        account_sign_in.setOnClickListener{
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(signInProviders)
                    .setLogo(R.drawable.splashback_small)
                    .build()
            startActivityForResult(intent,RC_SIGN_IN)
        }

        //handle Facebook Login
        login_button.setReadPermissions("email", "public_profile")
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }
                override fun onCancel() {
                    toast("Facebook Login Cancelled")
                }
                override fun onError(exception: FacebookException) {
                    toast(exception.localizedMessage)
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data) //for facebook login

        //handle email signin
        if (requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK){
                val progressDialog = indeterminateProgressDialog("Setting up your account")
            // initialize user in firestore
                FirestoreFirebase.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                progressDialog.dismiss()
                }
            }
           else if(resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        longSnackbar(constraint_layout, "No network found")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longSnackbar(constraint_layout, "Sorry, Unknown Error")
                }
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
        startActivity<MainActivity>()
    }
}
