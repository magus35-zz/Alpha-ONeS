package edu.csueastbay.horizon.lucifer.ones.recyclerview.item

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.systemTypes.TestType
import kotlinx.android.synthetic.main.itemtext.*


class TextItem(val message: TestType,
               val context: Context)


    : MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_message_text.text = message.text
        super.bind(viewHolder, position)

    }

    override fun getLayout()= R.layout.itemtext

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is TextItem)
            return false
        if (this.message != other.message)
            return false
        return true

    }



    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextItem)

    }


    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result

    }
}
