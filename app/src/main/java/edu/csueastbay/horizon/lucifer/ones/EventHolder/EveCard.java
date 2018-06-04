package edu.csueastbay.horizon.lucifer.ones.EventHolder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import edu.csueastbay.horizon.lucifer.ones.R;

@Layout(R.layout.eve_time)
public class EveCard {
    @View(R.id.eventImageView)
    private ImageView eveImageView;

    @View(R.id.nameEvtTxt)
    private TextView nameEvtTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Eve mEve;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public EveCard(Context context, Eve eve, SwipePlaceHolderView swipeView) {
        mContext = context;
        mEve = eve;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mEve.getImageUrl()).into(eveImageView);
        nameEvtTxt.setText(mEve.getName() + ", " + mEve.getTime());
        locationNameTxt.setText(mEve.getLocation());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

}
