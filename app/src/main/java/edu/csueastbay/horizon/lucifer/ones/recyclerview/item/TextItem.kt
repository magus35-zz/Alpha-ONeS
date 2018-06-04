package edu.csueastbay.horizon.lucifer.ones.recyclerview.item

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.TestType
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.rcv_text.*


class TextItem(
        // make sure to set message type as text type for this item
        val message: TestType,
        val context: Context)


    : MessageItem(message) {
    // setting up recyclerview ( RV) for sending messages
// need to specify the text type and the context of it for the viewholders

    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        // message in here is passed in from the constructor
        viewHolder.textView_message_text.text = message.text

        // then they will be binded with the viewholder and postiion
        super.bind(viewHolder, position)

    }
    // specifying that we are going to be using textmessage item as the layout

    override fun getLayout()= R.layout.rcv_text

    // since were using the item class were using Groupie's extension of recyleviewer
    // to be able to bind the types when they are generated


    // overrides funcation to see if the text message is has an equal value
    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextItem)

    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is TextItem)
        {
            return false
        }
        if (this.message != other.message)
        {
            return false
        }
        //else just return true that the iteams are the same

        return true
        // link at the top of the file for more information on Groupie


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
