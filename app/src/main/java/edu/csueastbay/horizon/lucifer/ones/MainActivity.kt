package edu.csueastbay.horizon.lucifer.ones


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import edu.csueastbay.horizon.lucifer.ones.Frags.UserFrag
import edu.csueastbay.horizon.lucifer.ones.Frags.PeopleFrag
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    // creating the default fragment
        replaceFragment(PeopleFrag())

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_people -> {
                    replaceFragment(PeopleFrag())
                    true
                }
                R.id.navigation_events -> {
                    //Jon's show events fragment;
                    true
                }
                R.id.navigation_my_account -> {
                    replaceFragment(UserFrag())

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




