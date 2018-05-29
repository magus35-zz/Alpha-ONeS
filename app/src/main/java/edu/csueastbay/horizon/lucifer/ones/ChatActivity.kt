package edu.csueastbay.horizon.lucifer.ones

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.lindsey.onesmessaging.util.FirestoreUtil
import com.example.lindsey.onesmessaging.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.csueastbay.horizon.lucifer.ones.model.ImageType
import edu.csueastbay.horizon.lucifer.ones.model.MessageType
import edu.csueastbay.horizon.lucifer.ones.model.TestType
import kotlinx.android.synthetic.main.activity_chat_.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE =2
class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var currentChannelId: String
    private lateinit var messageSection: Section
    private var shouldInitRecyclerView = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
         currentChannelId = channelId

            messagesListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)
            imageView_send.setOnClickListener {
                val messageToSend =
                        TestType(editText_message.text.toString(), Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                //get edit text and set to string
                editText_message.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }
            fab_send_image.setOnClickListener{
                val intent = Intent().apply{
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/gif", "image/png", "image/jpeg"))

                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)
            // converting to byte output stream
            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()
            //upload to cloud storage

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                        ImageType(imagePath, Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid)

                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
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



