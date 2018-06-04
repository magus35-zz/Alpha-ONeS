package edu.csueastbay.horizon.lucifer.ones.Frags


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lindsey.onesmessaging.util.FirestoreFirebase
import com.example.lindsey.onesmessaging.util.BetaFirestore
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.csueastbay.horizon.lucifer.ones.Glide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.MatchHolder
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.SignInActivity
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class MyAccountFragment : Fragment() {



    private val RC_SELECT_IMAGE = 2
    //keep track of image
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    private var auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        view.apply {
            imageView_profile_picture.setOnClickListener {

                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/gif", "image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select ImageType"), RC_SELECT_IMAGE)
            }

            btn_save.setOnClickListener {

                if (::selectedImageBytes.isInitialized)
                    BetaFirestore.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        FirestoreFirebase.updateCurrentUser(editText_name.text.toString(), editText_age.text.toString(),
                                editText_bio.text.toString(),  imagePath)
                    }
                else
                    FirestoreFirebase.updateCurrentUser(editText_name.text.toString(),editText_age.text.toString(),
                            editText_bio.text.toString(),  null)
                toast("Saving")
            }


// signout user
            btn_sign_out.setOnClickListener {

                AuthUI.getInstance()
                        .signOut(this@MyAccountFragment.context!!)
                        .addOnCompleteListener {
                            //prompts to sign in again
                            startActivity(intentFor<SignInActivity>().newTask().clearTask())
                        }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                    .getBitmap(activity?.contentResolver, selectedImagePath)

            //converting picture
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            GlideApp.with(this)
            //image in memory
                    .load(selectedImageBytes)
                    .into(imageView_profile_picture)

            pictureJustChanged = true

            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()

        var facebookUser = false

        for (data in auth.currentUser!!.providerData) {
            if (data.providerId.equals("facebook.com")) {
                editText_name.setText(data.displayName)
                facebookUser = true
                break
            }
        }

        if (!facebookUser) {
            FirestoreFirebase.getCurrentUser { user ->
                if (this@MyAccountFragment.isVisible) {
                    editText_name.setText(user.name)
                    editText_bio.setText(user.bio)
                    editText_age.setText(user.age)

                    if (!pictureJustChanged && user.profilePicturePath != null)
                        GlideApp.with(this)
                                .load(BetaFirestore.pathToReference(user.profilePicturePath))
                                .placeholder(R.drawable.ic_account_circle_black_24dp)
                                .into(imageView_profile_picture)
                }
            }
        }

        matchingButton.setOnClickListener({
                    startActivity<MatchHolder>()
                }
        )
    }

    private fun launchMatchingActivity() {
        startActivity<MatchHolder>()
    }
}