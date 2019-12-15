package com.example.myitime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myitime.data.model.Event;

import java.util.Calendar;

import static com.example.myitime.ListViewMainActivity.RESULT_CODE_ADD_CANCEL;
import static com.example.myitime.ListViewMainActivity.RESULT_CODE_ADD_OK;
import static com.example.myitime.ListViewMainActivity.RESULT_CODE_REV_CANCEL;
import static com.example.myitime.ListViewMainActivity.RESULT_CODE_REV_OK;
import static java.lang.String.valueOf;

public class CreateEventActivity extends AppCompatActivity {

    private Button btnBack,btnEnter;
    private EditText eventName,eventDiscription,eventYear,eventMonth,eventDay,eventHour,eventMinute;
    private Event e;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnBack = findViewById(R.id.button_back);
        btnEnter = findViewById(R.id.button_enter);

        btnBack.setOnClickListener(new BtnBackListener());
        btnEnter.setOnClickListener(new BtnEnterListener());

        eventName = findViewById(R.id.editText_name);
        eventDiscription = findViewById(R.id.editText_discription);
        eventYear = findViewById(R.id.editText_year);
        eventMonth = findViewById(R.id.editText_month);
        eventDay = findViewById(R.id.editText_day);
        eventHour = findViewById(R.id.editText_hour);
        eventMinute = findViewById(R.id.editText_minute);

        Bundle bundle = getIntent().getExtras();
        e = new Event(
                bundle.getInt("event_year"),
                bundle.getInt("event_month"),
                bundle.getInt("event_day"),
                bundle.getInt("event_hour"),
                bundle.getInt("event_minute"),
                bundle.getString("event_name"),
                bundle.getString("event_discription"),
                bundle.getInt("event_image")
        );
        eventName.setText(e.getEventName());
        eventDiscription.setText(e.getEventDescription());
        eventYear.setText(valueOf(e.getEventDate().get(Calendar.YEAR)));
        eventMonth.setText(valueOf(e.getEventDate().get(Calendar.MONTH)+1));
        eventDay.setText(valueOf(e.getEventDate().get(Calendar.DAY_OF_MONTH)));
        eventHour.setText(valueOf(e.getEventDate().get(Calendar.HOUR_OF_DAY)));
        eventMinute.setText(valueOf(e.getEventDate().get(Calendar.MINUTE)));
        
        flag = bundle.getInt("flag");
    }

    class BtnBackListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(flag == 0){
                setResult(RESULT_CODE_ADD_CANCEL);
            }
            else if(flag == 1){
                setResult(RESULT_CODE_REV_CANCEL);
            }
            CreateEventActivity.this.finish();
        }
    }

    class BtnEnterListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            Intent intentEnter = new Intent();
            
            Bundle bundle = new Bundle();

            Calendar tmpNow = Calendar.getInstance();
            String tmpName,tmpDiscription;
            int tmpYear,tmpMonth,tmpDay,tmpHour,tmpMinute;
            if(eventName.getText().toString().isEmpty()){ tmpName = "NULL"; }
            else{ tmpName = eventName.getText().toString(); }
            if(eventDiscription.getText().toString().isEmpty()){ tmpDiscription = ""; }
            else{ tmpDiscription = eventDiscription.getText().toString(); }
            if(eventYear.getText().toString().isEmpty()){ tmpYear = tmpNow.get(Calendar.YEAR); }
            else{ tmpYear = Integer.parseInt(eventYear.getText().toString()); }
            if(eventMonth.getText().toString().isEmpty()){ tmpMonth = tmpNow.get(Calendar.MONTH)+1; }
            else{ tmpMonth = Integer.parseInt(eventMonth.getText().toString()); }
            if(eventDay.getText().toString().isEmpty()){ tmpDay = tmpNow.get(Calendar.DAY_OF_MONTH); }
            else{ tmpDay = Integer.parseInt(eventDay.getText().toString()); }
            if(eventHour.getText().toString().isEmpty()){ tmpHour = tmpNow.get(Calendar.HOUR_OF_DAY); }
            else{ tmpHour = Integer.parseInt(eventHour.getText().toString()); }
            if(eventMinute.getText().toString().isEmpty()){ tmpMinute = tmpNow.get(Calendar.MINUTE); }
            else{ tmpMinute = Integer.parseInt(eventMinute.getText().toString()); }

            bundle.putString("eventName",tmpName);
            bundle.putString("eventDiscription",tmpDiscription);
            bundle.putInt("eventYear",tmpYear);
            bundle.putInt("eventMonth",tmpMonth);
            bundle.putInt("eventDay",tmpDay);
            bundle.putInt("eventHour",tmpHour);
            bundle.putInt("eventMinute",tmpMinute);
            bundle.putInt("eventImage",e.getEventImage());

            intentEnter.putExtras(bundle);
            if(flag == 0){
                setResult(RESULT_CODE_ADD_OK,intentEnter);
            }
            else if(flag == 1){
                setResult(RESULT_CODE_REV_OK,intentEnter);
            }
            CreateEventActivity.this.finish();
        }
    }
}
