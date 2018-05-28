package edu.csueastbay.horizon.lucifer.ones

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.lindsey.onesmessaging.util.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : AppCompatActivity() {
//firebase signin
    private val RC_SIGN_IN = 1
    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        account_sign_in.setOnClickListener{
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(signInProviders)
                    .setLogo(R.drawable.oneslogo)
                    .build()
            startActivityForResult(intent,RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK){
                val progressDialog = indeterminateProgressDialog("Setting up your account")
            // initialize user in firestore
                FirestoreUtil.initCurrentUserIfFirstTime {
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
}
