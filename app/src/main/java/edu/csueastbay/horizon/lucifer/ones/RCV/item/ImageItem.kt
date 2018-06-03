package edu.csueastbay.horizon.lucifer.ones.RCV.item

//https://developer.android.com/guide/topics/ui/layout/recyclerview

import android.content.Context
import com.example.lindsey.onesmessaging.util.BetaStore
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.AppGlide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.ImageType
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.itemimmagemessage.*


// setting up recyclerview ( RV) for sending images
// need to specify the image type and the conxt of it
class ImageItem(val message: ImageType,
               val context: Context)


    : MessageItem(message) {

    // specifying that we are going to be using itemimagemessage
   // as the layout
    override fun getLayout() = R.layout.itemimmagemessage

    // since were using the item class were using Groupie's extension of recyleviewer
    // to be able to bind the types when they are generated
    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

//Glideapp helps with loading the images and caching them
        GlideApp.with(context)
                // pulls the image path to set its reference
                .load(BetaStore.imagePathForReferences(message.picturePath))
               // if there is no image we are just oging to send image icon
                .placeholder(R.drawable.image_placeholder_pink)
               //we are putting that into itemimagemessage in its viewholder
                .into(viewHolder.imageView_message_image)

    }
    // overrides funcation and is true if the pic that we want is recyclable

    override fun equals(other: Any?)
            : Boolean
    {
        return isSameAs(other as? ImageItem)
        // otherwise it is set to false
    }

// uses groupie to determine if the
    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is ImageItem)
            return false
        if (this.message != other.message)
            return false
        return true

    }

    //Apparently we need to override hasCode since we are overriding equals
    //https://medium.com/@appmattus/effective-kotlin-item-11-always-override-hashcode-when-you-override-equals-608a090aeaed

    override fun hashCode(): Int {
        var ret = message.hashCode()
        ret = 31 * ret + context.hashCode()
        return ret

    }
}