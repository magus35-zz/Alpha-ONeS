package edu.csueastbay.horizon.lucifer.ones.recyclerview.item
//https://developer.android.com/guide/topics/ui/layout/recyclerview

import android.content.Context
import com.example.lindsey.onesmessaging.util.BetaStore
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.GlideFromGroupie.GlideApp
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.ImageType
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.rcv_image.*


// setting up recyclerview ( RV) for sending images
// need to specify the image type and the conxt of it
class ImageItem(
        val message: ImageType,
        val context: Context)


    : MessageItem(message)
{
    // specifying that we are going to be using itemimagemessage
    // as the layout
    override fun getLayout() = R.layout.rcv_image

    // since were using the item class were using Groupie's extension of recyleviewer
    // to be able to bind the types when they are generated
    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        super.bind(viewHolder, position)
        //Glideapp helps with loading the images and caching them

        GlideApp.with(context)
                // pulls the image path to set its reference

                .load(BetaStore.pathToReference(message.imagePath))
                // if there is no image we are just oging to send image icon

                .placeholder(R.drawable.imageplaceholder)
                //we are putting that into itemimagemessage in its viewholder

                .into(viewHolder.imageView_image_for_message)

    }
    // checks to make sure that we actually passing in the correct type
     override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageItem)

    }

    // overrides funcation and is true if the pic that we want is recyclable

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is ImageItem)
        {
            return false
        }
        if (this.message != other.message)
        {// otherwise it is set to false
            return false
        }


        return true

    }




    //Apparently we need to override hasCode since we are overriding equals
    //https://medium.com/@appmattus/effective-kotlin-item-11-always-override-hashcode-when-you-override-equals-608a090aeaed
    override fun hashCode(): Int {
        var result = message.hashCode()
        // kotlin doesnt mesh with groupie the best but this is the bandaid
        result = 31 * result + context.hashCode()
        return result

    }
}