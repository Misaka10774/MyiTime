package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myitime.data.model.Event;

import java.util.Calendar;


import static com.example.myitime.ListViewMainActivity.RESULT_CODE_DEL_OK;
import static java.lang.String.valueOf;

public class EventDetailsActivity extends AppCompatActivity implements Runnable{

    private ImageView imageViewBackGround;
    private Button buttonBack,buttonDel,buttonRev;
    private TextView eventName,eventDate,eventLeftDay,eventLeftHour,eventLeftMinute,eventLeftSecond;
    private Event e;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        imageViewBackGround = findViewById(R.id.image_view_background);
        buttonBack = findViewById(R.id.button_back);
        buttonDel = findViewById(R.id.button_del);
        buttonRev = findViewById(R.id.button_rev);
        eventDate = findViewById(R.id.text_view_datetime);
        eventName = findViewById(R.id.text_view_name);
        eventLeftDay = findViewById(R.id.text_view_days);
        eventLeftHour = findViewById(R.id.text_view_hour);
        eventLeftMinute = findViewById(R.id.text_view_minute);
        eventLeftSecond = findViewById(R.id.text_view_second);

        e = new Event(getIntent().getIntExtra("event_year",1900),
                getIntent().getIntExtra("event_month",1),
                getIntent().getIntExtra("event_day",1),
                getIntent().getIntExtra("event_hour",0),
                getIntent().getIntExtra("event_minute",0),
                getIntent().getStringExtra("event_name"),
                getIntent().getStringExtra("event_discription"),
                getIntent().getIntExtra("event_image",R.drawable.clock4)
        );

        buttonBack.setOnClickListener(new BtnBackListener());
        buttonDel.setOnClickListener(new BtnDelListener());
        buttonRev.setOnClickListener(new BtnRevListener());

        init();

        thread = new Thread(this);
        thread.start();
    }

    public void init(){
        imageViewBackGround.setImageDrawable(getResources().getDrawable(e.getEventImage()));
        eventName.setText(e.getEventName());
        eventLeftDay.setText(valueOf(e.getLeftTotalDay()));
        eventLeftHour.setText(valueOf(e.getLeftHour()));
        eventLeftMinute.setText(valueOf(e.getLeftMinute()));
        eventLeftSecond.setText(valueOf(e.getLeftSecond()));
        String tmp = e.getEventDate().get(Calendar.YEAR) + "/" +
                (e.getEventDate().get(Calendar.MONTH)+1) + "/" +
                e.getEventDate().get(Calendar.DAY_OF_MONTH) + "/" + "\t" +
                e.getEventDate().get(Calendar.HOUR_OF_DAY) + ":";
        if(e.getEventDate().get(Calendar.MINUTE) < 10){
            tmp += "0";
        }
        tmp += e.getEventDate().get(Calendar.MINUTE);
        eventDate.setText(tmp);
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag){
            e.CalLeft();
            EventDetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            });
            try{
                thread.sleep(1000);
            }catch (InterruptedException e){
                flag = false;
            }
        }
    }

    private class BtnBackListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            EventDetailsActivity.this.finish();
        }
    }

    private class BtnDelListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Dialog dialog = new AlertDialog.Builder(EventDetailsActivity.this)
                    .setMessage("Do you want to delete this event?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setResult(RESULT_CODE_DEL_OK);
                            EventDetailsActivity.this.finish();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create();
            dialog.show();
        }
    }

    private class BtnRevListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intentRev = new Intent(EventDetailsActivity.this,CreateEventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("event_name",e.getEventName());
            bundle.putString("event_discription",e.getEventDescription());
            bundle.putInt("event_year",e.getEventDate().get(Calendar.YEAR));
            bundle.putInt("event_month",e.getEventDate().get(Calendar.MONTH)+1);
            bundle.putInt("event_day",e.getEventDate().get(Calendar.DAY_OF_MONTH));
            bundle.putInt("event_hour",e.getEventDate().get(Calendar.HOUR_OF_DAY));
            bundle.putInt("event_minute",e.getEventDate().get(Calendar.MINUTE));
            bundle.putInt("event_image",e.getEventImage());
            bundle.putInt("flag",1);
            intentRev.putExtras(bundle);
            intentRev.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intentRev);
            EventDetailsActivity.this.finish();
        }
    }

}
