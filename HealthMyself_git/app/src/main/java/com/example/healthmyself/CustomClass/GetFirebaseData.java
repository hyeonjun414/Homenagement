package com.example.healthmyself.CustomClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.healthmyself.R;
import com.example.healthmyself.models.FirebaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;


public class GetFirebaseData {
    private DatabaseReference mDatabase;

    boolean flag;
    FirebaseData data = new FirebaseData();

    public GetFirebaseData(){

    }

    public void setFlag(boolean flag){ this.flag = flag; }

    public boolean getFlag() { return this.flag; }

    public FirebaseData getData(){ return this.data; }

    public void getFirebaseData(Calendar calendar, RelativeLayout layout, TextView ex, TextView w, Context context){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        String full_day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.CALENDAR_DAY_FORMAT);

        mDatabase.child(uid).child("calandar").child(full_day).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    if(String.valueOf(task.getResult().getValue()).equals("null")){
                        setFlag(false);
                        layout.setBackground(null);
                        ex.setText("");
                        w.setText("");
                    }
                    else{
                        layout.setBackground(ContextCompat.getDrawable(context, R.drawable.day_background));
                        setFlag(true);
                        data.setEx(String.valueOf(task.getResult().child("ex").getValue()));
                        data.setTime(String.valueOf(task.getResult().child("time").getValue()));
                        data.setWeight(String.valueOf(task.getResult().child("weight").getValue()));

                        ex.setText(data.getEx());
                        w.setText(data.getWeight()+"kg");

                    };
                }
                Calendar day_current = Calendar.getInstance();
                String day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.CALENDAR_DAY_FORMAT);
                String day2 = DateUtil.getDate(day_current.getTimeInMillis(), DateUtil.CALENDAR_DAY_FORMAT);

                if(day.equals(day2))
                {
                    layout.setBackground(ContextCompat.getDrawable(context, R.drawable.current_day_background));
                }
            }
        });

    }


}