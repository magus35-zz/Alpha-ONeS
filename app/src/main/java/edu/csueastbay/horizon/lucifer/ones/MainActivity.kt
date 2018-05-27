package edu.csueastbay.horizon.lucifer.ones

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import edu.csueastbay.horizon.lucifer.ones.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.navigation_people -> {
                    //Bigg's show people fragment;
                    true
                }
                R.id.navigation_events -> {
                    //Jon's show events fragment;
                    true
                }
                R.id.navigation_my_account -> {
                    //Joe's show my account fragment ;
                    true
                }
                else -> false
                }
            }
        }
    }

