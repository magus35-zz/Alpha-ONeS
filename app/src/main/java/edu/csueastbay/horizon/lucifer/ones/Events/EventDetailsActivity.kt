package edu.csueastbay.horizon.lucifer.ones.Events

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.csueastbay.horizon.lucifer.ones.R

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val inviteMatches : Button = findViewById(R.id.invite_matches_BTN)
        inviteMatches.setOnClickListener{
            val intent = Intent(this, Invite_Matches_Activity :: class.java)
            startActivity(intent)
        }
    }
}
