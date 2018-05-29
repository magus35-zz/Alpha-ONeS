package edu.csueastbay.horizon.lucifer.ones.recyclerview.item

import android.content.Context
import com.example.lindsey.onesmessaging.util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.Glide.GlideApp
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.model.ImageType
import edu.csueastbay.horizon.lucifer.ones.model.TestType
import kotlinx.android.synthetic.main.itemimmagemessage.*

class ImageItem(val message: ImageType,
               val context: Context)


    : MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        GlideApp.with(context)
                .load(StorageUtil.pathToReference(message.imagePath))
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(viewHolder.imageView_message_image)

    }

    override fun getLayout() = R.layout.itemimmagemessage

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is ImageItem)
            return false
        if (this.message != other.message)
            return false
        return true

    }


    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageItem)

    }


    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result

    }
}