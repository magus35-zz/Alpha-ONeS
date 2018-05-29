package edu.csueastbay.horizon.lucifer.ones;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;
import edu.csueastbay.horizon.lucifer.ones.R;


import java.util.List;

public class EventMatchingView extends BaseCardAdapter {

    private List<Model> modelList;
    private Context context;


    public EventMatchingView(List<Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.activity_event_matching_view;
    }

    @Override
    public void onBindData(int position, View cardview)
    {

        if(modelList == null || modelList.size() == 0)
        {
            return;
        }
    }
}
