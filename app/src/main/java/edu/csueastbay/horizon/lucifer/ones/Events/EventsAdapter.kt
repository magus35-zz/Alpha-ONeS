package edu.csueastbay.horizon.lucifer.ones.Events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import edu.csueastbay.horizon.lucifer.ones.R

class EventsAdapter (val mCtx: Context, val layoutResId: Int, val EventList: List<Event>)
: ArrayAdapter<Event>(mCtx, layoutResId, EventList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewName = view.findViewById<TextView>(R.id.eventsListView)

        val event = EventList[position]

        textViewName.text = event.name

        return view
    }


}
