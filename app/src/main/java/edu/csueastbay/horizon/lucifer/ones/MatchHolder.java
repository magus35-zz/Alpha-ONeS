package edu.csueastbay.horizon.lucifer.ones;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import edu.csueastbay.horizon.lucifer.ones.EventHolder.Eve;
import edu.csueastbay.horizon.lucifer.ones.EventHolder.EveCard;
import edu.csueastbay.horizon.lucifer.ones.EventHolder.EveUtils;
import edu.csueastbay.horizon.lucifer.ones.UserMatching.Profile;
import edu.csueastbay.horizon.lucifer.ones.UserMatching.UserCard;
import edu.csueastbay.horizon.lucifer.ones.UserMatching.Utils;

public class MatchHolder extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_holder);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mSwipeView.removeAllViews();
        mContext = getApplicationContext();
// Default mode Event Mode
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_a)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_r));


        for (Eve eve : EveUtils.loadEvents(this.getApplicationContext())) {
            mSwipeView.addView(new EveCard(mContext, eve, mSwipeView));
        }


        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });


    }

    // Method that detects when the switch is flipped from on to off and vice versa
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        // When switch is on user mode is activated
        if (switch1.isChecked()) {

            mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
            mSwipeView.removeAllViews();
            mContext = getApplicationContext();
            // This combines the profile cards with reject and accept text and detects swipe direction
            mSwipeView.getBuilder()
                    .setDisplayViewCount(3)
                    .setSwipeDecor(new SwipeDecor()
                            .setPaddingTop(20)
                            .setRelativeScale(0.01f)
                            .setSwipeInMsgLayoutId(R.layout.swipe_a)
                            .setSwipeOutMsgLayoutId(R.layout.swipe_r));

            // Loops through the database and find users information and image and display them in Card form
            for (Profile profile : Utils.loadProfiles(this.getApplicationContext())) {
                mSwipeView.addView(new UserCard(mContext, profile, mSwipeView));
            }

            findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(false);
                }
            });




            findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(true);
                }

            });
        }
        // When switch is off Event mode is active
        else
        {

            mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
            mSwipeView.removeAllViews();
            mContext = getApplicationContext();
// This combines the events cards with reject and accept text and detects swipe direction
            mSwipeView.getBuilder()
                    .setDisplayViewCount(3)
                    .setSwipeDecor(new SwipeDecor()
                            .setPaddingTop(20)
                            .setRelativeScale(0.01f)
                            .setSwipeInMsgLayoutId(R.layout.swipe_a)
                            .setSwipeOutMsgLayoutId(R.layout.swipe_r));

            // Loops through the database and find event information and image and display them in Card form
            for (Eve eve : EveUtils.loadEvents(this.getApplicationContext())) {
                mSwipeView.addView(new EveCard(mContext, eve, mSwipeView));
            }


            findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(false);
                }
            });

            findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(true);
                }
            });        }


    }
}



