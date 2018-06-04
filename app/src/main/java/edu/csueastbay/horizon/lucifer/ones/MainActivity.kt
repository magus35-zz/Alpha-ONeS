package edu.csueastbay.horizon.lucifer.ones


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import edu.csueastbay.horizon.lucifer.ones.Frags.MyAccountFragment
import edu.csueastbay.horizon.lucifer.ones.Frags.PeopleFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    // creating the default fragment
        replaceFragment(PeopleFragment())

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_people -> {
                    replaceFragment(PeopleFragment())
                    true
                }
                R.id.navigation_events -> {
                    //Jon's show events fragment;
                    true
                }
                R.id.navigation_my_account -> {
                    replaceFragment(MyAccountFragment())

                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment ){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, fragment)
                .commit()
        }


}




