package edu.csueastbay.horizon.lucifer.ones.Frags




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
import edu.csueastbay.horizon.lucifer.ones.ConstantVal
import edu.csueastbay.horizon.lucifer.ones.MessageActivity
import edu.csueastbay.horizon.lucifer.ones.R
import edu.csueastbay.horizon.lucifer.ones.recyclerview.item.UserModelItem
import kotlinx.android.synthetic.main.match_list_page.*
import org.jetbrains.anko.support.v4.startActivity


class  PeopleFrag : Fragment() {


// "listening" for an up to date list of users
    private lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true

    //creates sections from Groupie
    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userListenerRegistration =
                FirebaseFirestore.addUsersListener(this.activity!!, this::updateRecyclerView)
        return inflater.inflate(R.layout.match_list_page, container, false)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        FirebaseFirestore.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true }



    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFrag.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                   //section holds the items NOT group adapter
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is UserModelItem) {
            startActivity<MessageActivity>(
                    ConstantVal.MATCH_ID to item.userId,
                    ConstantVal.MATCH_NAME to item.person.name
            )

        }
    }
}





