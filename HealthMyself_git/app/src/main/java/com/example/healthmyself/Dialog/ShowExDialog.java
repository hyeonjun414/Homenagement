package com.example.healthmyself.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthmyself.CustomClass.DateUtil;
import com.example.healthmyself.CustomClass.PostCalendar;
import com.example.healthmyself.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShowExDialog extends Activity {
    private Context mContext;
    String ex="";
    String time="";
    String video="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_ex_dialog);
        TextView txt_ex = findViewById(R.id.txt_showEx_ex);
        TextView txt_time = findViewById(R.id.txt_showEx_time);
        TextView txt_video = findViewById(R.id.txt_showEx_video);
        Button cancelButton = findViewById(R.id.btn_showEx_Cancel);

        ex = getIntent().getStringExtra("ex");
        time = getIntent().getStringExtra("time");
        video = getIntent().getStringExtra("video");

        txt_ex.setText(ex);
        txt_time.setText(time);
        txt_video.setText(video);

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '취소' 버튼 클릭시
                // ...코드..
                // Custom Dialog 종료
                finish();
            }
        });
    }
}