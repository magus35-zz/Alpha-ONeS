package edu.csueastbay.horizon.lucifer.ones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;


//public class EventHolder extends AppCompatActivity {
public class  EventHolder extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener{

    Switch switch1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_holder);

        textView = (TextView)findViewById(R.id.textView);
        switch1 = (Switch)findViewById(R.id.switch1);

        switch1.setOnCheckedChangeListener(this);

        getMode();
    }
    private void getMode()
    {

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {

        if(switch1.isChecked())
        {
            textView.setText("Swich On");
            startActivity(new Intent(EventHolder.this,UserMatchingView.class));
        }
        else
        {
            textView.setText("Swich Off");
            startActivity(new Intent(EventHolder.this,EventMatchingView.class));
        }


    }

}
