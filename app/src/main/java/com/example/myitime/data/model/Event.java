package com.example.myitime.data.model;

import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable,Comparable{

    private Calendar eventDate;

    private int leftYear;
    private int leftMonth;
    private int leftDay;
    private long leftHour;
    private long leftMinute;
    private long leftSecond;

    private long leftTotalDay;
    private long leftTotalHour;
    private long leftTotalMinute;
    private long leftTotalSecond;

    private String eventName;
    private String eventDescription;
    private int eventImage;

    public Event(int eventYear,int eventMonth, int eventDay, int eventHour,int eventMinute,
                 String eventName, String eventDescription, int eventImage) {
        setEventDate(Calendar.getInstance());
        this.getEventDate().set(Calendar.YEAR,eventYear);
        this.getEventDate().set(Calendar.MONTH,eventMonth-1);
        this.getEventDate().set(Calendar.DAY_OF_MONTH,eventDay);
        this.getEventDate().set(Calendar.HOUR_OF_DAY,eventHour);
        this.getEventDate().set(Calendar.MINUTE,eventMinute);
        this.getEventDate().set(Calendar.SECOND,0);
        this.getEventDate().set(Calendar.MILLISECOND, 0);
        this.setEventName(eventName);
        this.setEventDescription(eventDescription);
        this.setEventImage(eventImage);
        CalLeft();
    }

    public void CalLeft(){
        Calendar now = Calendar.getInstance();

        Calendar earlyDate = Calendar.getInstance();
        Calendar lateDate = Calendar.getInstance();
        if(now.after(eventDate)){
            earlyDate = (Calendar) eventDate.clone();
            lateDate = (Calendar) now.clone();
        }
        else{
            earlyDate = (Calendar) now.clone();
            lateDate = (Calendar) eventDate.clone();
        }

        long tmpTime = lateDate.getTime().getTime() - earlyDate.getTime().getTime();

        leftYear = lateDate.get(Calendar.YEAR) - earlyDate.get(Calendar.YEAR);
        leftMonth = lateDate.get(Calendar.MONTH) - earlyDate.get(Calendar.MONTH);
        leftDay = lateDate.get(Calendar.DAY_OF_MONTH) - earlyDate.get(Calendar.DAY_OF_MONTH);

        if(getLeftDay() < 0) {
            leftMonth = getLeftMonth() - 1;
            lateDate.add(Calendar.MONTH,-1);
            leftDay = getLeftDay() + lateDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if(getLeftMonth() < 0) {
            leftMonth = (getLeftMonth() + 12) % 12;
            leftYear = getLeftYear() - 1;
        }

        long days = tmpTime/(60*60*24*1000);
        leftHour = (tmpTime-days*(60*60*24*1000))/(60*60*1000);
        leftMinute = (tmpTime-days*(60*60*24*1000)- getLeftHour() *(60*60*1000))/(60*1000);
        leftSecond = (tmpTime/1000-days*60*60*24- getLeftHour() *60*60- getLeftMinute() *60);

        leftTotalSecond = tmpTime/1000;
        leftTotalMinute = tmpTime/(60*1000);
        leftTotalHour = tmpTime/(60*60*1000);
        leftTotalDay = tmpTime/(60*60*24*1000);
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
        CalLeft();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getEventImage() {
        return eventImage;
    }

    public void setEventImage(int eventImage) {
        this.eventImage = eventImage;
    }

    public int getLeftYear() {
        return leftYear;
    }

    public int getLeftMonth() {
        return leftMonth;
    }

    public int getLeftDay() {
        return leftDay;
    }

    public long getLeftHour() {
        return leftHour;
    }

    public long getLeftMinute() {
        return leftMinute;
    }

    public long getLeftSecond() {
        return leftSecond;
    }

    public long getLeftTotalDay() {
        return leftTotalDay;
    }

    public long getLeftTotalHour() {
        return leftTotalHour;
    }

    public long getLeftTotalMinute() {
        return leftTotalMinute;
    }

    public long getLeftTotalSecond() {
        return leftTotalSecond;
    }

    @Override
    public int compareTo(Object o) {
        Event e = (Event) o;
        if(this.getEventDate().after(e.getEventDate())){
            return 1;
        }
        else if(this.getEventDate().before(e.getEventDate())){
            return -1;
        }
        else{
            return 0;
        }
    }
}
