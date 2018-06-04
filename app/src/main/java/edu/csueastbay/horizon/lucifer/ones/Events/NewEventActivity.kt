package edu.csueastbay.horizon.lucifer.ones.Events

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import edu.csueastbay.horizon.lucifer.ones.R
import org.json.JSONObject

class NewEventActivity : AppCompatActivity() {
    lateinit var nameTxtPln : EditText
    lateinit var dateTxtPln : EditText
    lateinit var locationTxtPln : EditText
    lateinit var descriptionTxtPln : EditText
    lateinit var createButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)

        createJsonData()

        nameTxtPln = findViewById(R.id.nametxtpln)
        dateTxtPln = findViewById(R.id.datetxtpln)
        locationTxtPln = findViewById(R.id.location_txt_pln)
        descriptionTxtPln = findViewById(R.id.description_txt_pln)
        createButton = findViewById(R.id.create_BTN)

        createButton.setOnClickListener{

        /*  saveEvent()*/
        val intent = Intent(this, Event_List_Activity::class.java)
        startActivity(intent)
    }




    val cancel : Button = findViewById(R.id.cancel_BTN)
    cancel.setOnClickListener{
        val intent = Intent(this, EventListActivity :: class.java)
        startActivity(intent)
    }
}
    private fun createJsonData() {
        var json = JSONObject()

        val event = Event("", "", "", "", "")
        json.put("Event", addEvent(Event))
    }

    private fun addEvent(event: Event): JSONObject {

        return JSONObject()
                .put("",Event)

    }


    private fun saveEvent(){
    val name = nameTxtPln.text.toString().trim()
    if(name.isEmpty()){
        nameTxtPln.error ="Please enter a name"
        return
    }

    val date = dateTxtPln.text.toString().trim()
    if(date.isEmpty()){
        dateTxtPln.error ="Please enter a date"
        return
    }

    val location = locationTxtPln.text.toString().trim()
    if(location.isEmpty()){
        locationTxtPln.error ="Please enter a location"
        return
    }

    val description = descriptionTxtPln.text.toString().trim()
    if(description.isEmpty()){
        descriptionTxtPln.error ="Please enter a date"
        return
    }


    /* val ref = FirebaseDatabase.getInstance().getReference("events")
     val eventId=ref.push().key

     val event = Event(eventId, name, date, location, description)


     ref.child(eventId).setValue(event).addOnCompleteListener {
         Toast.makeText(applicationContext,"Event Saved", Toast.LENGTH_LONG).show()
     }
*/


    }
}
