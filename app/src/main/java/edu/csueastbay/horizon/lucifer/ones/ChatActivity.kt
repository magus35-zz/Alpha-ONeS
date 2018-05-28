package edu.csueastbay.horizon.lucifer.ones

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.lindsey.onesmessaging.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.csueastbay.horizon.lucifer.ones.model.MessageType
import edu.csueastbay.horizon.lucifer.ones.model.Text
import kotlinx.android.synthetic.main.activity_chat_.*
import java.util.*


class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messageSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            imageView_send.setOnClickListener {
                val messageToSend =
                        Text(editText_message.text.toString(), Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                //get edit text and set to string
                editText_message.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }
            fab_send_image.setOnClickListener{
                //TODO: Send image meessages
            }

        }
    }
    private fun updateRecyclerView(messages: List<Item>){
        fun init()
        {
            recycler_view_messages.apply{
                layoutManager = LinearLayoutManager( this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply{
                   messageSection = Section(messages)
                    this.add(messageSection)
                }
            }
            shouldInitRecyclerView = false
        }
        fun updateItems() = messageSection.update(messages)
        if(shouldInitRecyclerView)
        {
            init()
        }
        else
            updateItems()
        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter.itemCount - 1)

    }
}



