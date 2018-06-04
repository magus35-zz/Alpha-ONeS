package edu.csueastbay.horizon.lucifer.ones

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.lindsey.onesmessaging.util.FirestoreFirebase
import com.example.lindsey.onesmessaging.util.BetaFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import edu.csueastbay.horizon.lucifer.ones.systemTypes.ImageType
import edu.csueastbay.horizon.lucifer.ones.systemTypes.MessageType
import edu.csueastbay.horizon.lucifer.ones.systemTypes.TestType
import kotlinx.android.synthetic.main.activity_chat_.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE =2
class ChatActivity : AppCompatActivity() {

    //Is the current channelID that we are viewing
    private lateinit var messagesListenerRegistration: ListenerRegistration
    // Is the messageSection from Groupie
    private lateinit var currentChannelId: String
    // Is the communicator for firebase Util
    private lateinit var messageSection: Section
    // Initially set all recyclerviews to being able to be recycled
    private var shouldInitRecyclerView = true


    override fun onCreate(savedInstanceState: Bundle?)
    {   // overrding on create function to send messages
        super.onCreate(savedInstanceState)
        // pointing to our layout activity message
        setContentView(R.layout.activity_chat_)

        // creates a way to go back to the messageing list
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //the match's name , whom the user is chatting with will appear at the top
        supportActionBar?.title =intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        // Call firestore so we can get the doc, and we need to remember to
        FirestoreFirebase.getOrCreateChatChannel(otherUserId) { channelId ->
         currentChannelId = channelId   //go to the current chat channel so a random one doesnt appear

            // need to utilize the messlistener to add a new channel to
            messagesListenerRegistration = FirestoreFirebase.addChatMessagesListener(channelId, this, this::updateRecyclerView)
            // calling xml file to tell it what the send button does
            imageView_send.setOnClickListener {
                //we are pushing the values that messageToSeend will need which we get from TextType beacuse
                // we are sending a text message
                val messageToSend = TestType(editText_message.text.toString(), Calendar.getInstance().time, FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)

                //get edit text and set to string
                editText_message.setText("")
                // use messagequeued function to implmement the context message to send and the channelid to make sure were are in the correct channelID
                FirestoreFirebase.sendMessage(messageToSend, channelId)
            }
            fab_send_image.setOnClickListener{
                // we are picking what image we want to send utilizing the activity_messaging file
                val intent = Intent().apply{
                    // we dont care what type of image we are sending so we are passing all types through
                    type = "image/*"
                    // we need the action function to say when clicked we are goign to get content
                    action = Intent.ACTION_GET_CONTENT
                    // we are declaring the actual image type that we are choosing
                    // it will be an arrayof, but it can be anytype of picture type
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/gif", "image/png", "image/jpeg"))

                }
                //The target intent for this function is the one above and we want to utilize RCIMageSelected request code
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        // we want to see if the request code is actually the RCimageSlected and if the result acitivity is
        //if the request code was acutally called from above and data
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            // the imagepath  will be stored into data.dad which will go to firebasefirestore
            val selectedImagePath = data.data
            // get the bitmap from mediastore and /getBitmat and convert it to a byte array
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)
            // converting to byte output stream
            val outputStream = ByteArrayOutputStream()

            // we also need to compres the image and we are changning it to JPEG and output it to outputsteram
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            //we need the actual bytes of the image and put that into toByteArray
            val selectedImageBytes = outputStream.toByteArray()
            //upload to cloud storage

            //upload to cloud storage that is the same as upload profile picture
            BetaFirestore.uploadMessageImage(selectedImageBytes) { imagePath ->
                // include the imagepath for the picture that the user is trying to send, as well as including the rest
                // of the message values
                val messageToSend =
                        ImageType(imagePath, Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid)

                //sends message up to firestore to be delivered to the match via the same channel that they are in
                FirestoreFirebase.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    // now we may need to update the recyclerview so that it has the most up to date list of people and
    // their bios on the match list.
    private fun updateRecyclerView(messages: List<Item>){

        // we have our list of items that we are initazting and pass them thorugh initiation function
        fun init()
        {
            // we are updating the xml file activity messaging wiht the lastest layout
            recycler_view_messages.apply{
                // The layoutManager from groupie deals with the layout for messages
                layoutManager = LinearLayoutManager( this@ChatActivity)
                //groupie adapter setsup the viewholder- similar to what we did with items
                adapter = GroupAdapter<ViewHolder>().apply{
                    // messageSetion brings in the message NOT the Section
                   messageSection = Section(messages)
                    //it is added to the section
                    this.add(messageSection)
                }
            }
            // we set recyclerviewr to false becuase we want to keep the current view
            shouldInitRecyclerView = false
        }

        //  now we need to update the messagesitems
        fun updateItems() = messageSection.update(messages)

        // if the shoudlInitializerecyclerview does not run through intilization
        if(shouldInitRecyclerView)
        {
            init()
            // otherwise we the messages will automatically scroll down to where we need to go
        }
        else
            updateItems()

        // show our value to to one "below" whwere the last message was sent
        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter.itemCount - 1)
    }
}



