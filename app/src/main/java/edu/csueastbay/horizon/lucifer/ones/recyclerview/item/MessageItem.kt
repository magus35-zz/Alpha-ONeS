package edu.csueastbay.horizon.lucifer.ones.recyclerview.item

// How to use lparams: https://github.com/Kotlin/anko/issues/392
import android.view.Gravity
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.MessageTypes.MessageTypeSent
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.rcv_text.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat




abstract class MessageItem(private val message: MessageTypeSent)

    : Item() {


    // we want to get the viewholder, and then get the text that was sent
    override fun bind(viewHolder: ViewHolder, position: Int)
    {
        // calling setTimeSent to get the date and time the message was sent
        setTimeText(viewHolder)
        // calling setMessageGravity so that the correct background images and "gravity"
        // is attahed to each type of person's message ( user vs match)
        setMessageRootGravity(viewHolder)

    }


    // creating function setTimeSent to show whemn the message was acutally send using
    // the messaging template
    private fun setTimeText(viewHolder: ViewHolder) {
        // setting date time to short because no  one needs long
        val messageTimeStamp = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        // our viewholder will then get the time that the message was sent through message temeplate

        viewHolder.textView_time_message_sent.text = messageTimeStamp.format(message.time)

    }



    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        // our viewholder will then get the time that the message was sent through message temeplate

        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.message_root.apply {
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                // we  want the frame layout for each message that is sent
                // gravity will be set to end, so that the main user's message will always be on the  right
                backgroundResource = R.drawable.text_right
                // then we want to set the main user's layout to be  what the l paramas are
                this.layoutParams = lParams

            }

        }
        // otherwise if it is the match who has sent the message follow this layoutparams instead:

        else
        {

            viewHolder.message_root.apply {
                // still going to use a lparam and relative layout, but we are going to make sure that the
                //gravity parameter is set to START, so that the match's message will be on the left side of screen
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                //need to update the background also for the layout,we can keep it the same

                backgroundResource = R.drawable.text_right
                // make sure that the layoutparams are actually set to lparams

                this.layoutParams = lParams

            }

        }

    }

}