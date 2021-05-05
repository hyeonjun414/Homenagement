package com.example.healthmyself.CustomClass;

import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class GetFirebaseData {
    private DatabaseReference mDatabase;

    boolean flag;
    String data = "";

    public GetFirebaseData(){

    }

    public void setFlag(boolean flag){ this.flag = flag; }

    public boolean getFlag() { return this.flag; }

    public void setData(String data){ this.data = data;}

    public String getData(){ return this.data; }

    public void getFirebaseData(Calendar calendar, RelativeLayout layout){
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
                        layout.setBackgroundColor(Color.WHITE);
                    }else{
                        layout.setBackgroundColor(Color.YELLOW);
                        setFlag(true);
                        setData(String.valueOf(task.getResult().getValue()));
                        Log.d("Day", String.valueOf(task.getResult().getValue()));
                    };
                }
            }
        });

    }


}