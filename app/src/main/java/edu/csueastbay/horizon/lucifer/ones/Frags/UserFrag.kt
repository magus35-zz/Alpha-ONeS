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
import com.example.lindsey.onesmessaging.util.BetaStore
import com.example.lindsey.onesmessaging.util.FirebaseFirestore
import com.firebase.ui.auth.AuthUI
import edu.csueastbay.horizon.lucifer.ones.GlideFromGroupie.GlideApp
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.SignInActivity
import kotlinx.android.synthetic.main.user_frag_account_page.*
import kotlinx.android.synthetic.main.user_frag_account_page.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class UserFrag : Fragment() {



    private val RC_SELECT_IMAGE = 2
    //keep track of image
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_frag_account_page, container, false)



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
                    BetaStore.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        FirebaseFirestore.updateCurrentUser(editText_name.text.toString(), editText_age.text.toString(),
                                editText_bio.text.toString(),  imagePath)
                    }

                else

                    FirebaseFirestore.updateCurrentUser(editText_name.text.toString(),editText_age.text.toString(),
                            editText_bio.text.toString(),  null)
                toast("Saving")

            }


// signout user
            btn_sign_out.setOnClickListener {

                AuthUI.getInstance()
                        .signOut(this@UserFrag.context!!)
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
        FirebaseFirestore.getCurrentUser { user ->
            if (this@UserFrag.isVisible) {
                editText_name.setText(user.name)
                editText_bio.setText(user.bio)
                editText_age.setText(user.age)

              if (!pictureJustChanged && user.profilePicturePath != null)
                    GlideApp.with(this)
                            .load(BetaStore.pathToReference(user.profilePicturePath))
                            .placeholder(R.drawable.account_profile_default)
                            .into(imageView_profile_picture)


            }

        }

    }



}