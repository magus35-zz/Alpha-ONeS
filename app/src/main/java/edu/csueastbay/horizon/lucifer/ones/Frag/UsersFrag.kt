package edu.csueastbay.horizon.lucifer.ones.Frag




import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lindsey.onesmessaging.util.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import edu.csueastbay.horizon.lucifer.ones.ConstantVars
import edu.csueastbay.horizon.lucifer.ones.MessagingActivity
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.RCV.item.UserModelItem
import kotlinx.android.synthetic.main.fragment_people.*
import org.jetbrains.anko.support.v4.startActivity


class  UsersFrag : Fragment() {


// "listening" for an up to date list of users
    //userListenerRegistration can be used to delete users
    private lateinit var userListenerRegistration: ListenerRegistration

    private var willRecycleView = true

    //creates section from Groupie for organization
    private lateinit var userSection: Section

    //creating view through Groupie MUST HAVE experimental activated
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              //finds out which instance and the view
                              savedInstanceState: Bundle?): View? {

        // calls update RecyclerView with fragment to make sure everyone who is logged in shows
        userListenerRegistration =
                FirebaseFirestore.addUsersListener(this.activity!!, this::updateRecyclerView)
        return inflater.inflate(R.layout.fragment_people, container, false)

    }


// Remove the listener, and destroys the view
    override fun onDestroyView() {
        super.onDestroyView()
        //removing listener from firestore
        FirebaseFirestore.removeListener(userListenerRegistration)
    // setting will recycleview to true so that the view can be recycled
        willRecycleView = true

    }


// updateRecyclerView is called from on CreateView/ userListenerRegistation
    private fun updateRecyclerView(items: List<Item>) {

        fun initialization() {
            // applyting the following reycle view layout
            recycler_view_people.apply {
                // Each user aka userfragments are geting layout through groupie
                layoutManager = LinearLayoutManager(this@UsersFrag.context)
                // Groupies GroupAfapter applied the view
                adapter = GroupAdapter<ViewHolder>().apply {
                   // need to create a new section
                    userSection = Section(items)
                   //NOTE: section holds the items NOT group adapter
                   add(userSection)
                    //sets listener for onitemclick
                    setOnItemClickListener(onItemClick)
                }
            }
            // views will not be recycelable
            willRecycleView = false
        }
        // when we need to update the list we can use updateUserItems from groupie
        fun updateUserItems() = userSection.update(items)
        // if the view will recycle we send it back up to function initilization
        if (willRecycleView)
            initialization()
        else
            // we dont have anything to recycle so we to to updateUserItems
            updateUserItems()

    }

    private val onItemClick = OnItemClickListener{ item, view ->
        if (item is UserModelItem)
        {
            // utilizing the usermodelitem which is crates the person
            startActivity<MessagingActivity>(

                    // values gathers from constant vars and puts them in the sections
                    ConstantVars.MATCH_ID to item.userId,
                    // matchid and name are both stored in firestore
                    ConstantVars.MATCHS_NAME to item.person.name
            )

        }
    }
}





