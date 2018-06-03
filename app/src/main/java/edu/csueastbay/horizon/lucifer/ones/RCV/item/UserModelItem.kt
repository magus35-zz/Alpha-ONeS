package edu.csueastbay.horizon.lucifer.ones.RCV.item


import android.content.Context
import com.example.lindsey.onesmessaging.MessageTypes.UserType
import com.example.lindsey.onesmessaging.util.BetaStore
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.AppGlide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.item_person.*


// contains persons data and communicates its data to the item_person xml file
// the personitem will be passed into groupies adapter for recyclerview (RV)
class UserModelItem(
        // keeps track of the user
        val person: UserType,
        //id from firebaseAuth
        val userId: String,
        // keeps track of the context
        private val context: Context)

    //this inherits from groupies item
    : Item()
// need to implement all of its memebers:
{

    // layout it overriden to the one we created- item_person
    override fun getLayout() = R.layout.item_person

    // we need to bind this user to the layout
    // we are using groupies layoutCountainers ( Ctrl B) to get views
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //this is ONLY possible because androidExtentions experimental is enabled in
        // the build.gradle files

        // getting the xml text property and setting it to the person name
        viewHolder.textView_name.text = person.name

        // gettomg the xml text properties and setting this one to bio
        viewHolder.textView_bio.text = person.bio

        // Uploading profile image to layout
        if (person.profilePicturePath != null)
            GlideApp.with(context)

                    // loads in the image from firestore
                    .load(BetaStore.imagePathForReferences(person.profilePicturePath))

                    //placeholder until the image is actually uploaded
                    .placeholder(R.drawable.pp_placeholder)

                    //setting the image into the viewholder once it does load
                    // this gets ride of the placeholder
                    .into(viewHolder.imageView_pp)
    }

}