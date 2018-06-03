package edu.csueastbay.horizon.lucifer.ones.Frag


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
import edu.csueastbay.horizon.lucifer.ones.AppGlide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.SignInActivity
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import kotlinx.android.synthetic.main.item_person.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class MyFrag : Fragment() {



    private val RC_SELECT_IMAGE = 2
    //keep track of image
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)



        view.apply {

            imageView_pp.setOnClickListener {

                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/gif", "image/jpeg", "image/png"))

                }

                startActivityForResult(Intent.createChooser(intent, "Select ImageType"), RC_SELECT_IMAGE)

            }



            btn_save.setOnClickListener {

                if (::selectedImageBytes.isInitialized)
                    BetaStore.uploadImageforProfilePicture(selectedImageBytes) { imagePath ->
                        FirebaseFirestore.updateCurrentUser(changeEntry_name.text.toString(),
                                changeEntry_bio.text.toString(),  imagePath)
                    }

                else

                    FirebaseFirestore.updateCurrentUser(changeEntry_name.text.toString(),
                            changeEntry_bio.text.toString(), null)
                toast("Saving")

            }


// signout user
            btn_sign_out.setOnClickListener {

                AuthUI.getInstance()
                        .signOut(this@MyFrag.context!!)
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
                    .into(imageView_pp)

            pictureJustChanged = true

            pictureJustChanged = true

        }

    }



    override fun onStart() {
        super.onStart()
        FirebaseFirestore.getCurrentUser { user ->
            if (this@MyFrag.isVisible) {
                changeEntry_name.setText(user.name)
                changeEntry_bio.setText(user.bio)
                //changeEntry_age.setText(user.age)

              if (!pictureJustChanged && user.profilePicturePath != null)
                    GlideApp.with(this)
                            .load(BetaStore.imagePathForReferences(user.profilePicturePath))
                            .placeholder(R.drawable.ic_account_circle_black_24dp)
                            .into(imageView_pp)


            }

        }

    }



}