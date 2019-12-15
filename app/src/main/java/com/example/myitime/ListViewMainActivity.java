package com.example.myitime;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myitime.data.EventSaver;
import com.example.myitime.data.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.valueOf;

public class ListViewMainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 100;
    public static final int RESULT_CODE_ADD_OK = 101;
    public static final int RESULT_CODE_ADD_CANCEL = 102;
    public static final int REQUEST_CODE_DEL_REV = 200;
    public static final int REQUEST_CODE_DEL = 300;
    public static final int RESULT_CODE_DEL_OK = 301;
    public static final int RESULT_CODE_DEL_CANCEL = 302;
    public static final int REQUEST_CODE_REV = 400;
    public static final int RESULT_CODE_REV_OK = 401;
    public static final int RESULT_CODE_REV_CANCEL = 402;
    private ListView listViewEvents;
    private List<Event> eventList = new ArrayList<>();
    private Button btnCreateEvent;
    private EventAdapter adapter;
    private int eventPosition;
    private EventSaver eventSaver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_main);

        eventSaver = new EventSaver(this);
        eventList = eventSaver.load();

        listViewEvents = this.findViewById(R.id.list_view_events);
        adapter = new EventAdapter(this, R.layout.list_view_event, eventList);

        adapter.notifyDataSetChanged();

        listViewEvents.setAdapter(adapter);
        listViewEvents.setOnItemClickListener(new EventClickListener());

        btnCreateEvent = findViewById(R.id.button_create_event);
        btnCreateEvent.setOnClickListener(new BtnClickListener());
    }

    class EventAdapter extends ArrayAdapter<Event>{

        private int resourceId;

        public EventAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            for(int i = 0; i < eventList.size(); i++){
                eventList.get(i).CalLeft();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);//获取当前项的实例

            String tmp1 = "\n" +
                    valueOf(event.getEventDate().get(Calendar.YEAR)) + "/" +
                    valueOf(event.getEventDate().get(Calendar.MONTH) + 1) + "/" +
                    valueOf(event.getEventDate().get(Calendar.DAY_OF_MONTH));
            if (!event.getEventDescription().isEmpty()) {
                tmp1 += ("\n" + event.getEventDescription());
            }

            String tmp2 = "";
            if (event.getLeftTotalDay() > 0) {
                tmp2 = valueOf(event.getLeftTotalDay()) + " DAYS";
            } else {
                if (event.getLeftTotalHour() > 0) {
                    tmp2 = valueOf(event.getLeftTotalHour()) + " HRS";
                } else {
                    if (event.getLeftTotalMinute() > 0) {
                        tmp2 = valueOf(event.getLeftTotalMinute()) + " MINS";
                    } else {
                        if (event.getLeftTotalSecond() >= 0) {
                            tmp2 = valueOf(event.getLeftTotalSecond()) + " SECS";
                        }
                    }
                }
            }
            Calendar now = Calendar.getInstance();
            if (now.after(event.getEventDate())) {
                tmp2 += "\nAGO";
            }

            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_event)).setImageResource(event.getEventImage());
            ((TextView) view.findViewById(R.id.text_view_event)).setText(event.getEventName() + tmp1);
            ((TextView) view.findViewById(R.id.text_view_total_time)).setText(tmp2);

            return view;
        }
    }

    class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ListViewMainActivity.this, CreateEventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("event_name", "Anniversary,birthday,festival");
            bundle.putString("event_discription", "Description(Optional)");
            Calendar now = Calendar.getInstance();
            bundle.putInt("event_year", now.get(Calendar.YEAR));
            bundle.putInt("event_month", now.get(Calendar.MONTH) + 1);
            bundle.putInt("event_day", now.get(Calendar.DAY_OF_MONTH));
            bundle.putInt("event_hour", now.get(Calendar.HOUR_OF_DAY));
            bundle.putInt("event_minute", now.get(Calendar.MINUTE));
            double ram = Math.random();
            int i = (int) (ram * 4);
            int image = 0;
            switch (i + 1) {
                case 1:
                    image = R.drawable.clock1;
                    break;
                case 2:
                    image = R.drawable.clock2;
                    break;
                case 3:
                    image = R.drawable.clock3;
                    break;
                case 4:
                    image = R.drawable.clock4;
                    break;
            }
            bundle.putInt("event_image", image);
            bundle.putInt("flag", 0);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        }
    }

    class EventClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            eventPosition = i;
            Intent intent = new Intent(ListViewMainActivity.this, EventDetailsActivity.class);
            intent.putExtra("event_name", eventList.get(i).getEventName());
            intent.putExtra("event_year", eventList.get(i).getEventDate().get(Calendar.YEAR));
            intent.putExtra("event_month", eventList.get(i).getEventDate().get(Calendar.MONTH) + 1);
            intent.putExtra("event_day", eventList.get(i).getEventDate().get(Calendar.DAY_OF_MONTH));
            intent.putExtra("event_hour", eventList.get(i).getEventDate().get(Calendar.HOUR_OF_DAY));
            intent.putExtra("event_minute", eventList.get(i).getEventDate().get(Calendar.MINUTE));
            intent.putExtra("event_image", eventList.get(i).getEventImage());
            intent.putExtra("event_discription", eventList.get(i).getEventDescription());
            startActivityForResult(intent,REQUEST_CODE_DEL_REV);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                if (resultCode == RESULT_CODE_ADD_OK) {
                    Bundle bundle = data.getExtras();
                    eventList.add(new Event(
                            bundle.getInt("eventYear"),
                            bundle.getInt("eventMonth"),
                            bundle.getInt("eventDay"),
                            bundle.getInt("eventHour"),
                            bundle.getInt("eventMinute"),
                            bundle.getString("eventName"),
                            bundle.getString("eventDiscription"),
                            bundle.getInt("eventImage")
                    ));
                }
                break;
            case REQUEST_CODE_DEL_REV:
                if (resultCode == RESULT_CODE_DEL_OK) {
                    eventList.remove(eventPosition);
                }
                else if (resultCode == RESULT_CODE_REV_OK) {
                    Bundle bundle = data.getExtras();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(
                            bundle.getInt("eventYear"),
                            bundle.getInt("eventMonth") - 1,
                            bundle.getInt("eventDay"),
                            bundle.getInt("eventHour"),
                            bundle.getInt("eventMinute"),
                            0
                    );
                    eventList.get(eventPosition).setEventImage(bundle.getInt("eventImage"));
                    eventList.get(eventPosition).setEventName(bundle.getString("eventName"));
                    eventList.get(eventPosition).setEventDescription(bundle.getString("eventDiscription"));
                    eventList.get(eventPosition).setEventDate(calendar);
                }
                break;
        }
        Collections.sort(eventList);
        adapter.notifyDataSetChanged();
    }
}

