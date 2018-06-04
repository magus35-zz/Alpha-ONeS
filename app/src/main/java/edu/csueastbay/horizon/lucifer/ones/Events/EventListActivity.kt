package edu.csueastbay.horizon.lucifer.ones.Events

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import edu.csueastbay.horizon.lucifer.ones.R

class EventListActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var eventList: MutableList<Event>
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
         /*This is code for firebase it Should read from it and display the events.*/
       eventList = mutableListOf()
      ref = FirebaseDatabase.getInstance().getReference("events")




      listView = findViewById(R.id.eventsListView)
      ref.addValueEventListener(object : ValueEventListener {
          override fun onCancelled(p0: DatabaseError?) {
              TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
          }

          override fun onDataChange(p0: DataSnapshot?) {
              if(p0!!.exists()){
                  eventList.clear()
                  for(h in p0.children){
                      val event = h.getValue(Event :: class.java)
                      eventList.add(event!!)
                  }

                  val adapter = EventsAdapter(applicationContext,R.layout.activity_event_list, eventList)
                  listView.adapter = adapter
              }
          }

      })

        val newEventActivity : Button = findViewById(R.id.new_event_BTN)
        newEventActivity.setOnClickListener{
            val intent = Intent(this, New_Event_Activity :: class.java)
            startActivity(intent)
        }
    }
}
