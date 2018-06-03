package edu.csueastbay.horizon.lucifer.ones.RCV.item

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.TextType
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.itemtext.*

//
class TextItem(val message: TextType,
               val context: Context)


    : MessageItem(message)
{
    // setting up recyclerview ( RV) for sending messages
// need to specify the text type and the context of it for the viewholders
    override fun bind(viewHolder: ViewHolder, position: Int) {
    // message in here is passed in from the constructor
        viewHolder.textView_message_text.text = message.text
        // then they will be binded with the viewholder and postiion
        super.bind(viewHolder, position) }

    // specifying that we are going to be using textmessage item as the layout
    override fun getLayout()= R.layout.itemtext

    // since were using the item class were using Groupie's extension of recyleviewer
    // to be able to bind the types when they are generated
    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextItem) {
            return false
        }
        if (this.message != other.message) {
            return false
        }
        //else just return true that the iteams are the same
        return true
        // link at the top of the file for more information on Groupie

    }
    // overrides funcation to see if the text message is has an equal value
    override fun equals(other: Any?): Boolean {
        // it does we can return the is same fucntion with the text item
        return isSameAs(other as? TextItem)

    }

    //Apparently we need to override hasCode since we are overriding equals
    //https://medium.com/@appmattus/effective-kotlin-item-11-always-override-hashcode-when-you-override-equals-608a090aeaed
    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result

    }
}
