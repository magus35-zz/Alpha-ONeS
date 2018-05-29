package edu.csueastbay.horizon.lucifer.ones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.List;

public class UserMatchingView extends BaseCardAdapter {

    private List<Model> modelList;


    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.activity_user_matching_view;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if(modelList == null || modelList.size() == 0)
        {
            return;
        }
    }
}
