package edu.csueastbay.horizon.lucifer.ones.recyclerview.item


import android.content.Context



import com.xwray.groupie.kotlinandroidextensions.Item

import com.xwray.groupie.kotlinandroidextensions.ViewHolder

import kotlinx.android.synthetic.main.item_person.*
import com.example.lindsey.onesmessaging.model.UserType
import com.example.lindsey.onesmessaging.util.BetaFirestore
import edu.csueastbay.horizon.lucifer.ones.Glide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.R

class PersonItem(val person: UserType,
                 //id from firebaseAuth
                 val userId: String,
                 private val context: Context)

    : Item() {


// binds user to the layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio
        if (person.profilePicturePath != null)
            GlideApp.with(context)
                    .load(BetaFirestore.pathToReference(person.profilePicturePath))
                  //placeholder until they upload image
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person

}