package com.example.healthmyself.CustomClass;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayObject extends Object{

    GregorianCalendar gregori;
    String day;
    String text;

    public DayObject() {
    }


    public void setGregori(GregorianCalendar gregori){
        this.gregori = gregori;
    }
    public GregorianCalendar setGregori(){
        return gregori;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    public String getText() {
        return text;
    }
    public void setText(String day) {
        this.text = text;
    }


    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){

        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);

    }

}
