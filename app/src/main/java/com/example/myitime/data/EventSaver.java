package com.example.myitime.data;

import android.content.Context;

import com.example.myitime.data.model.Event;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EventSaver {
    Context context;
    ArrayList<Event> events = new ArrayList<Event>();

    public ArrayList<Event> getEvents() {
        return events;
    }

    public EventSaver(Context context) {
        this.context = context;
    }

    public void save(){
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(events);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Event> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            events = (ArrayList<Event>) inputStream.readObject();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return events;
    }

}
