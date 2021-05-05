package com.example.healthmyself.models.CalendarView;

import com.example.healthmyself.CustomClass.DateUtil;

import java.util.Calendar;

public class Day extends ViewModel {

    String day;
    String day_text;
    int day_of_week;
    Calendar cal;

    public Day() {
    }

    public Day classda(){
        return this;
    }

    public int getDOW() {return day_of_week;}

    public String getDay() {
        return day;
    }

    public void setCal (Calendar cal){
        this.cal = cal;
    }

    public Calendar getCal (){
        return this.cal;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getText() {
        return day_text;
    }

    public void setText(String flag) {
        this.day_text = flag;
    }


    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){
        setCal(calendar);
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);


    }

}